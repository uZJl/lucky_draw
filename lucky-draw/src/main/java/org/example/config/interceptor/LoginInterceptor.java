package org.example.config.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.base.JSONResponse;
import org.example.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {
    //ObjectMapper类实现json的序列化和反序列化
    //JSON解析为Java对象也称为从JSON反序列化Java对象
    //从Java对象生成JSON的过程也被称为序列化Java对象到JSON
    private ObjectMapper objectMapper;

    public LoginInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    //实现HandlerInterceptor接口并重写了preHandle方法完成登陆拦截器并区分前后端
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if(session != null){//获取登录时设置的用户信息
            User user = (User) session.getAttribute("user");
            if(user != null){//登录了，允许访问
                return true;
            }
        }
        //登录失败，不允许访问的业务：区分前后端
        //TODO：前端跳转登录页面，后端返回json
//        new ObjectMapper().writeValueAsString(object);//序列化对象为json字符串
        //请求的服务路径
        String servletPath = request.getServletPath();//   /apiXXX.html
        // 如果请求服务路径包含/api 则说明是后端逻辑，返回json（自定义json响应格式JSONResponse）
        if(servletPath.startsWith("/api/")){//后端逻辑：返回json
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            JSONResponse json = new JSONResponse();
            json.setCode("USR000");
            json.setMessage("用户没有登录，不允许访问");
            // ObjectMapper是Jackson库中主要用于读取和写入Json数据的类，
            // 能够很方便地将Java对象转为Json格式的数据，
            // 用于后端Servlet向AJAX传递Json数据，动态地将数据展示在页面上。
            String s = objectMapper.writeValueAsString(json);//序列化对象为json
            response.setStatus(HttpStatus.UNAUTHORIZED.value());//状态信息
            PrintWriter pw = response.getWriter();
            pw.println(s);
            pw.flush();
        }else{//前端逻辑：跳转到登录页面 /views/index.html
            //相对路径的写法，一定是请求路径作为相对位置的参照点
            //使用绝对路径来重定向，不建议使用相对路径和转发
            String schema = request.getScheme();//http
            String host = request.getServerName();//ip
            int port = request.getServerPort();//port
            String contextPath = request.getContextPath();//application Context path应用上下文路径
            String basePath = schema+"://"+host+":"+port+contextPath; //得到url
            //重定向到登录页面
            response.sendRedirect(basePath+"/index.html");
        }
        return false;
    }
}
