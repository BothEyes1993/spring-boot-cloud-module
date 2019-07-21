package com.sbcm.task.service.impl;

import com.sbcm.common.vo.user.UserVO;
import com.sbcm.data.dao.user.UserDao;
import com.sbcm.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TaskService implements ITaskService {
   // private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Override
    public UserVO getTask()  {
        UserVO user1 = new UserVO();
        user1.setUserId(30);
        user1.setUserName("Task111");
        return user1;
//        UserVO user2 = userDao.getUser(user1);
//        return user2;
    }


}
