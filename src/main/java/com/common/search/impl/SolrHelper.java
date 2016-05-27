package com.common.search.impl;


import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.util.ConfigUtil;

public class SolrHelper {

    private static final Logger logger = LoggerFactory.getLogger(SolrHelper.class);

    private static final String KEY_SOLR_SERVER = "solrServer";
    private static final String KEY_SO_TIMEOUT = "soTimeout";
    private static final String KEY_CONNECTION_TIMEOUT = "connectionTimeout";
    private static final String KEY_MAX_CONNECTIONS = "maxConnections";
    private static final String KEY_MAX_TOTAL_CONNECTIONS = "maxTotalConnections";
    private static final String KEY_FOLLOW_REDIRECTS = "followRedirects";
    private static final String KEY_ALLOW_COMPRESSION = "allowCompression";
//    private static final String KEY_MAX_RETRIES = "maxRetries";
    private static final String KEY_UPDATE_QUEUE_SIZE = "queueSize";
    private static final String KEY_UPDATE_THREAD_NUM = "threadNum";
    private static final String KEY_COLLECTION = "collection";

    private String solrServerUrl = null;

    private int soTimeout;
    private int connectionTimeout;
    private int maxConnections;
    private int maxTotalConnections;
    private boolean followRedirects;
    private boolean allowCompression;
//    private int maxRetries;

    private int queueSize;
    private int threadNum;

    private String collection;

    //文档
    private SolrClient docClient = null;
    private ConcurrentUpdateSolrClient updateClient = null;


    private boolean processConfig(String confKey) {
        boolean changed = false;
        String conf = ConfigUtil.getProperty(confKey);
        if (logger.isInfoEnabled()) {
            logger.info("new log configuration is received: " + conf);
        }
        
        JSONObject jsonObj = new JSONObject(conf);
        solrServerUrl = jsonObj.getString(KEY_SOLR_SERVER);
        soTimeout = jsonObj.getInt(KEY_SO_TIMEOUT);
        connectionTimeout = jsonObj.getInt(KEY_CONNECTION_TIMEOUT);
        maxConnections = jsonObj.getInt(KEY_MAX_CONNECTIONS);
        maxTotalConnections = jsonObj.getInt(KEY_MAX_TOTAL_CONNECTIONS);
        followRedirects = jsonObj.getBoolean(KEY_FOLLOW_REDIRECTS);
        allowCompression = jsonObj.getBoolean(KEY_ALLOW_COMPRESSION);
        queueSize = jsonObj.getInt(KEY_UPDATE_QUEUE_SIZE);
        threadNum = jsonObj.getInt(KEY_UPDATE_THREAD_NUM);
        collection = jsonObj.getString(KEY_COLLECTION);
//        if (JSONValidator.isChanged(jsonObj, KEY_SOLR_SERVER, solrServerUrl)) {
//            changed = true;
//            solrServerUrl = jsonObj.getString(KEY_SOLR_SERVER);
//        }
        return changed;
    }

    /**
     * 搜索
     * @param conf paasConf
     * @return SolrServer
     * @throws Exception
     */
    public SolrClient createSearchServer(String conf) throws Exception {
        if (processConfig(conf) || null == docClient) {
            // 重建solrClient
        	docClient = createSolrSearchServer(collection);
        }
        return docClient;
    }


    private SolrClient createSolrSearchServer(String collectionStr) throws Exception {
    	HttpSolrClient server = null;
        try {

            server = new HttpSolrClient(solrServerUrl + (isNotBlank(collectionStr) ? collectionStr : ""));
            logger.info("solr server url:" + solrServerUrl + (isNotBlank(collectionStr) ? collectionStr : ""));

            server.setSoTimeout(soTimeout);
            server.setConnectionTimeout(connectionTimeout);
            server.setDefaultMaxConnectionsPerHost(maxConnections);
            server.setMaxTotalConnections(maxTotalConnections);
            server.setFollowRedirects(followRedirects);
            server.setAllowCompression(allowCompression);
//            server.setMaxRetries(maxRetries);
            server.setParser(new XMLResponseParser());

        } catch (Exception e) {
            logger.error("创建solr搜索 {} 连接失败" + collectionStr, e);
        }
        return server;
    }

    /**
     * 更新服务
     * @param conf paasConf
     * @return ConcurrentUpdateSolrClient
     * @throws Exception
     */
    public ConcurrentUpdateSolrClient createDocUpdateServer(String conf) throws Exception {
        if (processConfig(conf) || null == updateClient) {
            // 重建solrClient
        	updateClient = createUpdateServer(collection);
        }
        return updateClient;
    }


    private ConcurrentUpdateSolrClient createUpdateServer(String collectionStr) throws Exception {
    	ConcurrentUpdateSolrClient server = null;
        try {
            server = new ConcurrentUpdateSolrClient(solrServerUrl +
                    (isNotBlank(collectionStr) ? collectionStr : ""), queueSize, threadNum);

            logger.info("solr server url:" + solrServerUrl +
                    (isNotBlank(collectionStr) ? collectionStr : ""));

            server.setSoTimeout(soTimeout);
            server.setConnectionTimeout(connectionTimeout);
            server.setRequestWriter(new BinaryRequestWriter());

        } catch (Exception e) {
            logger.error("创建solr连接 {} 失败：" + collectionStr, e);
        }
        return server;
    }

    private boolean isNotBlank(String str) {
        return !(str == null || "".equals(str.trim()));
    }

}
