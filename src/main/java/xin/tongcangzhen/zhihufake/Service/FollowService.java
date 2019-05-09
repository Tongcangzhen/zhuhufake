package xin.tongcangzhen.zhihufake.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import xin.tongcangzhen.zhihufake.Util.JedisAdapter;
import xin.tongcangzhen.zhihufake.Util.RedisKeyUtil;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction transaction = jedisAdapter.multiTran(jedis);
        transaction.zadd(followerKey, date.getTime(), String.valueOf(userId));
        transaction.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        List<Object> res = jedisAdapter.execTrans(transaction, jedis);
        return res.size() == 2 && (Long) res.get(0) > 0 && (Long) res.get(1) > 0;
    }

    public boolean unFollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction transaction = jedisAdapter.multiTran(jedis);
        transaction.zrem(followerKey, String.valueOf(userId));
        transaction.zrem(followeeKey, String.valueOf(entityId));
        List<Object> res = jedisAdapter.execTrans(transaction, jedis);
        return res.size() == 2 && (Long) res.get(0) > 0 && (Long) res.get(1) > 0;
    }

    private List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> lid = new ArrayList<>();
        for (String a : idset) {
            lid.add(Integer.parseInt(a));
        }
        return lid;
    }
    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followKey, 0, count));
    }
    public List<Integer> getFollowers(int entityType, int entityId, int offset,int count) {
        String followKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followKey, offset, count));
    }

    public List<Integer> getFollowees(int entityType, int entityId, int count) {
        String followeKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followeKey, 0, count));
    }
    public List<Integer> getFollowees(int entityType, int entityId, int offset,int count) {
        String followeKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followeKey, offset, count));
    }

    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }
}
