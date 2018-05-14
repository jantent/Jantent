package springboot.controller.admin;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springboot.constant.WebConst;
import springboot.controller.AbstractController;
import springboot.controller.helper.ExceptionHelper;
import springboot.dto.LogActions;
import springboot.exception.TipException;
import springboot.modal.bo.RestResponseBo;
import springboot.modal.bo.StaticticsBo;
import springboot.modal.vo.CommentVo;
import springboot.modal.vo.ContentVo;
import springboot.modal.vo.LogVo;
import springboot.modal.vo.UserVo;
import springboot.service.ILogService;
import springboot.service.ISiteService;
import springboot.service.IUserService;
import springboot.util.GsonUtils;
import springboot.util.MyUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 管理页面首页管理
 *
 * @author tangj
 * @date 2018/1/23 11:39
 */
@Controller("adminIndexController")
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class IndexController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private ILogService logService;

    @Resource
    private IUserService userService;

    @Resource
    private ISiteService siteService;


    /**
     * 管理中心起始页
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request) {
        List<ContentVo> contentVos = siteService.recentContents(5);
        List<CommentVo> commentVos = siteService.recentComments(5);
        StaticticsBo staticticsBo = siteService.getStatistics();
        //取最新的15条日志
        List<LogVo> logVos = logService.getLogs(1, 5);
        request.setAttribute("comments", commentVos);
        request.setAttribute("articles", contentVos);
        request.setAttribute("statistics", staticticsBo);
        request.setAttribute("logs", logVos);
        return "admin/index";
    }

    @GetMapping(value = "profile")
    public String profile() {
        return "admin/profile";
    }

    @GetMapping(value = "logout")
    public String logout() {
        return "admin/login";
    }

    @PostMapping(value = "/profile")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {
        UserVo users = this.user(request);
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            UserVo temp = new UserVo();
            temp.setUid(users.getUid());
            temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateByUid(temp);
            logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            UserVo original = (UserVo) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY, original);
        }
        return RestResponseBo.ok();
    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param request
     * @param session
     * @return
     */
    @PostMapping(value = "/password")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo updatePwd(@RequestParam String oldPassword, @RequestParam String newPassword, HttpServletRequest request, HttpSession session) {
        UserVo users = this.user(request);
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return RestResponseBo.fail("请确认信息输入完整");
        }
        if (!users.getPassword().equals(MyUtils.MD5encode(users.getUsername() + oldPassword))) {
            return RestResponseBo.fail("原始密码不正确");
        }
        if (newPassword.length() < 6 || newPassword.length() > 14) {
            return RestResponseBo.fail("请输入6-14位密码");
        }
        try {
            UserVo temp = new UserVo();
            temp.setUid(users.getUid());
            String pwd = MyUtils.MD5encode(users.getUsername() + newPassword);
            temp.setPassword(pwd);
            userService.updateByUid(temp);
            logService.insertLog(LogActions.UP_PWD.getAction(), null, request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            UserVo originalUser = (UserVo) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            originalUser.setPassword(pwd);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY, originalUser);
            return RestResponseBo.ok();
        } catch (Exception e) {
            String msg = "密码修改失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
    }
}
