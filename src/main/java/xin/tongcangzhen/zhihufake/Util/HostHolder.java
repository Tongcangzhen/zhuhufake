package xin.tongcangzhen.zhihufake.Util;

import org.springframework.stereotype.Component;
import xin.tongcangzhen.zhihufake.Model.UserEntity;

@Component
public class HostHolder {
    private static ThreadLocal<UserEntity> users = new ThreadLocal<UserEntity>();

    public UserEntity getUser() {
        return users.get();
    }

    public  void setUsers(UserEntity user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
