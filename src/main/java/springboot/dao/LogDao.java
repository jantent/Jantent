package springboot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import springboot.modal.vo.LogVo;

import java.util.List;

@Mapper
public interface LogDao {
    @Select("select * from t_logs")
    List<LogVo> listLogVo();
}
