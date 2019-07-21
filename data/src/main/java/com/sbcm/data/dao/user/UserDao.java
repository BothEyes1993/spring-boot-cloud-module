package com.sbcm.data.dao.user;

import com.sbcm.common.vo.user.UserVO;
import org.apache.ibatis.annotations.Param;

public interface UserDao {

/*

@Param是地处Dao层，是为了传递多个参数，
在方法参数的前面写上@Param("参数名"),表示给参数命名,名称就是括号中的内容
然后sql语句里面就可以 #{user} 得到参数值了

*/
    public UserVO getUser(@Param("user") UserVO user);


}
