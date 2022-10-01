/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.template.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class Tool {
    
    private static final Logger logger = Logger.getLogger(Tool.class);
    private static final Random RANDOM = new SecureRandom();
    
    public enum STATUS {
        ACTIVE(1),
        LOCK(0),
        DEL(404);
        public int val;
        
        private STATUS(int val) {
            this.val = val;
        }
    }
    
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public static String getHeaderParam(HttpServletRequest request, String headerkey) {
        String param = request.getHeader(headerkey);
        if (param == null) {
            param = "UNKNOW";
        }
        return param;
    }
    
    public static boolean writeFileText(String content, String path) {
        boolean flag = false;
        try {
            try (Writer outw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"))) {
                outw2.write(content);
                flag = true;
            } catch (Exception e) {
                Tool.out(e.getMessage());
                flag = false;
            }
        } catch (Exception e) {
            logger.error(Tool.getLogMessage(e));
            flag = false;
        }
        return flag;
    }
    
    public static String readFileText(String path) {
        String content = "";
        String result = "";
        try {
            FileInputStream fstream = new FileInputStream(path);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((content = br.readLine()) != null) {
                result += content + "\n";
            }
            in.close();
        } catch (IOException e) {
            System.err.println("Tool : Error: ReadFile >> " + e.getMessage());
        }
        return result;
    }
    
    public static Properties parseProperties(String s) {
        // grr at load() returning void rather than the Properties object
        // so this takes 3 lines instead of "return new Properties().load(...);"
        try {
            final Properties p = new Properties();
            p.load(new StringReader(s));
            return p;
        } catch (IOException e) {
            logger.error(Tool.getLogMessage(e));
            return null;
        }
    }
    
    public static boolean timeOut(long lastTime, int secondTimeOut) {
        long current = System.currentTimeMillis();
        return lastTime + (secondTimeOut * 1000) < current;
    }
    
    public static String getCurrentURL(HttpServletRequest request) {
        String currentURL = null;
        if (request.getAttribute("javax.servlet.forward.request_uri") != null) {
            currentURL = (String) request.getAttribute("javax.servlet.forward.request_uri");
        } else {
            currentURL = request.getRequestURI();
        }
        if (currentURL != null && request.getAttribute("javax.servlet.include.query_string") != null) {
            currentURL += "?" + request.getQueryString();
        }
        return currentURL;
    }
    
    public static void out(String input) {
        String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        System.out.println(className + ".class:[" + lineNumber + "] " + input);
    }
    
    public static String generateRandomPassword(int leng) {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        
        String pw = "";
        for (int i = 0; i < leng; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            pw += letters.substring(index, index + 1);
        }
        return pw;
    }
    
    public static String getLogMessage(Exception ex) {
        StackTraceElement[] trace = ex.getStackTrace();
        String str = DateProc.createTimestamp() + "||" + ex.getMessage() + "\n";
        for (StackTraceElement trace1 : trace) {
            str += trace1 + "\n";
        }
        return str;
    }
    
    public static boolean getBoolean(String status) {
        return status != null && status.equals("1");
    }
    
    public static boolean checkNull(String input) {
        return input == null || input.equals("") || input.equalsIgnoreCase("null");
    }
    
    public static boolean checkNull(Object input) {
        return input == null;
    }
    
    public static String getString(String input) {
        if (input != null) {
            input = input.trim();
        } else {
            input = "";
        }
        return input;
    }
    
    public static int getInt(String input) {
        int tem = 0;
        try {
            tem = Integer.parseInt(input.trim());
        } catch (NumberFormatException | NullPointerException e) {
            tem = 0;
        }
        return tem;
    }
    
    public static int getInt(String input, int defaultVal) {
        int tem = 0;
        try {
            tem = Integer.parseInt(input.trim());
        } catch (NumberFormatException | NullPointerException e) {
            tem = defaultVal;
        }
        return tem;
    }
    
    public static long getLong(String input) {
        long tem = 0;
        try {
            tem = Long.parseLong(input);
        } catch (NumberFormatException | NullPointerException e) {
            tem = 0;
        }
        return tem;
    }
    
    public static String stringToHTMLString(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;
        
        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                switch (c) {
                    case '"':
                        sb.append("&quot;");
                        break;
                    case '&':
                        sb.append("&amp;");
                        break;
                    case '<':
                        sb.append("&lt;");
                        break;
                    case '>':
                        sb.append("&gt;");
                        break;
                    // Handle Newline
                    case '\n':
                        sb.append("&lt;br/&gt;");
                        break;
                    default:
                        int ci = 0xffff & c;
                        if (ci < 160) // nothing special only 7 Bit
                        {
                            sb.append(c);
                        } else {
                            // Not 7 Bit use the unicode system
                            sb.append("&#");
                            sb.append(Integer.toString(ci));
                            sb.append(';');
                        }
                        break;
                }
            }
        }
        return sb.toString();
    }
    
    public static String convert2NoSign(String org) {
        if (org == null) {
            org = "";
            return org;
        }
        char arrChar[] = org.toCharArray();
        char result[] = new char[arrChar.length];
        for (int i = 0; i < arrChar.length; i++) {
            switch (arrChar[i]) {
                case '\u00E1':
                case '\u00E0':
                case '\u1EA3':
                case '\u00E3':
                case '\u1EA1':
                case '\u0103':
                case '\u1EAF':
                case '\u1EB1':
                case '\u1EB3':
                case '\u1EB5':
                case '\u1EB7':
                case '\u00E2':
                case '\u1EA5':
                case '\u1EA7':
                case '\u1EA9':
                case '\u1EAB':
                case '\u1EAD':
                case '\u0203':
                case '\u01CE': {
                    result[i] = 'a';
                    break;
                }
                case '\u00E9':
                case '\u00E8':
                case '\u1EBB':
                case '\u1EBD':
                case '\u1EB9':
                case '\u00EA':
                case '\u1EBF':
                case '\u1EC1':
                case '\u1EC3':
                case '\u1EC5':
                case '\u1EC7':
                case '\u0207': {
                    result[i] = 'e';
                    break;
                }
                case '\u00ED':
                case '\u00EC':
                case '\u1EC9':
                case '\u0129':
                case '\u1ECB': {
                    result[i] = 'i';
                    break;
                }
                case '\u00F3':
                case '\u00F2':
                case '\u1ECF':
                case '\u00F5':
                case '\u1ECD':
                case '\u00F4':
                case '\u1ED1':
                case '\u1ED3':
                case '\u1ED5':
                case '\u1ED7':
                case '\u1ED9':
                case '\u01A1':
                case '\u1EDB':
                case '\u1EDD':
                case '\u1EDF':
                case '\u1EE1':
                case '\u1EE3':
                case '\u020F': {
                    result[i] = 'o';
                    break;
                }
                case '\u00FA':
                case '\u00F9':
                case '\u1EE7':
                case '\u0169':
                case '\u1EE5':
                case '\u01B0':
                case '\u1EE9':
                case '\u1EEB':
                case '\u1EED':
                case '\u1EEF':
                case '\u1EF1': {
                    result[i] = 'u';
                    break;
                }
                case '\u00FD':
                case '\u1EF3':
                case '\u1EF7':
                case '\u1EF9':
                case '\u1EF5': {
                    result[i] = 'y';
                    break;
                }
                case '\u0111': {
                    result[i] = 'd';
                    break;
                }
                case '\u00C1':
                case '\u00C0':
                case '\u1EA2':
                case '\u00C3':
                case '\u1EA0':
                case '\u0102':
                case '\u1EAE':
                case '\u1EB0':
                case '\u1EB2':
                case '\u1EB4':
                case '\u1EB6':
                case '\u00C2':
                case '\u1EA4':
                case '\u1EA6':
                case '\u1EA8':
                case '\u1EAA':
                case '\u1EAC':
                case '\u0202':
                case '\u01CD': {
                    result[i] = 'A';
                    break;
                }
                case '\u00C9':
                case '\u00C8':
                case '\u1EBA':
                case '\u1EBC':
                case '\u1EB8':
                case '\u00CA':
                case '\u1EBE':
                case '\u1EC0':
                case '\u1EC2':
                case '\u1EC4':
                case '\u1EC6':
                case '\u0206': {
                    result[i] = 'E';
                    break;
                }
                case '\u00CD':
                case '\u00CC':
                case '\u1EC8':
                case '\u0128':
                case '\u1ECA': {
                    result[i] = 'I';
                    break;
                }
                case '\u00D3':
                case '\u00D2':
                case '\u1ECE':
                case '\u00D5':
                case '\u1ECC':
                case '\u00D4':
                case '\u1ED0':
                case '\u1ED2':
                case '\u1ED4':
                case '\u1ED6':
                case '\u1ED8':
                case '\u01A0':
                case '\u1EDA':
                case '\u1EDC':
                case '\u1EDE':
                case '\u1EE0':
                case '\u1EE2':
                case '\u020E': {
                    result[i] = 'O';
                    break;
                }
                case '\u00DA':
                case '\u00D9':
                case '\u1EE6':
                case '\u0168':
                case '\u1EE4':
                case '\u01AF':
                case '\u1EE8':
                case '\u1EEA':
                case '\u1EEC':
                case '\u1EEE':
                case '\u1EF0': {
                    result[i] = 'U';
                    break;
                }
                
                case '\u00DD':
                case '\u1EF2':
                case '\u1EF6':
                case '\u1EF8':
                case '\u1EF4': {
                    result[i] = 'Y';
                    break;
                }
                case '\u0110':
                case '\u00D0':
                case '\u0089': {
                    result[i] = 'D';
                    break;
                }
                case (char) 160: {
                    result[i] = ' ';
                    break;
                }
                default:
                    result[i] = arrChar[i];
            }
        }
        return new String(result);
    }
    
    public static String replace(String sStr, String oldStr, String newStr) {
        sStr = (sStr == null ? "" : sStr);
        String strVar = sStr;
        String tmpStr = "";
        String finalStr = "";
        int stpos = 0, endpos = 0, strLen = 0;
        while (true) {
            strLen = strVar.length();
            stpos = 0;
            endpos = strVar.indexOf(oldStr, stpos);
            if (endpos == -1) {
                break;
            }
            tmpStr = strVar.substring(stpos, endpos);
            tmpStr = tmpStr.concat(newStr);
            strVar = strVar.substring(endpos + oldStr.length() > sStr.length() ? endpos : endpos + oldStr.length(), strLen);
            finalStr = finalStr.concat(tmpStr);
            stpos = endpos;
        }
        finalStr = finalStr.concat(strVar);
        return finalStr;
    }
    
    public static boolean validIP(String ipRequest, String ipAllow) {
        boolean flag = false;
        try {
            if (ipAllow != null) {
                String[] allIP = ipAllow.split(",");
                for (String one : allIP) {
                    if (ipRequest.equals(one)) {
                        flag = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
        return flag;
    }
    
    public static String[] str2Arry(String input, String separator) {
        String[] list = null;
        try {
            if (!checkNull(input)) {
                list = input.split(separator);
            }
        } catch (Exception e) {
        }
        return list;
    }
    
    public static String buildAuth(String user, String pass) {
        String result = "";
        try {
            result = new StringBuffer(user).append(":").append(pass).toString();
        } catch (Exception e) {
            logger.error(Tool.getLogMessage(e));
        }
        return result;
    }
    
    public static byte[] buildEncodedAuth(String user, String pass) {
        byte[] result = null;
        try {
            String auth = buildAuth(user, pass);
            if (!Tool.checkNull(auth)) {
                result = Base64.getEncoder().encodeToString(auth.getBytes(Charset.forName("US-ASCII"))).getBytes();
            }
        } catch (Exception e) {
            logger.error(Tool.getLogMessage(e));
        }
        return result;
    }
    
    public static String buildAuthHeader(String user, String pass) {
        String result = "";
        try {
            byte[] encodedAuth = buildEncodedAuth(user, pass);
            if (encodedAuth != null) {
                result = "Basic " + new String(encodedAuth);
            }
        } catch (Exception e) {
            logger.error(Tool.getLogMessage(e));
        }
        return result;
    }
}
