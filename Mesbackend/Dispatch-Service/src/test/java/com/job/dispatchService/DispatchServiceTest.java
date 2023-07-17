package com.job.dispatchService;

import com.job.dispatchService.lineManager.controller.LineTaskController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 庸俗可耐
 * @create 2023-07-10-22:54
 * @description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DispatchServiceTest {
    @Autowired
    private LineTaskController lineTaskController;



    @Test
    public void test() throws InterruptedException {
        for(int i=0;i<5;i++){
            lineTaskController.queryOrders();
        }
    }
}
