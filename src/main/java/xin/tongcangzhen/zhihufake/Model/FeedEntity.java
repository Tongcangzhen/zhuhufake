package xin.tongcangzhen.zhihufake.Model;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "feed", schema = "huzhi", catalog = "")
public class FeedEntity {
    private int id;
    private Integer type;
    private Integer userId;
    private Date createDate;
    private String data;
    public JSONObject dataJSON = null;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "data")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedEntity that = (FeedEntity) o;
        return id == that.id &&
                Objects.equals(type, that.type) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, type, userId, createDate, data);
    }

//    public String get(String key) {
//        return dataJSON == null ? null : dataJSON.getString(key);
//    }
}
