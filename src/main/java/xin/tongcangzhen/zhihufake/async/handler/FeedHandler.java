package xin.tongcangzhen.zhihufake.async.handler;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xin.tongcangzhen.zhihufake.Model.EntityType;
import xin.tongcangzhen.zhihufake.Model.FeedEntity;
import xin.tongcangzhen.zhihufake.Model.QuestionEntity;
import xin.tongcangzhen.zhihufake.Model.UserEntity;
import xin.tongcangzhen.zhihufake.Service.FeedService;
import xin.tongcangzhen.zhihufake.Service.FollowService;
import xin.tongcangzhen.zhihufake.Service.QuestionService;
import xin.tongcangzhen.zhihufake.Service.UserService;
import xin.tongcangzhen.zhihufake.Util.JedisAdapter;
import xin.tongcangzhen.zhihufake.async.EventHandler;
import xin.tongcangzhen.zhihufake.async.EventModel;
import xin.tongcangzhen.zhihufake.async.EventType;

import java.sql.Timestamp;
import java.util.*;


@Component
public class FeedHandler implements EventHandler {


    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    QuestionService questionService;

    @Override
    public void doHandle(EventModel model) {
        FeedEntity feedEntity = new FeedEntity();
        feedEntity.setCreateDate(new Date());
        feedEntity.setType(model.getEventType().getValue());
        feedEntity.setUserId(model.getActorId());
        feedEntity.setData(buildFeedData(model));
        if (feedEntity.getData() == null) {
            return;
        }
        feedService.addFeed(feedEntity);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }


    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<String ,String>();
        UserEntity userEntity = userService.getUser(model.getActorId());
        if (userEntity == null) {
            return null;
        }
        map.put("userId", String.valueOf(userEntity.getId()));
        map.put("userHead", userEntity.getHeadUrl());
        map.put("userName", userEntity.getName());

        if (model.getEventType() == EventType.COMMENT ||
                (model.getEventType() == EventType.FOLLOW  && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            QuestionEntity questionEntity = questionService.getQuestionById(model.getEntityId());
            if (questionEntity == null) {
                return null;
            }
            map.put("questionId", String.valueOf(questionEntity.getId()));
            map.put("questionTitle", questionEntity.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }
}
