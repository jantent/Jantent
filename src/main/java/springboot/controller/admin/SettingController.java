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
import springboot.dto.Types;
import springboot.exception.TipException;
import springboot.modal.bo.BackResponseBo;
import springboot.modal.bo.RestResponseBo;
import springboot.modal.vo.OptionVo;
import springboot.service.ILogService;
import springboot.service.IOptionService;
import springboot.service.ISiteService;
import springboot.util.GsonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tangj
 * @date 2018/2/1 21:43
 */
@Controller
@RequestMapping("/admin/setting")
public class SettingController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(SettingController.class);

    @Resource
    private IOptionService optionService;

    @Resource
    private ILogService logService;

    @Resource
    private ISiteService siteService;

    /**
     * 起始页
     *
     * @param request
     * @return
     */
    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        List<OptionVo> volist = optionService.getOptions();
        Map<String, String> options = new HashMap<>();
        volist.forEach((option) -> {
            options.put(option.getName(), option.getValue());
        });
        if (options.get("site_record") == null) {
            options.put("site_record", "");
        }
        request.setAttribute("options", options);
        return "admin/setting";
    }

    @PostMapping(value = "")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo saveSetting(@RequestParam(required = false) String site_theme, HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> querys = new HashMap<>();
            parameterMap.forEach((key, value) -> {
                querys.put(key, join(value));
            });

            optionService.saveOptions(querys);
            WebConst.initConfig = querys;

            if (StringUtils.isNotBlank(site_theme)) {
                AbstractController.THEME = "themes/" + site_theme;
            }
            logService.insertLog(LogActions.SYS_SETTING.getAction(), GsonUtils.toJsonString(querys), request.getRemoteAddr(), this.getUid(request));
            return RestResponseBo.ok();
        } catch (Exception e) {
            String msg = "保存设置失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
    }

    @PostMapping(value = "backup")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo backup(@RequestParam String bk_type, @RequestParam String bk_path, HttpServletRequest request) {
        if (StringUtils.isBlank(bk_type)) {
            return RestResponseBo.fail("请确认信息输入完整");
        }
        try {
            BackResponseBo backResponseBo = siteService.backup(bk_type, bk_path, "yyyyMMddHHmm");
            logService.insertLog(LogActions.SYS_BACKUP.getAction(), null, request.getRemoteAddr(), this.getUid(request));
            return RestResponseBo.ok(backResponseBo);

        } catch (Exception e) {
            String msg = "备份失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
    }

    @PostMapping(value = "advanced")
    @ResponseBody
    public RestResponseBo doAdvanced(@RequestParam String cache_key, @RequestParam String block_ips) {
        // 清除缓存
        if (StringUtils.isNotBlank(cache_key)) {
            if ("*".equals(cache_key)) {
                cache.clean();
            } else {
                cache.del(cache_key);
            }
        }
        // 要过过滤的黑名单列表
        if (StringUtils.isNotBlank(block_ips)) {
            String url = Types.BLOCK_IPS.getType();
            optionService.insertOption(Types.BLOCK_IPS.getType(), block_ips);
            WebConst.BLOCK_IPS.addAll(Arrays.asList(block_ips.split(",")));
        } else {
            optionService.insertOption(Types.BLOCK_IPS.getType(), "");
            WebConst.BLOCK_IPS.clear();
        }
        return RestResponseBo.ok();
    }

    /**
     * 数组转字符串
     *
     * @param arr
     * @return
     */
    private String join(String[] arr) {
        StringBuilder ret = new StringBuilder();
        String[] var3 = arr;
        int var4 = arr.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String item = var3[var5];
            ret.append(',').append(item);
        }
        return ret.length() > 0 ? ret.substring(1) : ret.toString();
    }
}
