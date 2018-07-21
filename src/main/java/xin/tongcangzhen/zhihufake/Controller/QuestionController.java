package xin.tongcangzhen.zhihufake.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.tongcangzhen.zhihufake.Model.QuestionEntity;
import xin.tongcangzhen.zhihufake.Service.QuestionService;
import xin.tongcangzhen.zhihufake.Util.HostHolder;
import xin.tongcangzhen.zhihufake.Util.ZhiHuUtil;

import java.util.Date;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setTitle(title);
            questionEntity.setContent(content);
            questionEntity.setCreatedDate(new Date());
            questionEntity.setCommentCount(0);
            if (hostHolder.getUser() == null) {
                throw new Exception("用户未登录");
            } else {
                questionEntity.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(questionEntity) > 0) {
                return ZhiHuUtil.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("提问失败:" + e.getMessage());
        }
        return ZhiHuUtil.getJSONString(1, "失败");
    }

}
