package springboot.controller;

import springboot.modal.vo.UserVo;
import springboot.util.MapCache;
import springboot.util.MyUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tangj
 * @date 2018/1/21 11:25
 */
public abstract class BaseController {
    public static String THEME = "themes/jantent";

    protected MapCache cache = MapCache.single();

    /**
     * 主页的页面主题
     *
     * @param viewName
     * @return
     */
    public String render(String viewName) {
        return THEME + "/" + viewName;
    }

    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    public BaseController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }

    public UserVo user(HttpServletRequest request){
        return MyUtils.getLoginUser(request);
    }

    public Integer getUid(HttpServletRequest request){
        return this.user(request).getUid();
    }

    public String render_404() {
        return "comm/error_404";
    }
}
