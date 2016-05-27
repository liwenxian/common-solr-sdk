package com.common.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.common.search.ISolrSearcher;
import com.common.search.ISolrUpdater;
import com.common.search.impl.SolrSearcherImpl;
import com.common.search.impl.SolrUpdaterImpl;
import com.common.search.model.QuestAnswer;
import com.common.search.model.Results;

public class SearchUtil {

    private static final String confKey = "common.solr.conf";
    
    private SearchUtil() {
    }


    /**查询索引数据
     * @param fields 查询字段名
     * @param values 查询字段取值
     * @param start  设置起始位置 0开始
     * @param count	 所需记录数
     * @param sortFields 排序字段
     * @param isSortAsc  字段排序方式(true:升 false降)
     * @return
     * @throws Exception
     */
    public static Results<QuestAnswer> searchQaInfo(String[] fields,
                                                      String[] values, int start, int count, String[] sortFields,
                                                      Boolean[] isSortAsc) throws Exception {
        return getSearcherIntance().searchDocsInfo(fields, values, start, count, sortFields, isSortAsc,QuestAnswer.class);
    }

    /**
     * 智能提示     
     * @param input   关键字
     * @return
     * @throws Exception
     */
    public static Results<QuestAnswer> suggestQaSearch(String input) throws Exception {
        if (StringUtils.isBlank(input)) 
        	throw new Exception("The keyword is null.");
        return getSearcherIntance().suggestDocsSearch(input,QuestAnswer.class);
    }



    /**添加文档list
     * @param docs
     * @throws Exception
     */
    public static void addQaIndex(List<QuestAnswer> docs) throws Exception {
        getUpdaterIntance().addDocs(docs);
    }

    /**删除文档
     * @param id
     * @throws Exception
     */
    public static void removeQaIndex(String id) throws Exception {
        getUpdaterIntance().deleteDocIndex(id);
    }
    /**
     * 更新文档 根据最后更新时间
     * @throws Exception
     */
    public static void updateQaIndex() throws Exception {
        getUpdaterIntance().updateDocIndex();
    }

    /**
     * 清空文档
     * @throws Exception
     */
    public static void removeAllQaIndex() throws Exception {
        getUpdaterIntance().deleteDocIndexByQuery("*:*");
    }
    /**
     * 重新构建文档
     * @throws Exception
     */
    public static void rebuildQaIndex() throws Exception {
        getUpdaterIntance().rebuildDocIndex();
    }

    

    private static ISolrUpdater<QuestAnswer> getUpdaterIntance() {
        return QaUpdateSvHolder.qaUpdateSv;
    }
    private static ISolrSearcher<QuestAnswer> getSearcherIntance() {
    	return QaSearchSvHolder.qaAearchSv;
    }
    private static class QaSearchSvHolder {  
		private static final ISolrSearcher<QuestAnswer> qaAearchSv = new SolrSearcherImpl<QuestAnswer>(confKey);
	}
    private static class QaUpdateSvHolder {  
		private static final ISolrUpdater<QuestAnswer> qaUpdateSv = new SolrUpdaterImpl<QuestAnswer>(confKey);
	}
}
