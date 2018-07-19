package xin.tongcangzhen.zhihufake;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xin.tongcangzhen.zhihufake.DAO.QuestionDao;
import xin.tongcangzhen.zhihufake.Model.QuestionEntity;
import xin.tongcangzhen.zhihufake.Service.QuestionService;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZhihufakeApplicationTests {

	@Autowired
	QuestionDao questionDao;

	@Autowired
	QuestionService questionService;

	@Test
	public void contextLoads() {
//		System.out.print(questionDao.findLatestQuestion(111, 0, 1));
		List<QuestionEntity> list;
		list = questionService.getLatestQuestion(111, 2, 5);
		for (QuestionEntity a : list) {
			System.out.println(a.getId() + "   " + a.getContent());
		}
//		for (int i = 0; i < 10; i++) {
//			QuestionEntity questionEntity = new QuestionEntity();
//			questionEntity.setCreatedDate(new Date());
//			questionEntity.setCommentCount(i + 1);
//			questionEntity.setContent("balabalabaksbjiadbjkasbd" + 1 + i);
//			questionEntity.setTitle("aa" + i);
//			questionEntity.setUserId(111);
//			questionDao.save(questionEntity);
//		}
	}

}
