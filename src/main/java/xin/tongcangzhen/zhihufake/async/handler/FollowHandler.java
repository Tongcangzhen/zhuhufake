package xin.tongcangzhen.zhihufake.async.handler;

import org.springframework.beans.factory.annotation.Autowired;
import xin.tongcangzhen.zhihufake.Model.EntityType;
import xin.tongcangzhen.zhihufake.Model.MessageEntity;
import xin.tongcangzhen.zhihufake.Model.UserEntity;
import xin.tongcangzhen.zhihufake.Service.MessageService;
import xin.tongcangzhen.zhihufake.Service.UserService;
import xin.tongcangzhen.zhihufake.Util.ZhiHuUtil;
import xin.tongcangzhen.zhihufake.async.EventHandler;
import xin.tongcangzhen.zhihufake.async.EventModel;
import xin.tongcangzhen.zhihufake.async.EventType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FollowHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        MessageEntity message = new MessageEntity();
        message.setFormId(ZhiHuUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        UserEntity user = userService.getUser(model.getActorId());

        if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName()
                    + "关注了你的问题,http://tongcangzhen.xin/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityType.ENTITY_USER) {
            message.setContent("用户" + user.getName()
                    + "关注了你,http://tongcangzhen.xin/user/" + model.getActorId());
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
