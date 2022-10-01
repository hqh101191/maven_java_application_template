/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.template.common;

import org.apache.log4j.Logger;

public class MyLog {

    private static final Logger DEBUG_LOG = Logger.getLogger("DEBUG_LOG");

    public static void logDebug(String message) {
        String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        DEBUG_LOG.info(className + ".class:[" + lineNumber + "] " + message);
    }

}
