package xin.tongcangzhen.zhihufake.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xin.tongcangzhen.zhihufake.DAO.LoginTicketDao;
import xin.tongcangzhen.zhihufake.DAO.UserDao;
import xin.tongcangzhen.zhihufake.Model.LoginTicketEntity;
import xin.tongcangzhen.zhihufake.Model.UserEntity;
import xin.tongcangzhen.zhihufake.Util.ZhiHuUtil;


import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    public UserEntity getUser(int id) {
        return userDao.findById(id);
    }

    public Map<String, String> register(String username, String password, boolean isadmin) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        UserEntity userEntity = userDao.findByName(username);
        if (userEntity != null) {
            map.put("msg", "用户已存在");
            return map;
        }
        userEntity = new UserEntity();
        userEntity.setName(username);
        userEntity.setSalt(UUID.randomUUID().toString().substring(0, 5));
        userEntity.setPassword(ZhiHuUtil.MD5(password+userEntity.getSalt()));
        userEntity.setHeadUrl(String.format("https://github.com/identicons/" + new Random().nextInt(1000)) + ".png");
        userDao.save(userEntity);
        return map;
    }

    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        UserEntity userEntity = userDao.findByName(username);
        if (userEntity == null) {
            map.put("msg", "用户不存在");
            return map;
        }

        if (!ZhiHuUtil.MD5(password + userEntity.getSalt()).equals(userEntity.getPassword())) {
            map.put("msg", "密码错误");
            return map;
        }
        String ticket = addLoginTicket(userEntity.getId());
        map.put("ticket", ticket);
        return map;
    }

    public String addLoginTicket(int userId) {
        LoginTicketEntity loginTicketEntity = new LoginTicketEntity();
        loginTicketEntity.setUserId(userId);
        Date now = new Date();
        now.setTime(3600 * 1000 *1 + now.getTime());
        loginTicketEntity.setExpired(now);
        loginTicketEntity.setStatus(0);
        loginTicketEntity.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDao.save(loginTicketEntity);
        return loginTicketEntity.getTicket();
    }

    public void logout(String ticket) {
        LoginTicketEntity loginTicketEntity = loginTicketDao.findByTicket(ticket);
        loginTicketEntity.setStatus(1);
        loginTicketDao.save(loginTicketEntity);
    }
}
