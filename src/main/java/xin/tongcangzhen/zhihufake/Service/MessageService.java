package xin.tongcangzhen.zhihufake.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import xin.tongcangzhen.zhihufake.DAO.MessageDAO;
import xin.tongcangzhen.zhihufake.Model.MessageEntity;

import java.util.List;


@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(MessageEntity message) {
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.save(message) != null ? message.getId() : 0;
    }

    public List<MessageEntity> getConversationDetail(String conversationId, int offset, int limit) {
        return  messageDAO.findAllByConversationId(conversationId,new PageRequest(offset, limit, Sort.Direction.DESC, "id"));
    }

    public List<MessageEntity> getConversationList(int userId, int offset, int limit) {
        return  messageDAO.findlist(userId, new PageRequest(offset, limit, Sort.Direction.DESC, "created_date"));
    }

    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDAO.countAllByToIdAndConversationIdAndHasRead(userId, conversationId,0);
    }

    public int getConversationCount(String conversationId) {
        return messageDAO.countAllByConversationId(conversationId);
    }
}

