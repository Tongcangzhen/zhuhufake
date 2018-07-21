package xin.tongcangzhen.zhihufake.DAO;

import org.springframework.data.repository.CrudRepository;
import xin.tongcangzhen.zhihufake.Model.CommentEntity;

import java.util.List;

public interface CommentDao extends CrudRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByEntityIdaAndEntityType(int entity_id, int entity_type);

    int countByEntityIdAndEntityType(int entity_id, int entity_type);
}
