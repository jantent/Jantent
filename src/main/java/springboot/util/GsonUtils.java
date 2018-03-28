package springboot.util;

import com.google.gson.Gson;

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
}
