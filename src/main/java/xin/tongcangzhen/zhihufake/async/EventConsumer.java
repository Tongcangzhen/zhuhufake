package xin.tongcangzhen.zhihufake.async;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import xin.tongcangzhen.zhihufake.Util.JedisAdapter;
import xin.tongcangzhen.zhihufake.Util.RedisKeyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class EventConsumer implements InitializingBean , ApplicationContextAware {

    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();
    private ApplicationContext applicationContext;



    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        System.out.println(beans);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventType();

                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();

                    List<String> events = jedisAdapter.brpop(0, key);  //从redis队列中获取事件，如果队列为空，则阻塞
                    System.out.println("get key :  " + key);
                    System.out.println(events);
                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }
                        EventModel eventModel = JSONObject.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getEventType())) {
                            System.out.println(config);
                            logger.error("不能识别事件");
                            continue;
                        }
                        for (EventHandler handler : config.get(eventModel.getEventType())) {  //分配给handler运行
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
