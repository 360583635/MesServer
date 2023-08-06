package com.job.dispatchService.lineManager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Flow;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Users;
import com.job.common.redis.RedisCache;
import com.job.common.result.Result;
import com.job.common.utils.JwtUtil;
import com.job.dispatchService.lineManager.dto.FlowDto;
import com.job.dispatchService.lineManager.mapper.FlowMapper;
import com.job.dispatchService.lineManager.mapper.FlowProcessRelationMapper;
import com.job.dispatchService.lineManager.mapper.ProcessMapper;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;

import com.job.dispatchService.lineManager.service.FlowService;
import com.job.dispatchService.lineManager.service.ProcessService;
import com.job.dispatchService.lineManager.vo.ProcessVo;
import com.job.feign.clients.AuthenticationClient;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import com.job.common.pojo.Process;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FlowProcessRelationServiceImpl extends ServiceImpl<FlowProcessRelationMapper, FlowProcessRelation> implements FlowProcessRelationService {

    Logger log = LoggerFactory.getLogger(FlowProcessRelationServiceImpl.class);

    /**
     * 工序基础数据服务
     */


    @Resource
    private ProcessMapper processMapper;

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private RedisCache redisCache;

    /**
     * 流程基础数据服务
     */
    @Autowired
    public FlowMapper flowMapper;

    /**
     * 流程与工序关系
     */
    @Autowired
    public FlowProcessRelationMapper flowProcessRelationMapper;


    /**
     * 流程与工序关系新增与修改
     *
     * @param flowDto 流程信息DTO
     * @return
     * @throws Exception
     */
    @Override
    public Result addOrUpdate(FlowDto flowDto) throws Exception {
        List<ProcessVo> processVoList = flowDto.getProcessVoList();
        List<FlowProcessRelation> flowProcessRelationList = new ArrayList<>();
        StringBuilder processBuilder = new StringBuilder();
        if(CollectionUtils.isEmpty(processVoList)||processVoList.size() <= 1){
            throw new Exception("流程下必须存在至少两个工序");
        }
        Flow flow = new Flow();
        BeanUtils.copyProperties(flowDto,flow);
        String flowId = flow.getId();
        String flowName = flow.getFlow();
        if(StringUtils.isNotEmpty(flowId)){
            LambdaQueryWrapper<FlowProcessRelation> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(FlowProcessRelation::getFlowId,flowId);
            flowProcessRelationMapper.delete(queryWrapper);
//            flowProcessRelationMapper.deleteProcessRelationByFlowId(flowId);
        }else{
            //如果流程头表为空先创建一下
            /*flowService.saveOrUpdate(flow);*/
            int i = flowMapper.insert(flow);
            if(i==0){
                return Result.error("更新失败");
            }
            flowId = flow.getId();

        }
        // 批量处理需要插入数据库的工序
        log.info("开始处理流程下工序关系");
        for(int i=0;i<processVoList.size();i++){
            FlowProcessRelation relation = new FlowProcessRelation();
            relation.setFlowId(flowId);//流程ID
            relation.setFlow(flowName);//流程编码
            Process process = processMapper.selectById(processVoList.get(i).getValue());
            if(i == 0){//首个工序
                relation.setPerProcess("");
                relation.setPerProcess("");
                //下一道工序
                relation.setNextProcessId(processVoList.get(i + 1).getValue());
                relation.setNextProcess(processVoList.get(i + 1).getTitle());
                relation.setProcessType("firstProcess");
            }else if (i + 1 >= processVoList.size()) {//末尾工序
                //前一道工序
                relation.setPerProcessId(processVoList.get(i - 1).getValue());
                relation.setPerProcess(processVoList.get(i - 1).getTitle());
                relation.setNextProcessId("");
                relation.setNextProcess("");
                relation.setProcessType("lastProcess");
            }else {
                //前一道工序
                relation.setPerProcessId(processVoList.get(i - 1).getValue());
                relation.setPerProcess(processVoList.get(i - 1).getTitle());
                //下一道工序
                relation.setNextProcessId(processVoList.get(i + 1).getValue());
                relation.setNextProcess(processVoList.get(i + 1).getTitle());
            }
            //当前工序
            relation.setProcessId(processVoList.get(i).getValue());
            relation.setProcess(processVoList.get(i).getTitle());
            relation.setSortNum(i + 1);//顺序
            relation.setIsDelete(1);
            if (i == 0) {
                processBuilder.append(process.getProcessDesc());
            } else {
                processBuilder.append("->" + process.getProcessDesc());
            }
            flowProcessRelationList.add(relation);
        }
        log.info("本次流程时序" + processBuilder.toString());
        flow.setProcess(processBuilder.toString());
        //更细流程头表信息
        /*flowService.saveOrUpdate(flow);*/
        int i = flowMapper.updateById(flow);
        boolean b = saveOrUpdateBatch(flowProcessRelationList);
        if(b==true&&i!=0){
            return Result.success(null,"更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 全部工序集合
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<ProcessVo> allProcessViewServer() throws Exception {
        List<Process> processList = processMapper.selectList(null);
        List<ProcessVo> processvos = new ArrayList<>();
        //得出全部的工序数据
        for(Process process : processList) {
            ProcessVo processVo = new ProcessVo();
            processVo.setValue(process.getId());
            processVo.setTitle(process.getProcess());
            processvos.add(processVo);
        }
        return processvos;
    }


    /**
     * 绘制当前流程下的工序
     *
     * @param flowId
     * @return
     * @throws Exception
     */
    @Override
    public List<ProcessVo> currentProcessViewServer(String flowId) throws Exception {
        return flowProcessRelationMapper.queryProcessRelationByFlowId(flowId);
    }

    @Override
    public Result addOrUpdateService(FlowDto flowDto, HttpServletRequest request) throws Exception {
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            Users users = BeanUtil.copyProperties(redisCache.getCacheObject("login"+userId), Users.class);
            String name = users.getName();
            //System.out.println(userId);
            flowDto.setUpdateUsername(name);
            if(StringUtils.isNotEmpty(flowDto.getCreateUsername())&&flowDto.getCreateUsername()!=""){
                flowDto.setCreateUsername(name);
                flowDto.setCreateTime(DateUtil.date());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        flowDto.setUpdateTime(DateUtil.date());
        return addOrUpdate(flowDto);
    }

    @Override
    public Result deleteByTableNameId(FlowDto req) {
        //先删除流程头表
        /*flowService.removeById(req.getId());*/
        flowMapper.deleteById(req.getId());
        //删除流程关系表
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper
                .eq("flow_id", req.getId());
        boolean remove = remove(queryWrapper);
        if(remove){
            return Result.success(null,"删除成功");
        }
        return Result.error("删除失败");
    }


}