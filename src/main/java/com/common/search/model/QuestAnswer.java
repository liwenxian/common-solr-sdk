package com.common.search.model;

import java.io.Serializable;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class QuestAnswer implements Serializable{
	private static final long serialVersionUID = 6315076074120702475L;
	@Field
	private String id; //问题id 
	@Field
	private String title; //标题
	@Field
	private String content; //内容
	@Field
	List<String> contexts; // title content  合集
	@Field
	private String code; //问题code
	@Field
	private int answers; //回答数
	@Field
	private int rates; //认同数 
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getAnswers() {
		return answers;
	}
	public void setAnswers(int answers) {
		this.answers = answers;
	}
	public int getRates() {
		return rates;
	}
	public void setRates(int rates) {
		this.rates = rates;
	}
	public List<String> getContexts() {
		return contexts;
	}
	public void setContexts(List<String> contexts) {
		this.contexts = contexts;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return "id=" + id + " title=" + title + " content=" + content 
				+ " answers=" + answers + " rates=" + rates;
	}
	
}
