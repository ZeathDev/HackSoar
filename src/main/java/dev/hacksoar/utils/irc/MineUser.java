package dev.hacksoar.utils.irc;

import net.minecraft.util.StringUtils;

public class MineUser {
    public static String qqNumber = null;
    public static String qqName = null;
    public static boolean isNull() {
        return (StringUtils.isNullOrEmpty(qqName) || StringUtils.isNullOrEmpty(qqNumber));
    }
}
