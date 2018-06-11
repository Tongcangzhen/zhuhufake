package xin.tongcangzhen.zhihufake.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index() {
        return "hello zhihu";
    }

    @RequestMapping(path = {"/profile/{userid}"})
    @ResponseBody
    public String profile(@PathVariable("userid") int userId) {

        return String.format("hello %d", userId);
    }
}
