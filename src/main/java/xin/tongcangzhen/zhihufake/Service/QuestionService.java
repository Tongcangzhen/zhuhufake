package xin.tongcangzhen.zhihufake.Service;

import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import xin.tongcangzhen.zhihufake.DAO.QuestionDao;
import xin.tongcangzhen.zhihufake.Model.QuestionEntity;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;
    @Autowired
    SensitiveService sensitiveService;

    public int addQuestion(QuestionEntity questionEntity) {
        questionEntity.setContent(HtmlUtils.htmlEscape(questionEntity.getContent()));
        questionEntity.setTitle(HtmlUtils.htmlEscape(questionEntity.getTitle()));
        questionEntity.setTitle(sensitiveService.filter(questionEntity.getTitle()));
        questionEntity.setContent(sensitiveService.filter(questionEntity.getContent()));

        return questionDao.save(questionEntity) != null ? questionEntity.getId() : 0;
    }

    public List<QuestionEntity> getLatestQuestion(int userId, int offert, int limit) {
        if (userId != 0) {
            return questionDao.findAllByUserId(userId, new PageRequest(offert, limit, Sort.Direction.DESC, "id"));
        } else {
            return questionDao.findAllBy(new PageRequest(offert, limit, Sort.Direction.DESC, "id"));
        }

    }

    public int updateComment(int id, int commentCount) {
        QuestionEntity questionEntity = questionDao.findById(id);
        questionEntity.setCommentCount(commentCount);
        return questionDao.save(questionEntity) != null ? questionEntity.getId() : 0 ;
    }

    public QuestionEntity getQuestionById(int id) {
        return questionDao.findById(id);
    }

//    Object

}
