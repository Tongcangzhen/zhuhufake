package xin.tongcangzhen.zhihufake.DAO;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import xin.tongcangzhen.zhihufake.Model.QuestionEntity;

import java.util.List;

public interface QuestionDao extends CrudRepository<QuestionEntity, Long> {

    QuestionEntity findById(int id);

    List<QuestionEntity> findAllByUserId(int userId, Pageable pageable);

    List<QuestionEntity> findAllBy(Pageable pageable);
}
