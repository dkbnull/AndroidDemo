package cn.wbnull.androiddemo.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cn.wbnull.androiddemo.constant.CommonConstants;
import cn.wbnull.helloutil.util.DateUtils;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * 日志工具类
 *
 * @author dukunbiao(null)  2020-01-26
 * https://github.com/dkbnull/AndroidDemo
 */
public class LoggerUtils {

    private static Logger logger;
    private static LogConfigurator logConfigurator;

    private static void init() {
        if (logConfigurator == null) {
            logConfigurator = new LogConfigurator();
        }

        logConfigurator.setMaxBackupSize(1024);
        logConfigurator.setFileName(CommonConstants.LOG_PATH + "log_" + DateUtils.dateFormat("yyyyMMdd") + ".log");
        logConfigurator.setRootLevel(Level.ALL);
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();
    }

    public static Logger getLogger() {
        if (logConfigurator == null) {
            init();
        }

        if (logger == null) {
            logger = Logger.getLogger("");
        }

        return logger;
    }

    public static String getMessage(String position, String method, String content) {
        return "[" + position + "]" + "\n" + "方法：" + method + "\n" + "参数：" + content + "\n";
    }

    public static void error(Object message) {
        getLogger().error(message);
    }

    public static void error(String position, String method, String content) {
        getLogger().error(getMessage(position, method, content));
    }

    public static void error(Object message, Throwable t) {
        getLogger().error(message, t);
    }

    public static void info(Object message) {
        getLogger().info(message);
    }

    public static void info(String position, String method, String content) {
        getLogger().info(getMessage(position, method, content));
    }

    public static void debug(Object message) {
        getLogger().debug(message);
    }

    public static void debug(String position, String method, String content) {
        getLogger().debug(getMessage(position, method, content));
    }

    public static void debug(Object message, Throwable t) {
        getLogger().debug(message, t);
    }
}
