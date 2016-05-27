/*
 * Project: UniBss Copyright 2006 Asisinfo Co. Ltd. All right reserved.
 */
package com.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 建立日期 :2011-08-10<br>
 * 作者 : liwenxian<br>
 * 模块 : <br>
 * 描述 : <br>
 * 修改历史: 序号 日期 修改人 修改原因 <br>
 * 方便读取任一资源文件
 * 1 <br>
 * 2 <br>
 */
@SuppressWarnings("rawtypes")
public class ConfigUtil {

    private static Properties mConfig;
    private static Log log = LogFactory.getLog(ConfigUtil.class);
    private static String default_config_1 = "/conf/search-dev.properties";
    

    /**
     * 获取配置文件属性对应的值
     * @param propertSrc
     * 		  eg：/sso-server.properties  资源文件名
     * @param key
     * @return keyValue
     */
    public static String getParameterValue(String propertSrc,String key){
    	
    	mConfig = new Properties();

        try {
            Class config_class = ConfigUtil.class;
            InputStream is = config_class.getResourceAsStream(propertSrc);
            mConfig.load(is);
            log.info("successfully loaded propertSrc properties...");
            return mConfig.getProperty(key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    static {
        mConfig = new Properties();
        InputStream is = null;
        try {
            // we'll need this to get at our properties files in the classpath
            Class config_class = ConfigUtil.class;

            // first, lets load our default properties
            is = new FileInputStream(new File(config_class.getResource(default_config_1).toURI())); 
            mConfig.load(is);
            log.info("successfully loaded default properties.");

            // now, see if we can find our custom config

            // some debugging for those that want it
            if (log.isDebugEnabled()) {
                log.debug("AiCasConfig looks like this ...");

                String key = null;
                Enumeration keys = mConfig.keys();
                while (keys.hasMoreElements()) {
                    key = (String) keys.nextElement();
                    log.debug(key + "=" + mConfig.getProperty(key));
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }finally{
        	if(is!=null){
        		try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }

    }
    /**
     * Retrieve a property value
     * @param key Name of the property
     * @return String Value of property requested, null if not found
     */
    public static String getProperty(String key) {
        log.debug("Fetching property [" + key + "=" + mConfig.getProperty(key) + "]");
        return mConfig.getProperty(key);
    }

    /**
     * Retrieve a property value
     * @param key Name of the property
     * @param defaultValue Default value of property if not found
     * @return String Value of property requested or defaultValue
     */
    public static String getProperty(String key, String defaultValue) {
        log.debug("Fetching property [" + key + "=" + mConfig.getProperty(key) + ",defaultValue=" + defaultValue + "]");
        String value = mConfig.getProperty(key);
        if (value == null)
            return defaultValue;

        return value;
    }



    /**
     * Retrieve a property as an int ... defaults to 0 if not present.
     */
    public static int getIntProperty(String name) {
        return getIntProperty(name, 0);
    }

    /**
     * Retrieve a property as a int ... with specified default if not present.
     */
    public static int getIntProperty(String name, int defaultValue) {
        // get the value first, then convert
        String value = ConfigUtil.getProperty(name);

        if (value == null)
            return defaultValue;

        return (new Integer(value)).intValue();
    }

    /**
     * Retrieve all property keys
     * @return Enumeration A list of all keys
     */
    public static Enumeration keys() {
        return mConfig.keys();
    }
    public static boolean isTrue(String name){
    	return "true".equalsIgnoreCase(getProperty(name)==null?"":getProperty(name).trim());
    }
    public static void main(String[] args) {
		System.out.println(getProperty("reloadcache.request.plus"));
	}
	public static synchronized void reloadProperties() throws Exception{
	    	
    	mConfig = new Properties();
    	InputStream is = null;
        try {
            // we'll need this to get at our properties files in the classpath
            Class config_class = ConfigUtil.class;

            // first, lets load our default properties
            //is = config_class.getResourceAsStream(default_config_1);
            is = new FileInputStream(new File(config_class.getResource(default_config_1).toURI()));
            mConfig.load(is);
            log.info("successfully loaded default properties.");

            // now, see if we can find our custom config

            // some debugging for those that want it
            if (log.isDebugEnabled()) {
                log.debug("AiCasConfig looks like this ...");

                String key = null;
                Enumeration keys = mConfig.keys();
                while (keys.hasMoreElements()) {
                    key = (String) keys.nextElement();
                    log.debug(key + "=" + mConfig.getProperty(key));
                }
            }
            

        }
        catch (Exception e) {
            throw e;
        }finally{
        	if(is!=null){
        		is.close();
        	}
        }
    }

}
