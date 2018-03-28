package springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.config.AuthorSettings;

import javax.annotation.Resource;

@Controller
public class ConfigController {
    @Resource
    private AuthorSettings authorSettings;

    @GetMapping("/config")
    @ResponseBody
    public String idnex(){
        return "author name is"+authorSettings.getName()+"age is: "+authorSettings.getAge();
    }
}
