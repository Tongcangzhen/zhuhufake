package xin.tongcangzhen.zhihufake.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import xin.tongcangzhen.zhihufake.DAO.CommentDao;
import xin.tongcangzhen.zhihufake.Model.CommentEntity;
import xin.tongcangzhen.zhihufake.Model.EntityType;
import xin.tongcangzhen.zhihufake.Service.CommentService;
import xin.tongcangzhen.zhihufake.Service.QuestionService;
import xin.tongcangzhen.zhihufake.Util.HostHolder;

import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int qusetionId,
                             @RequestParam("content") String content) {
        try {
            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setEntityType(EntityType.ENTITY_QUESTION);
            commentEntity.setEntityId(qusetionId);
            commentEntity.setCreatedDate(new Date());
            if (hostHolder.getUser() == null) {
                return "redirect:/";
            } else {
                commentEntity.setUserId(hostHolder.getUser().getId());
            }
            commentEntity.setStatus(0);
            commentEntity.setContent(content);
            commentService.addComment(commentEntity);

            //修改评论数量
            // TODO: 2019/3/21 放入消息队列异步处理；
            int count = commentService.getCommentCount(commentEntity.getEntityId(), commentEntity.getEntityType());
            questionService.updateComment(commentEntity.getEntityId(), count);
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
        }
        return "redirect:/question/" + qusetionId;
    }
}
