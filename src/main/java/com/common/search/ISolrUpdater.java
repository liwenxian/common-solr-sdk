package com.common.search;

import java.util.List;


public interface ISolrUpdater<T> {
	/**
	 * 将一组文档添加到索引中，如果索引中已经存在，则会更新
	 * @param <T>
	 * 
	 * @param docs
	 *            文档列表
	 * @throws Exception
	 */
	public void addDocs(List<T> docs) throws Exception;

	/**
	 * 删除查询结果的文档索引
	 * 
	 * @param query
	 * @throws Exception
	 */
	public void deleteDocIndexByQuery(String query) throws Exception;

	/**
	 * 删除某个文档索引
	 * 
	 * @param docId
	 * @throws Exception
	 */
	public void deleteDocIndex(String docId) throws Exception;

	/**
	 * 更新文档查询索引
	 * @throws Exception
	 */
	public void updateDocIndex() throws Exception;


	/**
	 * 重建索引
	 * 
	 * @throws Exception
	 */
	public void rebuildDocIndex() throws Exception;



}
