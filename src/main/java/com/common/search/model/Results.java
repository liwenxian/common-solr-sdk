package com.common.search.model;

import java.util.List;

public class Results<T> {
	// 搜索的结果集
	private List<T> docs;
	// 总数
	private long count;

	public List<T> getDocs() {
		return docs;
	}

	public void setDocs(List<T> docs) {
		this.docs = docs;
	}

	public long getCount() {
		return count;
	}

	public void setCounts(long count) {
		this.count = count;
	}

}