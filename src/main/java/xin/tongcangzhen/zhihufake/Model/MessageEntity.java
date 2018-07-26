package xin.tongcangzhen.zhihufake.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "message", schema = "huzhi", catalog = "")
public class MessageEntity {
    private int id;
    private Integer formId;
    private Integer toId;
    private String content;
    private Date createdDate;
    private Integer hasRead;
    private String conversationId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "form_id", nullable = true)
    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    @Basic
    @Column(name = "to_id", nullable = true)
    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    @Basic
    @Column(name = "content", nullable = true, length = 255)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "created_date", nullable = true)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Basic
    @Column(name = "has_read", nullable = true)
    public Integer getHasRead() {
        return hasRead;
    }

    public void setHasRead(Integer hasRead) {
        this.hasRead = hasRead;
    }

    @Basic
    @Column(name = "conversation_id", nullable = true, length = 45)
    public String getConversationId() {
        if (formId < toId) {
            return String.format("%d_%d", formId, toId);
        } else {
            return String.format("%d_%d", toId, formId);
        }

    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity that = (MessageEntity) o;
        return id == that.id &&
                Objects.equals(formId, that.formId) &&
                Objects.equals(toId, that.toId) &&
                Objects.equals(content, that.content) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(hasRead, that.hasRead) &&
                Objects.equals(conversationId, that.conversationId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, formId, toId, content, createdDate, hasRead, conversationId);
    }
}
