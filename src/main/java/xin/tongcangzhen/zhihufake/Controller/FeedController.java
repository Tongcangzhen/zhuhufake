package xin.tongcangzhen.zhihufake.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xin.tongcangzhen.zhihufake.Model.EntityType;
import xin.tongcangzhen.zhihufake.Model.FeedEntity;
import xin.tongcangzhen.zhihufake.Service.FeedService;
import xin.tongcangzhen.zhihufake.Service.FollowService;
import xin.tongcangzhen.zhihufake.Util.HostHolder;
import xin.tongcangzhen.zhihufake.Util.JedisAdapter;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisAdapter jedisAdapter;


    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            // 关注的人
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<FeedEntity> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
