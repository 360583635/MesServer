package com.job.dispatchService.lineManager.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Process;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.common.pojo.Users;
import com.job.common.redis.RedisCache;
import com.job.common.result.Result;
import com.job.common.utils.JwtUtil;
import com.job.dispatchService.lineManager.dto.ProcessDto;
import com.job.dispatchService.lineManager.dto.ProcessUpdateDto;
import com.job.dispatchService.lineManager.request.ProcessPageReq;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.ProcessMaterialRelationService;
import com.job.dispatchService.lineManager.service.ProcessService;
import com.job.dispatchService.lineManager.vo.EquipmentVo;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import com.job.feign.clients.AuthenticationClient;
import com.job.feign.clients.ProductionManagementClient;
import com.job.feign.pojo.Equipment;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:05
 * @description
 */
@RestController
@RequestMapping("/dispatch/process")
@Slf4j
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private FlowProcessRelationService processRelationService;

    @Autowired
    private ProductionManagementClient productionManagementClient;

    @Autowired
    private ProcessMaterialRelationController processMaterialRelationController;

    @Autowired
    private ProcessMaterialRelationService processMaterialRelationService;

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private RedisCache redisCache;

    //逻辑删除1未删除0已删除
    private static int IS_DELETE=1;
    /**
     * 工序分页查询
     * @param req
     * @return
     */
    @PostMapping("/page")
    public Result page(@RequestBody ProcessPageReq req){
        LambdaQueryWrapper<Process> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Process::getIsDelete,IS_DELETE);
        IPage result = processService.page(req,queryWrapper);
        return Result.success(result,"查询成功");
    }

    /**
     * 修改工序
     * @param processDto
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Result updateProcess(@RequestBody ProcessDto processDto, HttpServletRequest request) throws Exception {

        //获得用户信息
        //String userId= UserUtil.getUserId(httpServletRequest);
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            Users users = BeanUtil.copyProperties(redisCache.getCacheObject("login"+userId), Users.class);
            String name = users.getName();
            //System.out.println(userId);
            processDto.setUpdateUsername(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

        processDto.setUpdateTime(DateUtil.date());
        Process process = new Process();
        BeanUtils.copyProperties(processDto,process);

        LambdaQueryWrapper<Process> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Process::getIsDelete,1)
                .eq(Process::getProcess, processDto.getProcess());
        boolean b = processService.updateById(process);
        if(b==true) {
            Process process1 = processService.getOne(queryWrapper);
            processDto.setId(process1.getId());
            Result result = processMaterialRelationController.addOrUpdate(processDto);
            if(result.getCode()==1){
                return Result.success(null,"修改成功");
            }
        }
        return Result.error("工序修改失败");

    }

    /**
     * 增加工序
     * @param processDto
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result saveProcess(@RequestBody ProcessDto processDto, HttpServletRequest request) throws Exception {
        //获得用户信息
        //String userId= UserUtil.getUserId(httpServletRequest);
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            Users users = BeanUtil.copyProperties(redisCache.getCacheObject("login"+userId), Users.class);
            String name = users.getName();
            //System.out.println(userId);
            processDto.setUpdateUsername(name);
            processDto.setCreateUsername(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        DateTime nowTime = DateUtil.date();
        processDto.setUpdateTime(nowTime);
        processDto.setCreateTime(nowTime);
        Process process = new Process();
        BeanUtils.copyProperties(processDto,process);

        LambdaQueryWrapper<Process> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Process::getIsDelete,1)
                .eq(Process::getProcess, processDto.getProcess());
        long count = processService.count(queryWrapper);
        if(count>0){
            return Result.error("工序名称不可重复，请重新添加");
        }
        boolean b = processService.saveOrUpdate(process);
        if(b==true) {
            Process process1 = processService.getOne(queryWrapper);
            processDto.setId(process1.getId());
            Result result = processMaterialRelationController.addOrUpdate(processDto);
            if(result.getCode()==1){
                return Result.success(null,"增加成功");
            }
        }
        return Result.error("增加失败");
    }

    /**
     * 逻辑删除工序
     * @param processId
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public Result removePeocess(@RequestParam String processId){
        LambdaQueryWrapper<FlowProcessRelation> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(FlowProcessRelation::getProcessId,processId);
        long count = processRelationService.count(queryWrapper);
//        List<TFlowProcessRelation> list = processRelationService.list(queryWrapper);
        if(count>0){
            return Result.error("请先删除与本工序有关的流程");
        }
        LambdaUpdateWrapper<Process> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.set(Process::getIsDelete,0).eq(Process::getId,processId);
        boolean b = processService.update(updateWrapper);
        LambdaUpdateWrapper<ProcessMaterialRelation> queryWrapper1 = new LambdaUpdateWrapper<>();
        queryWrapper1
                .eq(ProcessMaterialRelation::getProcessId,processId)
                .set(ProcessMaterialRelation::getIsDelete,0);
        boolean update = processMaterialRelationService.update(queryWrapper1);

        if(b&&update){
            return Result.success(null,"删除成功");
        }
        return Result.error("操作失败，请刷新页面重试");
    }

    /**
     * 批量逻辑删除
     * @param idList
     * @return
     */
    @PostMapping("/batchDelete")
    @ResponseBody
    public Result batchDeleteById(@RequestParam List<String> idList){

//        List< FlowProcessRelation> flowProcessRelations = processRelationService.listByIds(idList);
        LambdaQueryWrapper<FlowProcessRelation> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(FlowProcessRelation::getProcessId,idList);
        long count = processRelationService.count(queryWrapper);
        if(count>0){
            return Result.error("请保证删除的工序没有流程与之绑定");
        }
        // 获取需要逻辑删除的记录的ID列表
        Vector<Process> recordList = new Vector<>();
        Vector<ProcessMaterialRelation> processMaterialRelationList = new Vector<>();
        for (String id : idList) {
            Process process=new Process();
            process.setId(id);
            process.setIsDelete(0);  // 设置要更新的字段和值
            recordList.add(process);

            ProcessMaterialRelation processMaterialRelation = new ProcessMaterialRelation();
            processMaterialRelation.setProcessId(id);
            processMaterialRelation.setIsDelete(0);
            processMaterialRelationList.add(processMaterialRelation);
        }

        boolean b = processService.updateBatchById(recordList);

        boolean b1 = processMaterialRelationService.updateBatchById(processMaterialRelationList);
        if(b&&b1){
            return Result.success(null,"查询成功");
        }
        return Result.error("删除失败");


    }



    /**
     * 根据工序名模糊查询
     * @param searchName
     * @return
     */
    @PostMapping("/likeQuery")
    public Result<ProcessPageReq> likeQuery(@RequestParam String searchName,@RequestParam int size,@RequestParam int current){
        ProcessPageReq req=new ProcessPageReq();
        req.setCurrent(current);
        req.setSize(size);
        if(StringUtil.isNullOrEmpty(searchName)){
            ProcessPageReq page = processService.page(req);
            return Result.success(page,"查询成功");
       }
        boolean matches = searchName.matches("-?\\d+(\\.\\d+)?");
        LambdaQueryWrapper<Process> queryWrapper=new LambdaQueryWrapper<>();
        if(matches){
            queryWrapper.eq(Process::getId,searchName);
        }else {
            queryWrapper.like(Process::getProcess, searchName);
        };
        ProcessPageReq page = processService.page(req, queryWrapper);
        return Result.success(page,"查询成功");
    }
    /**
     * 查询全部工序
     */
    @GetMapping("/list")
    public Result list(){
        LambdaQueryWrapper<Process> queryWrapper = new LambdaQueryWrapper();
        queryWrapper
                .eq(Process::getIsDelete,IS_DELETE);
//                .eq(Process::getEquipmentId,0);
        List<Process> list = processService.list(queryWrapper);
        return Result.success(list,"查询成功");
    }

    /**
     * 查询全部设备
     */
    @GetMapping("/queryEquipments")
    public Result queryEquipments(){
        List<Equipment> equipmentList = productionManagementClient.queryEquipments();
        return Result.success(equipmentList,"查询成功");
    }

    /**
     * 查询全部设备类型
     */
    @GetMapping("/queryEquipmentTypes")
    public Result<List<String>> queryEquipmentTypes(HttpServletRequest request){
        String token = request.getHeader("token");
        List<String> equipmentTypes = productionManagementClient.queryEquipmentTypes(token);
        return Result.success(equipmentTypes,"查询成功");
    }

    /**
     * 根据设备功能类型查询设备
     */
    @GetMapping("/queryEquipmentsByType/{functionName}")
    public Result queryEquipmentsByType(@PathVariable("functionName") String functionName){
        List<Equipment> equipmentList = productionManagementClient.queryEquipmentsByType(functionName);
        /*List<String> equipmentNameList = new ArrayList<>();
        for(Equipment equipment : equipmentList){
            String equipmentName = equipment.getEquipmentName();
            equipmentNameList.add(equipmentName);
        }*/
        return Result.success(equipmentList,"查询成功");
    }

    @GetMapping("/queryEquipmentMap")
    public Result queryEquipmentMap(HttpServletRequest request){
        String token = request.getHeader("token");
        List<String> equipmentTypes = productionManagementClient.queryEquipmentTypes(token);
        List<EquipmentVo> equipmentVoList = new ArrayList<>();
        for(String equipmentType : equipmentTypes){
            EquipmentVo equipmentVo = new EquipmentVo();
            equipmentVo.setTitle(equipmentType);
            List<Equipment> equipmentList = productionManagementClient.queryEquipmentsByType(equipmentType);
            List<Map<String,String>> mapList = new ArrayList<>();
            for(Equipment equipment : equipmentList){
                Map<String,String> map = new HashMap<>();
                map.put("id", String.valueOf(equipment.getEquipmentID()));
                map.put("title",equipment.getEquipmentName());
                mapList.add(map);
            }
            equipmentVo.setEquipmentMapList(mapList);
        }

        return Result.success(equipmentVoList,"查询成功");
    }

    /**
     * 根据id查询信息
     * @param id
     * @return
     */
    @PostMapping("/queryById")
    public Result queryById(@RequestParam String id){
        LambdaQueryWrapper<Process> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(Process::getIsDelete,1)
                .eq(Process::getId,id);
        Process process = processService.getOne(lambdaQueryWrapper);
        return Result.success(process,"查询成功");
    }

    /**
     *
     */
    @PostMapping("/queryProcessUpdateDtoById")
    public Result queryProcessUpdateDtoById(@RequestParam String id){
        if(id==null){
            return Result.error("传入参数为空");
        }
        log.info("设备id："+id);
        Process process = (Process) queryById(id).getData();
        ProcessUpdateDto processUpdateDto = new ProcessUpdateDto();
        BeanUtil.copyProperties(process,processUpdateDto);
        String equipmentId = process.getEquipmentId();

        Equipment equipment = productionManagementClient.queryEquipmentById(equipmentId);
        if(equipment==null){
            return Result.error("设备未查询成功");
        }
        processUpdateDto.setFunction(equipment.getFunctionName());
        processUpdateDto.setEquipmentName(equipment.getEquipmentName());

        LambdaQueryWrapper<ProcessMaterialRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ProcessMaterialRelation::getIsDelete,1)
                .eq(ProcessMaterialRelation::getProcessId,id);
        List<ProcessMaterialRelation> processMaterialRelations = processMaterialRelationService.list(queryWrapper);
        List<MaterialVo> materialVoList = new Vector<>();
        for(ProcessMaterialRelation processMaterialRelation : processMaterialRelations){
            MaterialVo materialVo = new MaterialVo();
            materialVo.setTitle(processMaterialRelation.getMaterialName());
            materialVo.setValue(processMaterialRelation.getMaterialId());
            materialVo.setNumber(processMaterialRelation.getNumber());
            materialVoList.add(materialVo);
        }
        processUpdateDto.setMaterialVoList(materialVoList);
        return Result.success(processUpdateDto,"查询成功");
    }
}
