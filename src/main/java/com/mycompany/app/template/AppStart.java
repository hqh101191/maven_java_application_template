package com.mycompany.app.template;

import com.mycompany.app.template.common.MyConfig;
import com.mycompany.app.template.common.Tool;
import com.mycompany.app.template.db.PoolMng;
import org.apache.log4j.Logger;

public class AppStart {

    private static final Logger logger = Logger.getLogger(AppStart.class);

    public static boolean isRuning = true;

    static {
        try {
            MyConfig.initLog4j();

            MyConfig.loadConfig();

            if (!PoolMng.CreatePool()) {
                Tool.out("Khong khoi tao duoc ket noi DB");
                System.exit(1);
            }
        } catch (Exception ex) {
            Tool.out("Thong so gateway chua du..." + Tool.getLogMessage(ex));
            System.exit(1);
        }
    }

    public static WebServer websever;

    public static void appStop() {
        if (websever != null) {
            websever.stop();
        }
    }

    public static void reloadCnf() {
        MyConfig.loadConfig();
    }

    public static void main(String[] args) {
        try {

            websever = new WebServer();
            websever.start();

        } catch (Exception e) {
            Tool.out(Tool.getLogMessage(e));
        }
    }

}
