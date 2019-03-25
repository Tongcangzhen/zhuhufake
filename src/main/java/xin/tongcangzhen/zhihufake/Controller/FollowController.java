package xin.tongcangzhen.zhihufake.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xin.tongcangzhen.zhihufake.Model.*;
import xin.tongcangzhen.zhihufake.Service.CommentService;
import xin.tongcangzhen.zhihufake.Service.FollowService;
import xin.tongcangzhen.zhihufake.Service.QuestionService;
import xin.tongcangzhen.zhihufake.Service.UserService;
import xin.tongcangzhen.zhihufake.Util.HostHolder;
import xin.tongcangzhen.zhihufake.Util.ZhiHuUtil;
import xin.tongcangzhen.zhihufake.async.EventModel;
import xin.tongcangzhen.zhihufake.async.EventProducer;
import xin.tongcangzhen.zhihufake.async.EventType;

import java.util.*;

public class FollowController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String follow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return ZhiHuUtil.getJSONString(999);
        }
        boolean res = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        return ZhiHuUtil.getJSONString(res ? 0 : 1, String.valueOf(followService.getFolloweeCount( hostHolder.getUser().getId(),EntityType.ENTITY_USER)));

    }

    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return ZhiHuUtil.getJSONString(999);
        }
        boolean res = followService.unFollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        return ZhiHuUtil.getJSONString(res ? 0 : 1, String.valueOf(followService.getFolloweeCount( hostHolder.getUser().getId(),EntityType.ENTITY_USER)));

    }

    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQiestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return ZhiHuUtil.getJSONString(999);
        }
        QuestionEntity questionEntity = questionService.getQuestionById(questionId);
        if (questionEntity == null) {
            return ZhiHuUtil.getJSONString(1, "不存在的问题");
        }
        boolean res = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(questionEntity.getUserId()));

        Map<String, Object> mapinfo = new HashMap<>();
        mapinfo.put("headUrl", hostHolder.getUser().getHeadUrl());
        mapinfo.put("name", hostHolder.getUser().getName());
        mapinfo.put("id", hostHolder.getUser().getId());
        mapinfo.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return ZhiHuUtil.getJSONString(res ? 0 : 1,mapinfo );

    }

    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return ZhiHuUtil.getJSONString(999);
        }
        QuestionEntity questionEntity = questionService.getQuestionById(questionId);
        if (questionEntity == null) {
            return ZhiHuUtil.getJSONString(1, "不存在的问题");
        }

        boolean res = followService.unFollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(questionEntity.getUserId()));

        Map<String, Object> mapinfo = new HashMap<>();
        mapinfo.put("headUrl", hostHolder.getUser().getHeadUrl());
        mapinfo.put("name", hostHolder.getUser().getName());
        mapinfo.put("id", hostHolder.getUser().getId());
        mapinfo.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return ZhiHuUtil.getJSONString(res ? 0 : 1,mapinfo );

    }


    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);

        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            UserEntity user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }

}
