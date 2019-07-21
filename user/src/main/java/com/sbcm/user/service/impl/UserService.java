package com.sbcm.user.service.impl;


import com.sbcm.common.vo.user.UserVO;
import com.sbcm.data.dao.user.UserDao;
import com.sbcm.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserService implements IUserService {
   // private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Override
    public UserVO getUser()  {
        UserVO user1 = new UserVO();
        user1.setUserId(14);
        user1.setUserName("User1111");
        return user1;
//        UserVO user2 = userDao.getUser(user1);
//        return user2;
    }


}
