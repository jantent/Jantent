package springboot.controller.mytheme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.controller.BaseController;
import springboot.exception.TipException;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author tangj
 * @date 2018/4/11 21:07
 */
@Controller
@RequestMapping("/theme/index")
@Transactional(rollbackFor = TipException.class)
public class myThemeController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(myThemeController.class);

    @GetMapping("")
    public String index(HttpServletRequest request){
        return "jantent/lwindex";
    }
}
