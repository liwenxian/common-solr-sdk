package com.common.search.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.search.ISolrUpdater;

public class SolrUpdaterImpl<T> implements ISolrUpdater<T> {

    private static final Logger log = LoggerFactory.getLogger(SolrUpdaterImpl.class);

    private String confKey = "";

    private ConcurrentUpdateSolrClient updateClient = null;

    private SolrHelper solrHelper = null;
    
    public SolrUpdaterImpl(String confKey){
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
        if (log.isInfoEnabled()) {
            log.info("new log configuration is received: " + conf);
        }
        try {
            updateClient = solrHelper.createDocUpdateServer(conf);
        } catch (Exception e) {
            log.error("Create Solr Client Error,conf=" + conf, e);
        }
    }

    @Override
    public void addDocs(List<T> docs) throws Exception {
        updateClient.addBeans(docs);
        commit();
    }

    @Override
    public void deleteDocIndexByQuery(String query) throws Exception {
        updateClient.deleteByQuery(query);
        commit();
    }

    @Override
    public void deleteDocIndex(String docId) throws Exception {
        updateClient.deleteById(docId);
        commit();
    }


    @Override
    public void rebuildDocIndex() throws Exception {
        SolrQuery query = new SolrQuery();
        query.set("qt", "/dataimport");
        query.set("command", "full-import");
        query.set("clean", "false");
        query.set("commit", "true");
        QueryResponse response = updateClient.query(query);
        if (response.getStatus() == 0) {
            log.info("--------------rebuild URL： " + updateClient + query.toString());
            log.info("--------------rebuild response： " + response.toString());
        } else {
            log.error("--------------rebuild URL： " + updateClient + query.toString());
            log.error("--------------rebuild response： " + response.toString());
        }
    }

    @Override
    public void updateDocIndex() throws Exception {
        SolrQuery query = new SolrQuery();
        query.set("qt", "/dataimport");
        query.set("command", "delta-import");
        QueryResponse response = updateClient.query(query);
        if (response.getStatus() == 0) {
            log.info("--------------update URL： " + updateClient + query.toString());
            log.info("--------------update response： " + response.toString());
        } else {
            log.error("--------------update URL： " + updateClient + query.toString());
            log.error("--------------update response： " + response.toString());
        }
    }


    private void commit() throws IOException, SolrServerException {
        updateClient.blockUntilFinished();
        UpdateResponse response = updateClient.commit();
        log.info("Commit  documents to solr Client, " + response.toString());
    }

    public String getconfKey() {
        return confKey;
    }

    public void setconfKey(String confKey) {
        this.confKey = confKey;
    }

}
