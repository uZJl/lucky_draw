package org.example.service;

import org.example.exception.AppException;
import org.example.mapper.SettingMapper;
import org.example.mapper.UserMapper;
import org.example.model.Setting;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SettingMapper settingMapper;

    @Value("${user.head.local-path}")
    private String headLocalPath;

    @Value("${user.head.remote-path}")
    private String headRemotePath;

    public User query(String username) {
        return userMapper.query(username);
    }

    @Transactional
    public void register(User user, MultipartFile headFile) {
        try {
            //如果有上传用户头像，需要保存在服务端本地，并且部署好，能通过url访问
            //保存时，需要考虑localPath后的相对路径是在并发情况下唯一
            if(headFile != null){
                String uuid = UUID.randomUUID().toString();
                //使用上传时的图片名称保存到本地
                String uri = "/" + uuid + "/" + headFile.getOriginalFilename();
                //本地就是保存在headLocalPath+uri，http访问就是headRemotePath+uri
//                String localPath = headRemotePath + uri;
                String localPath = headLocalPath + uri;
                //不能直接保存，因为父文件夹可能还没创建
                File head = new File(localPath);
                File parent = head.getParentFile();
                if(!parent.exists()) {
                    parent.mkdirs();
                }
                headFile.transferTo(head);
                //用户头像的url，设置到user对象的head属性
                user.setHead(headRemotePath+uri);
            }

            //用户注册，插入用户数据到数据库
            //插入后，自增主键会绑定到User对象
            userMapper.insertSelective(user);

            //用户注册并登陆后，跳转到设置页面，页面初始化就查询设置表数据绑定的奖项，人员信息
            //所以注册时，再插入一条和用户绑定的设置数据
            Setting setting = new Setting();
            setting.setUserId(user.getId());
            setting.setBatchNumber(8);
            settingMapper.insertSelective(setting);
        } catch (IOException e) {
            throw new AppException("USR003", "保存用户头像出错", e);
        }
    }
}
