package xin.tongcangzhen.zhihufake;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xin.tongcangzhen.zhihufake.DAO.CommentDao;
import xin.tongcangzhen.zhihufake.DAO.QuestionDao;
import xin.tongcangzhen.zhihufake.Model.CommentEntity;
import xin.tongcangzhen.zhihufake.Model.QuestionEntity;
import xin.tongcangzhen.zhihufake.Service.QuestionService;
import xin.tongcangzhen.zhihufake.Service.SensitiveService;

import java.sql.Timestamp;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZhihufakeApplicationTests {

	@Autowired
	QuestionDao questionDao;

	@Autowired
	QuestionService questionService;

	@Autowired
	SensitiveService sensitiveService;

	@Autowired
	CommentDao commentDao;

	@Test
	public void contextLoads() {
//		System.out.print(questionDao.findLatestQuestion(111, 0, 1));
//		List<QuestionEntity> list;
//		list = questionService.getLatestQuestion(111, 2, 5);
//		for (QuestionEntity a : list) {
//			System.out.println(a.getId() + "   " + a.getContent());
//		}
		for (int i = 0; i < 2; i++) {
			CommentEntity commentEntity = new CommentEntity();
			commentEntity.setCreatedDate( new Date());
			commentEntity.setEntityId(i);
			commentEntity.setEntityType(i);
			commentEntity.setUserId(i);
			commentDao.save(commentEntity);
		}





	}

}
