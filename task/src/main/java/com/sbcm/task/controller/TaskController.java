package com.sbcm.task.controller;


import com.sbcm.common.vo.user.UserVO;
import com.sbcm.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/task", produces = { "application/json;charset=UTF-8" })
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @RequestMapping("/sayhello")
    public String Hello() {
        return "Hello Task!";
    }

    @RequestMapping("/getTask")
    public UserVO getUser() {
        return taskService.getTask();
    }

}