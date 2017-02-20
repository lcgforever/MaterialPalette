package com.chenguang.materialpalette.util;

public class ColorUtils {

    public static int[] getRGBFromHex(String colorHexStr) {
        if (colorHexStr.contains("#")) {
            colorHexStr = colorHexStr.replace("#", "");
        }
        int color = (int) Long.parseLong(colorHexStr, 16);
        int[] rgb = new int[3];
        rgb[0] = (color >> 16) & 0xFF;
        rgb[1] = (color >> 8) & 0xFF;
        rgb[2] = color & 0xFF;
        return rgb;
    }
}
