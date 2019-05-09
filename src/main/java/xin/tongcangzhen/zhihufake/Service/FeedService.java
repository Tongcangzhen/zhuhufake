package xin.tongcangzhen.zhihufake.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import xin.tongcangzhen.zhihufake.DAO.FeedDao;
import xin.tongcangzhen.zhihufake.Model.FeedEntity;

import java.util.List;

@Service
public class FeedService {
    @Autowired
    FeedDao feedDao;

    public List<FeedEntity> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedDao.findAllByIdLessThanAndUserIdIn(maxId, userIds, new PageRequest(0, count,Sort.Direction.DESC,"id"));
    }

    public boolean addFeed(FeedEntity feedEntity) {
        return feedDao.save(feedEntity) != null ;
    }

    public FeedEntity getById(int id) {
        return feedDao.findAllById(id);
    }
}
