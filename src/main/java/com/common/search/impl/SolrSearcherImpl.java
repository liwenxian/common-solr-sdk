package com.common.search.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.search.ISolrSearcher;
import com.common.search.model.Results;


public class SolrSearcherImpl<T> implements ISolrSearcher<T> {

    private static final Logger log = LoggerFactory.getLogger(SolrSearcherImpl.class);

    private String confKey = "";

    private static final int SUGGEST_MAX_ROWS = 10;

    private SolrHelper solrHelper = null;

    //文档 搜索
    private SolrClient solrClient = null;

    public SolrSearcherImpl(String confKey){
    	this.confKey = confKey;
    	init();
    }

    public void init() {
        try {
            solrHelper = new SolrHelper();
            process(confKey);
        } catch (Exception e) {
            log.error("", e);
        }
    }


    public void process(String conf) {
        try {
        	solrClient = solrHelper.createSearchServer(conf);
        } catch (Exception e) {
            log.error("Create Solr Server Error,conf=" + conf, e);
        }
    }

    @Override
    public  Results<T> searchDocsInfo(String[] fields,
			String[] values, int start, int count, String[] sortFields,
			Boolean[] isSortAsc,Class<T> t) throws Exception{

        log.debug("fields:" + Arrays.toString(fields) + "," + "values:"
                + Arrays.toString(values) + ",start:" + start + ",count:"
                + count + ",sortFields:" + Arrays.toString(sortFields)
                + ",isSortAsc:" + Arrays.toString(isSortAsc));

        if (fields.length != values.length) {
        	throw new Exception(" fileds number not match values");
        }
        if (sortFields.length != isSortAsc.length) {
        	throw new Exception(" sortFileds number not match isSortAsc");
        }

        if (t == null) {
        	throw new Exception(" Class<T> is null");
        }

        SolrQuery query = null;
        Results<T> results = new Results<T>();
        try {
            // 初始化查询对象
            query = new SolrQuery();
            query.setHighlight(true);
            
			if (fields.length <= 0) {
				query.set("q", "*:*");
			} else {
				query.set("q", "*:*");
				for (int i = 0; i < fields.length; i++) {
					if (StringUtils.isNotBlank(fields[i])) {
						 if (t.getDeclaredField(fields[i]).getGenericType().toString().contains(  
		                        "java.lang.String"))
							 query.addFilterQuery(fields[i] + ":*" + values[i]+"*");
						 else
							 query.addFilterQuery(fields[i] + ":" + values[i]);

					}
				}
			}

            // 设置起始位置与返回结果数
            query.setStart(start);
            query.setRows(count);
            // 设置排序
            for (int i = 0; i < sortFields.length; i++) {
                if (isSortAsc[i]) {
                    query.addSort(sortFields[i], SolrQuery.ORDER.asc);
                } else {
                    query.addSort(sortFields[i], SolrQuery.ORDER.desc);
                }
            }

        } catch (Exception e) {
            log.error("", e);
        }
        QueryResponse rsp = null;
        try {
            rsp = solrClient.query(query);
            results.setCounts(rsp.getResults().getNumFound());
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
        // 返回查询结果
        results.setDocs(rsp.getBeans(t));
        return results;
    }



    @Override
    public Results<T> suggestDocsSearch(String input,Class<T> t) throws Exception {
        Results<T> results = new Results<T>();
        // 初始化查询对象
        // select?q=*%3A*&wt=json&indent=true&fl=name&rows=0&facet=true&facet.field=name&facet.mincount=1&facet.prefix=三
        SolrQuery query = new SolrQuery();
        query.set("q", "*:*");
        query.setRows(SUGGEST_MAX_ROWS);
        query.set("fl", "name");
        query.set("facet", "true");
        query.set("facet.field", "contexts");
        query.set("facet.mincount", "1");
        query.set("facet.prefix", input.toLowerCase());

        try {

            QueryResponse rsp = solrClient.query(query);

            List<FacetField> nameFacetFields = rsp.getFacetFields();
            List<T> suggests = new ArrayList<T>();
            if (null != nameFacetFields) {
                for (FacetField nameField : nameFacetFields) {
                    results.setCounts(nameField.getValueCount());
                    List<Count> counts = nameField.getValues();
                    int i = 0;
                    for (Count count : counts) {
                    	T obj = t.newInstance();
                        Method m = t.getDeclaredMethod("setTitle",String.class);
                        m.invoke(obj, count.getName());
                        suggests.add(obj);
                        i++;
                        if (i >= SUGGEST_MAX_ROWS)
                            break;
                    }
                    results.setCounts(counts.size() >= SUGGEST_MAX_ROWS ? SUGGEST_MAX_ROWS : counts.size());
                }
            }
            results.setDocs(suggests);
        } catch (Exception e) {
            log.error("", e);
            throw e;
		}
        // 返回查询结果
        return results;
    }




}
