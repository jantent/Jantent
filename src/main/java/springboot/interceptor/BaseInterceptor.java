package springboot.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import springboot.constant.WebConst;
import springboot.dto.Types;
import springboot.modal.vo.UserVo;
import springboot.service.IUserService;
import springboot.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tangj
 * @date 2018/1/21 22:27
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Resource
    private IUserService userService;

    private MapCache cache = MapCache.single();

    @Autowired
    private Commons commons;

    @Autowired
    private AdminCommons adminCommons;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String ip = IpUtil.getIpAddrByRequest(request);
        logger.info("UserAgent: {}", request.getHeader(USER_AGENT));
        logger.info("用户访问地址: {}, 来路地址: {}", uri, ip);

        //请求拦截处理
        UserVo user = MyUtils.getLoginUser(request);
        if (null == user) {
            Integer uid = MyUtils.getCooKieUid(request);
            request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
        }
        // 处理uri
        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }

        // 设置get请求的token
        if (request.getMethod().equals("GET")) {
            String csrf_token = UUID.UU64();
            // 默认存储30分钟
            cache.hset(Types.CSRF_TOKEN.getType(), csrf_token, uri, 30 * 60);
            request.setAttribute("_csrf_token", csrf_token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String ip = IpUtil.getIpAddrByRequest(request);
        // 禁止该ip访问
        if (WebConst.BLOCK_IPS.contains(ip)) {
            // 强制跳转
            modelAndView.setViewName("comm/ipban");
        }

        request.setAttribute("commons",commons);
        request.setAttribute("adminCommons",adminCommons);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
