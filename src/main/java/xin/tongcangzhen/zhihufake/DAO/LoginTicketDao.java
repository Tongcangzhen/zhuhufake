package xin.tongcangzhen.zhihufake.DAO;

import org.springframework.data.repository.CrudRepository;
import xin.tongcangzhen.zhihufake.Model.LoginTicketEntity;



public interface LoginTicketDao extends CrudRepository<LoginTicketEntity, Long> {
    LoginTicketEntity findByTicket(String ticket);
}
