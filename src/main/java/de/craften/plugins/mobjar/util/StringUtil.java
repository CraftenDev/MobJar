package de.craften.plugins.mobjar.util;

import org.bukkit.ChatColor;

/**
 * Provides string utils for bukkit plugins.
 */
public class StringUtil {
    /**
     * Makes a string invisible.
     * You need to remove all '§' to get the original string back.
     *
     * @param s String to make invisible
     * @return Invisible string
     */
    public static String convertToInvisibleString(String s) {
        String hidden = "";
        for (char c : s.toCharArray()) hidden += ChatColor.COLOR_CHAR + "" + c;
        return hidden;
    }
}
