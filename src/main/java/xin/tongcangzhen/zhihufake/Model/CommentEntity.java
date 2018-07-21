package xin.tongcangzhen.zhihufake.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "comment", schema = "huzhi", catalog = "")
public class CommentEntity {
    private int id;
    private Integer userId;
    private Date createdDate;
    private Integer entityId;
    private Integer entityType;
    private Integer status;
    private String content;

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
    @Column(name = "entity_id", nullable = true)
    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    @Basic
    @Column(name = "entity_type", nullable = true)
    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return id == that.id &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(entityId, that.entityId) &&
                Objects.equals(entityType, that.entityType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, createdDate, entityId, entityType);
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "content", nullable = true, length = 5000)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
