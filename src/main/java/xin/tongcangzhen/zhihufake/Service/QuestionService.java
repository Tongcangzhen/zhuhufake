package xin.tongcangzhen.zhihufake.Service;

import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import xin.tongcangzhen.zhihufake.DAO.QuestionDao;
import xin.tongcangzhen.zhihufake.Model.QuestionEntity;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    public List<QuestionEntity> getLatestQuestion(int userId, int offert, int limit) {
        if (userId != 0) {
            return questionDao.findAllByUserId(userId, new PageRequest(offert, limit, Sort.Direction.ASC, "id"));
        } else {
            return questionDao.findAllBy(new PageRequest(offert, limit, Sort.Direction.ASC, "id"));
        }

    }
}
