package com.sbcm.user.controller;


import com.sbcm.adapter.inject.AspectLogs;
import com.sbcm.common.vo.PageVO;
import com.sbcm.common.vo.user.UserVO;
import com.sbcm.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
/*
*  本身spingboot项目是用@RestController注解，返回结果也是json格式，
*  但是结合springcloud的eureka构建微服务之后，无论是消费者还是提供者，均返回的xml格式
*
*   解决方案有两种:1.前台调用接口的时候修改accept信息，改为application/json，2，后台设置produces
*
* */
@RequestMapping(value = "/user", produces = { "application/json;charset=UTF-8" })
public class UserController {

    //spring自己定义的注解，按byType自动注入
    @Autowired
    private IUserService userService;

    @RequestMapping("/sayhello")
    public String Hello() {
        return "Hello World!";
    }

    @RequestMapping("/getUser")
    public UserVO getUser() {
       return userService.getUser();
    }

    //只接受get方式的请求， @getMapping = @requestMapping(method = RequestMethod.GET)。指定请求的method类型
    //@PathVariable获取url里面带占位参数
    @GetMapping("/single/{userId}")

    //自定义注解，记录日志
    @AspectLogs(description = "查询用户,会员ID[(%userId)]")
    public UserVO getUserById(@PathVariable("userId")Integer userId) {
        return new UserVO();
    }

    //只接受post方式的请求，@postMapping = @requestMapping(method = RequestMethod.POST)。
    //@RequestBody接受post请求的参数，接受前端参数是application/json的格式。
    @PostMapping("/add")
    public int addUser(@RequestBody UserVO user) {
        return 1;
    }

    //带分页带请求
    //ulr后面的参数名称和user,page的属性一致的话会自动注入到user和page的实体
    //不管是path(PathVariable)参数还是？后面的查询参数（requestParm）只要key对得上，spring都会匹配装备实体
    @GetMapping("/list/page/{pageSize}/{curPage}")
    public int getUsersByPage(UserVO user, PageVO page) {
        return 1;
    }

}