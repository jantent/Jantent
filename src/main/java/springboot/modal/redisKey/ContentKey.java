package springboot.modal.redisKey;

/**
 * @author tangj
 * @date 2018/5/13 17:45
 */
public class ContentKey {
    // 表名
    public static final String TABLE_NAME = "t_contents";

    // 主键名
    public static final String MAJOR_KEY = "cid";

    // 默认主键值
    public static final String DEFAULT_VALUE = "all";

    // 生存周期
    public static final int LIVE_TIME = 6;
}
