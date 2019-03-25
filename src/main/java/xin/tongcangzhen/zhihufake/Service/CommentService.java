package xin.tongcangzhen.zhihufake.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import xin.tongcangzhen.zhihufake.DAO.CommentDao;
import xin.tongcangzhen.zhihufake.Model.CommentEntity;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    @Autowired
    SensitiveService sensitiveService;

    public List<CommentEntity> getCommentsByEntity(int entityId, int entityType) {
        return commentDao.findAllByEntityIdAndEntityType(entityId, entityType);
    }

    public int addComment(CommentEntity commentEntity) {
        commentEntity.setContent(sensitiveService.filter(commentEntity.getContent()));
        commentEntity.setContent(HtmlUtils.htmlEscape(commentEntity.getContent()));
        return commentDao.save(commentEntity) != null ? commentEntity.getId() : 0;
    }

    public int getCommentCount(int entityId, int entityType) {
        return  commentDao.countByEntityIdAndEntityType(entityId, entityType);
    }

    public CommentEntity getCommentById(int id) {
       return commentDao.findAllById(id);
    }

    public int getUserCommentCount(int userId) {
        return commentDao.countAllByUserId(userId);
    }


}
