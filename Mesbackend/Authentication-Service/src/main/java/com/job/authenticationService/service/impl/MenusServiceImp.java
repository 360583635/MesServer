package com.job.authenticationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.authenticationService.mapper.MenusMapper;
import com.job.authenticationService.service.MenusService;
import com.job.common.pojo.Menus;
import org.springframework.stereotype.Service;

@Service
public class MenusServiceImp extends ServiceImpl<MenusMapper, Menus> implements MenusService {

}
