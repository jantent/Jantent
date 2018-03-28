package springboot.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import springboot.modal.vo.UserVo;
import springboot.modal.vo.UserVoExample;

import java.util.List;

/**
 * @author tangj
 * @date 2018/1/21 14:59
 */
@Component
public interface UserVoMapper {
    long countByExample(UserVoExample example);

    int deleteByExample(UserVoExample example);

    int deleteByPrimaryKey(Integer uid);

    int insert(UserVo record);

    int insertSelective(UserVo record);

    List<UserVo> selectByExample(UserVoExample example);

    UserVo selectByPrimaryKey(Integer uid);

    int updateByExampleSelective(@Param("record") UserVo record, @Param("example") UserVoExample example);

    int updateByExample(@Param("record") UserVo record, @Param("example") UserVoExample example);

    int updateByPrimaryKeySelective(UserVo record);

    int updateByPrimaryKey(UserVo record);

}
