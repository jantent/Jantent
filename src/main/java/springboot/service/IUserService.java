package springboot.service;

import springboot.modal.vo.UserVo;

/**
 * @author tangj
 * @date 2018/1/21 14:22
 */
public interface IUserService {
    /**
     * 保存用户数据
     *
     * @param userVo 用户数据
     * @return 主键
     */

    Integer insertUser(UserVo userVo);

    /**
     * 通过uid查找对象
     * @param uid
     * @return
     */
    UserVo queryUserById(Integer uid);

    /**
     * 用戶登录
     * @param username
     * @param password
     * @return
     */
    UserVo login(String username, String password);

    /**
     * 根据主键更新user对象
     * @param userVo
     * @return
     */
    void updateByUid(UserVo userVo);
}
