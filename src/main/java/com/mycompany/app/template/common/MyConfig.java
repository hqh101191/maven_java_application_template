package com.mycompany.app.template.common;

import java.io.File;
import org.apache.log4j.BasicConfigurator;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jconfig.Configuration;
import org.jconfig.ConfigurationManager;
import org.jconfig.ConfigurationManagerException;
import org.jconfig.handler.XMLFileHandler;

public class MyConfig {

    static Logger logger = Logger.getLogger(MyConfig.class);
    private static final ConfigurationManager CFM = ConfigurationManager.getInstance();
    public static Configuration config;

    //-- Web Service
    public static String contextPath;
    public static int web_port;

    public static String LB_NODE = "";              // NODE Process
    public static boolean DEBUG = false;
    public static boolean SV_ALERT = false;
    public static boolean NODE_DEV = false;
    public static boolean BUID_CDR = false;
    public static String REJECT_ACC = "";

    // -- MAIL CONFIG ---
    public static String MAIL_USER;
    public static String MAIL_PASS;
    public static String MAIL_DEBUG;
    public static String MAIL_HOST;

    public MyConfig() {

    }

    public static void loadConfig() {
        File file = new File("../config/config.xml");
        MyLog.logDebug("Config File:" + file.getName());
        XMLFileHandler handler = new XMLFileHandler();
        handler.setFile(file);
        try {
            MyLog.logDebug("trying to load file config");
            CFM.load(handler, "engineConfig");
            MyLog.logDebug("file config successfully processed");
            config = ConfigurationManager.getConfiguration("engineConfig");
            //-- Read MyConfig WebServer
            contextPath = config.getProperty("contextPath", "/", "webService");
            web_port = config.getIntProperty("port", 6688, "webService");
            //--
            MAIL_USER = getString("MAIL_USER", "11alert@ahp.vn", "EMAIL");
            MAIL_PASS = getString("MAIL_PASS", "11ahp@alert.vn", "EMAIL");
            MAIL_HOST = getString("MAIL_HOST", "11mail.ahp.vn", "EMAIL");
            MAIL_DEBUG = getString("MAIL_DEBUG", "false", "EMAIL");
            //--
            LB_NODE = MyConfig.getString("LB_NODE", "NOTEx", "appconfig");
            SV_ALERT = MyConfig.getBoolean("SV_ALERT", false, "appconfig");
            NODE_DEV = MyConfig.getBoolean("NODE_DEV", false, "appconfig");
            BUID_CDR = MyConfig.getBoolean("BUID_CDR", false, "appconfig");
            REJECT_ACC = MyConfig.getString("REJECT_ACC", "", "appconfig");

        } catch (ConfigurationManagerException e) {
            logger.error("can not load file config!");
            logger.error(Tool.getLogMessage(e));
            System.exit(0);
        }
    }

    public static void initLog4j() {
        String log4jPath = "../config/log4j.properties";
        File fileLog4j = new File(log4jPath);
        if (fileLog4j.exists()) {
            Tool.out("----->Initializing Log4j:" + log4jPath);
            PropertyConfigurator.configure(log4jPath);
        } else {
            System.err.println("-----> *** " + log4jPath + " file not found, so initializing log4j with BasicConfigurator");
            BasicConfigurator.configure();
        }
    }

    public static int getInt(String properties, int defaultVal, String categoryName) {
        try {
            return Integer.parseInt(config.getProperty(properties, defaultVal + "", categoryName));
        } catch (NumberFormatException e) {
            logger.error(Tool.getLogMessage(e));
            return defaultVal;
        }
    }

    public static boolean getBoolean(String properties, boolean defaultVal, String categoryName) {
        try {
            return Integer.parseInt(config.getProperty(properties, 1 + "", categoryName)) == 1;
        } catch (NumberFormatException e) {
            logger.error(Tool.getLogMessage(e));
            return defaultVal;
        }
    }

    public static long getLong(String properties, long defaultVal, String categoryName) {
        try {
            return Long.parseLong(config.getProperty(properties, defaultVal + "", categoryName));
        } catch (NumberFormatException e) {
            logger.error(Tool.getLogMessage(e));
            return defaultVal;
        }
    }

    public static Double getDouble(String properties, Double defaultVal, String categoryName) {
        try {
            return Double.parseDouble(config.getProperty(properties, defaultVal + "", categoryName));
        } catch (NumberFormatException e) {
            logger.error(Tool.getLogMessage(e));
            return defaultVal;
        }
    }

    public static String getString(String properties, String defaultVal, String categoryName) {
        try {
            String val = config.getProperty(properties, defaultVal, categoryName);
            MyLog.logDebug(properties + ": " + val);
            return val;
        } catch (Exception e) {
            logger.error(Tool.getLogMessage(e));
            return defaultVal;
        }
    }

}
