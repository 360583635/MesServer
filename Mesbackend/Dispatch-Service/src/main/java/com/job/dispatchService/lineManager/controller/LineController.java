package com.job.dispatchService.lineManager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.job.common.pojo.Line;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.request.LinePageReq;
import com.job.dispatchService.lineManager.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.job.dispatchService.lineManager.controller.LineTaskController.findThreadByName;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:06
 * @description
 */

@RestController
@RequestMapping("/dispatch/line")
public class LineController {

    @Autowired
    private LineService lineService;



    /**
     * 流水线分页查询
     * @param req
     * @return
     */
    @PostMapping
    public Result page(LinePageReq req){
        IPage result = lineService.page(req);
        return Result.success(result,"查询成功");
    }

    /**
     * 添加流水线
     * @param pipeLine
     * @return
     */
    @RequestMapping("/saveLine")
    @ResponseBody
    public Result saveLine(@RequestBody Line pipeLine){

        String user="wen"; //获取用户信息
        pipeLine.setOrderCount("0");
        pipeLine.setLineStatus("0"); //设置状态为空闲
        lineService.save(pipeLine);
        //ToDo 调用日志接口
        return Result.success(null,"添加成功");
    }

    /**
     * 修改流水线
     * @param pipeLine
     * @return
     */
    @RequestMapping("/updateLine")
    @ResponseBody
    public Result updateLine(@RequestBody Line pipeLine){
        UpdateWrapper updateWrapper=new UpdateWrapper();
        String user="wen";//获取当前用户信息
        lineService.updateById(pipeLine);
        //ToDo 调用日志接口
        return Result.success(null,"修改成功");
    }
    /**
     * 删除流水线
     * @param lineId
     * @return
     */
    @GetMapping("/removeLine/{lineId}")
    public Result removeLine(@PathVariable String lineId){
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Line::getIsDelete,1)
                .eq(Line::getId,lineId);
        Line byId = lineService.getById(lineId);
        if(!"1".equals(byId.getLineStatus())){
            return Result.error("流水线未关闭，请先关闭流水线");
        }
        byId.setIsDelete(0);
        return Result.success(null,"删除成功");
    }

    /**
     * 查询全部流水线
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result list(){
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper();
        List<Line> list = lineService.list(queryWrapper);
        return Result.success(list,"查询成功");
    }

    /**
     * 根据id查询流水线
     */
    @GetMapping("/selectLineById/{id}")
    public Result selectLineById(@PathVariable("id") String id){
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper();
        queryWrapper
                .eq(Line::getIsDelete,1)
                .eq(Line::getId,id);
        Line line = lineService.getOne(queryWrapper);
        if(line==null){
            return Result.error("查询失败");
        }
        return Result.success(line,"查询成功");
    }


    /**
     * 停止流水线，将流水线状态改为 “停机” 且 将流水线实体线程停止
     * @param id
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/haltLine/{id}")
    public Result haltLine(@PathVariable("id") String id) throws InterruptedException {
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper();
        queryWrapper
                .eq(Line::getIsDelete,1)
                .eq(Line::getId,id);
        Line line = lineService.getOne(queryWrapper);
        if(line==null){
            return Result.error("该流水线不存在");
        }
        line.setLineStatus("1");
        String lineName = line.getLine();
        String lineId = line.getId();
        Thread threadByName = findThreadByName(lineName+lineId);
        if(threadByName!=null){
            threadByName.interrupt();
        }
        return Result.success(null,"该流水线已关闭");
    }



}