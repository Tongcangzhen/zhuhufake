package xin.tongcangzhen.zhihufake.DAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import xin.tongcangzhen.zhihufake.Model.MessageEntity;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface MessageDAO extends CrudRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByConversationId(String conversationId, Pageable pageable);

    @Query(value = "SELECT * ,count(id) as id" + " FROM (SELECT * FROM message WHERE form_id=:userId or to_id= :userId order by created_date desc )"
            + "tt group by conversation_id order BY created_date desc ",
            nativeQuery = true)
    List<MessageEntity> findlist(@Param("userId") int userId,
                                 Pageable pageable);



    int countAllByToIdAndConversationIdAndHasRead(int userid, String conversationId, int hasRead);

    int countAllByConversationId(String conversationId);
}
