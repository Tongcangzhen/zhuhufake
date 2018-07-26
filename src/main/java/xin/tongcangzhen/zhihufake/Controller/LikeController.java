package xin.tongcangzhen.zhihufake.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.tongcangzhen.zhihufake.Model.CommentEntity;
import xin.tongcangzhen.zhihufake.Model.EntityType;
import xin.tongcangzhen.zhihufake.Service.CommentService;
import xin.tongcangzhen.zhihufake.Service.LikeService;
import xin.tongcangzhen.zhihufake.Util.HostHolder;
import xin.tongcangzhen.zhihufake.Util.ZhiHuUtil;
import xin.tongcangzhen.zhihufake.async.EventModel;
import xin.tongcangzhen.zhihufake.async.EventProducer;
import xin.tongcangzhen.zhihufake.async.EventType;

@Controller
public class LikeController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return ZhiHuUtil.getJSONString(999);
        }

        CommentEntity commentEntity=commentService.getCommentById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId).setEntityOwnerId(commentEntity.getUserId())
                .setEntityType(EntityType.ENTITY_COMMENT).setExt("questionId",String.valueOf(commentEntity.getEntityId())));
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ZhiHuUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return ZhiHuUtil.getJSONString(999);
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ZhiHuUtil.getJSONString(0, String.valueOf(likeCount));
    }

}
