package com.common.search;

import com.common.search.model.Results;


public interface ISolrSearcher<T> {
	/**
	 * 查询文档信息
	 * 
	 * @param fields
	 *            查询条件字段数组，必须和values长度一致且按序号对于，否则结果不可预料
	 * @param values
	 *            查询条件的值
	 * @param start
	 *            起始行数，分页用
	 * @param count
	 *            页面行数，分页用
	 * @param sortfields
	 *            排序字段，和isSortAsc对应
	 * @param isSortAsc
	 *            是否升序排序
	 * @return
	 * @throws Exception
	 */
	public Results<T> searchDocsInfo(String[] fields,
			String[] values, int start, int count, String[] sortfields,
			Boolean[] isSortAsc,Class<T> t) throws Exception;

	/**
	 * 用户输入的自动提示功能，只会返回10个结果，且结果列表只包含Name字段，只去Name字段
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public Results<T> suggestDocsSearch(String input,Class<T> t)
			throws Exception;

	
}

