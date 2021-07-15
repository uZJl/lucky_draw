package org.example.controller;

import org.example.model.Setting;
import org.example.model.User;
import org.example.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/setting")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping("/query")
    public Object query(HttpSession session){
        //返回的字段是setting对象属性，缺少的属性先构造好
        //需要setting对象，可以通过user_id从数据库查询
        //user_id从session中的user获取
        User user = (User) session.getAttribute("user");
        Setting setting = settingService.query(user.getId());
        setting.setUser(user);
        return setting;
    }

    @GetMapping("/update")
    public Object update(HttpSession session, Integer batchNumber){
        User user = (User) session.getAttribute("user");
        Setting setting = new Setting();
        setting.setId(user.getSettingId());
        setting.setBatchNumber(batchNumber);
        int n = settingService.update(setting);
        return null;
    }
}
