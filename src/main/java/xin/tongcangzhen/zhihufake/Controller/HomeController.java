package xin.tongcangzhen.zhihufake.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import xin.tongcangzhen.zhihufake.Model.EntityType;
import xin.tongcangzhen.zhihufake.Model.QuestionEntity;
import xin.tongcangzhen.zhihufake.Model.UserEntity;
import xin.tongcangzhen.zhihufake.Model.ViewObject;
import xin.tongcangzhen.zhihufake.Service.CommentService;
import xin.tongcangzhen.zhihufake.Service.FollowService;
import xin.tongcangzhen.zhihufake.Service.QuestionService;
import xin.tongcangzhen.zhihufake.Service.UserService;
import xin.tongcangzhen.zhihufake.Util.HostHolder;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<QuestionEntity> questionList = questionService.getLatestQuestion(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (QuestionEntity question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));

            vos.add(vo);
//            System.out.print("yes!!!!!" + vo.get("user"));
        }
        return vos;
    }

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "index";
    }


    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        UserEntity userEntity = userService.getUser(userId);
        ViewObject viewObject = new ViewObject();
        viewObject.set("user", userEntity);
        viewObject.set("commentCount", commentService.getUserCommentCount(userId));
        viewObject.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        viewObject.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            viewObject.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            viewObject.set("followed", false);
        }
        model.addAttribute("profileUser", viewObject);
        return "profile";
    }
}
