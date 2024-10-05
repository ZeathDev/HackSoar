package dev.hacksoar.utils;

import org.apache.logging.log4j.LogManager;

public class Logger {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    public static void dbg(String s) {
        LOGGER.info("[GLASS20] " + s);
    }

    public static void warn(String s) {
        LOGGER.warn("[GLASS20] " + s);
    }

    public static void warn(String s, Throwable t) {
        LOGGER.warn("[GLASS20] " + s, t);
    }

    public static void error(String s) {
        LOGGER.error("[GLASS20] " + s);
    }

    public static void error(String s, Throwable t) {
        LOGGER.error("[GLASS20] " + s, t);
    }

    public static void log(String s) {
        dbg(s);
    }
}
