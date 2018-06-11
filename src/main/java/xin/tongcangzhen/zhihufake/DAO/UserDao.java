package xin.tongcangzhen.zhihufake.DAO;

import org.springframework.data.repository.CrudRepository;
import xin.tongcangzhen.zhihufake.Model.UserEntity;

public interface UserDao extends CrudRepository<UserEntity, Long> {

    UserEntity findByName(String name);

    UserEntity findById(int id);
}
