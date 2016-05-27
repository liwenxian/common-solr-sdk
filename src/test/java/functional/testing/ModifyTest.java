package functional.testing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.common.search.model.QuestAnswer;
import com.common.search.model.Results;
import com.common.util.SearchUtil;

/**
 * 数据变更类
 * 
 * @author liwenxian
 *
 */
public class ModifyTest {

	/**
	 * 新增
	 */
	@Test
	public void addQaIndex() {
		try {
			List<QuestAnswer> docs = new ArrayList<QuestAnswer>();
			QuestAnswer model = new QuestAnswer();
			model.setId("10001");
			model.setCode("XXXXX");
			model.setTitle("李文显测试");
			model.setContent("测试数据家第三方哈市的开发大法好骄傲的三句话发是否发多少建华发发。");
			model.setAnswers(90);
			model.setRates(120);
			docs.add(model);
			SearchUtil.addQaIndex(docs);
			
			Results<QuestAnswer> res = SearchUtil.searchQaInfo(new String[] {"id"}, new String[] {"10001"}, 0, 10, new String[] {},
					new Boolean[] {});

			System.out.println(res.getCount());

			if (res != null) {
				List<QuestAnswer> beans = res.getDocs();
				if (beans != null && beans.size() > 0) {
					System.out.println("query result size:" + res.getCount());
					for (int i = 0; i < beans.size(); i++) {
						System.out.println(beans.get(i).getId());
						System.out.println(beans.get(i).getCode());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除 搜索
	 */
	@Test
	public void removeQaIndex() {
		try {
			SearchUtil.removeQaIndex("10001");
			
			Results<QuestAnswer> res = SearchUtil.searchQaInfo(new String[] {"id"}, new String[] {"10001"}, 0, 100,
					new String[] {}, new Boolean[] {});
			System.out.println(res.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清空 搜索
	 */
	@Test
	public void removeAllQaIndex() {
		try {
			SearchUtil.removeAllQaIndex();
			
			Results<QuestAnswer> res = SearchUtil.searchQaInfo(new String[] {}, new String[] {}, 0, 100,
					new String[] {}, new Boolean[] {});
			System.out.println(res.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重新构建
	 */
	@Test
	public void rebuildQaIndex() {
		try {
			SearchUtil.rebuildQaIndex();
			Thread.sleep(10000);
			
			Results<QuestAnswer> res = SearchUtil.searchQaInfo(new String[] {}, new String[] {}, 0, 100,
					new String[] {}, new Boolean[] {});
			System.out.println(res.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
