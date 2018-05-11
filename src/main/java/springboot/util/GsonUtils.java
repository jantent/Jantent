package springboot.util;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

/**
 * Gson转换工具
 *
 * @author tangj
 * @date 2018/1/23 22:48
 */
public class GsonUtils {
    private static final Gson gson = new Gson();

    public static String toJsonString(Object object) {

        return object == null ? null : gson.toJson(object);
    }

    /**
     * 将json字符串转成对象
     *
     * @param jstr
     * @param clazz
     * @return
     */
    public static Object jsonToObject(String jstr, Class clazz) {
        Object object = null;

        if (StringUtils.isNotBlank(jstr)) {
            object = gson.fromJson(jstr, clazz);
        }
        return object;
    }
}
