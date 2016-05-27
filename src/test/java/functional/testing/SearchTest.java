package functional.testing;

import java.util.List;

import org.junit.Test;

import com.common.search.model.QuestAnswer;
import com.common.search.model.Results;
import com.common.util.SearchUtil;

/**
 * 查询类型
 * @author liwenxian
 *
 */
public class SearchTest {

	/**
	 * 查询    搜索
	 */
	@Test
	public void query() {
		try {
			Results<QuestAnswer> res = SearchUtil.searchQaInfo(new String[] {}, new String[] {}, 0, 10, new String[] {},
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
	 * 查询    搜索
	 */
	@Test
	public void queryByContexts() {
		try {
			Results<QuestAnswer> res = SearchUtil.searchQaInfo(new String[] {"contexts","contexts"}, new String[] {"宝宝","蔬菜"}, 0, 10, new String[] {},
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
	 * 搜索   智能提示
	 */
	@Test
	public void suggest() {
		try {
			Results<QuestAnswer> res = SearchUtil.suggestQaSearch("宝宝");

			System.out.println(res.getCount());

			if (res != null) {
				List<QuestAnswer> beans = res.getDocs();
				if (beans != null && beans.size() > 0) {
					System.out.println("query result size:" + res.getCount());
					for (int i = 0; i < beans.size(); i++) {
						System.out.println(beans.get(i).getTitle());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
