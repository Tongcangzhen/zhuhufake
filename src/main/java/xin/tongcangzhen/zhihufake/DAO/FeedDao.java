package xin.tongcangzhen.zhihufake.DAO;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import xin.tongcangzhen.zhihufake.Model.CommentEntity;
import xin.tongcangzhen.zhihufake.Model.FeedEntity;

import java.util.List;


public interface FeedDao extends CrudRepository<FeedEntity, Long> {


    FeedEntity findAllById(int id);


//    @Query(value = "SELECT * FROM feed WHERE id< :maxId AND user_id IN (:userIds) ORDER BY id DESC",
//            nativeQuery = true)
//    List<FeedEntity> findUserFeed(@Param("maxId") int maxId,
//                                  @Param("") List<Integer> userIds,
//                                  Pageable pageable);

    List<FeedEntity> findAllByIdLessThanAndUserIdIn(int id, List<Integer> userIds,Pageable pageable);
}
