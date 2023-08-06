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


        return  processService.updateProcess(processDto,request);
        //获得用户信息
        //String userId= UserUtil.getUserId(httpServletRequest);


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
        return processService.saveProcess(processDto,request);

    }

    /**
     * 逻辑删除工序
     * @param processId
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public Result removePeocess(@RequestParam String processId){


        return processService.removePeocess(processId);

    }

    /**
     * 批量逻辑删除
     * @param idList
     * @return
     */
    @PostMapping("/batchDelete")
    @ResponseBody
    public Result batchDeleteById(@RequestParam List<String> idList){

        return processService.batchDeleteById(idList);
//        List< FlowProcessRelation> flowProcessRelations = processRelationService.listByIds(idList);



    }



    /**
     * 根据工序名模糊查询
     * @param searchName
     * @return
     */
    @PostMapping("/likeQuery")
    public Result<ProcessPageReq> likeQuery(@RequestParam String searchName,@RequestParam int size,@RequestParam int current){
        return processService.likeQuery(searchName,size,current);


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
        return processService.queryEquipmentMap(request);


    }

    /**
     * 根据id查询信息
     * @param id
     * @return
     */
    @PostMapping("/queryById")
    public Result queryById(@RequestParam String id){
        return processService.queryById(id);

    }

    /**
     *
     */
    @PostMapping("/queryProcessUpdateDtoById")
    public Result queryProcessUpdateDtoById(@RequestParam String id){
        return processService.queryProcessUpdateDtoById(id);

    }
}
