package xin.tongcangzhen.zhihufake.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xin.tongcangzhen.zhihufake.Model.*;
import xin.tongcangzhen.zhihufake.Service.*;
import xin.tongcangzhen.zhihufake.Util.HostHolder;
import xin.tongcangzhen.zhihufake.Util.ZhiHuUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    @RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        QuestionEntity questionEntity = questionService.getQuestionById(qid);
        model.addAttribute("question", questionEntity);
//        System.out.println(questionEntity.getId()+"    nadoalewa!!!!!!!!!!!!!!!!!!!");
        List<CommentEntity> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>();
        for (CommentEntity comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("comment", comment);
            vo.set("user", userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("comments", vos);

        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            UserEntity u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }


        return "detail";
    }

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
