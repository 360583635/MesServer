package com.job.authenticationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.authenticationService.mapper.MenusRolesMapper;
import com.job.authenticationService.mapper.RolesMapper;
import com.job.authenticationService.service.MenusRolesService;
import com.job.authenticationService.service.RolesService;
import com.job.common.pojo.MenusRoles;
import com.job.common.pojo.Roles;
import org.springframework.stereotype.Service;

@Service
public class MenusRolesImpl extends ServiceImpl<MenusRolesMapper, MenusRoles> implements MenusRolesService {
}
