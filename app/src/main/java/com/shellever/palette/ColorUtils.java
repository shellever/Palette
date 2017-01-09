package com.shellever.palette;

import android.graphics.Color;

/**
 * Author: Shellever
 * Date:   1/4/2017
 * Email:  shellever@163.com
 */

public class ColorUtils {

    public static int parseBackgroundColor2(int color) {
        int counter = 0;
        counter += Color.red(color) >= 128 ? 1 : 0;
        counter += Color.green(color) >= 128 ? 1 : 0;
        counter += Color.blue(color) >= 128 ? 1 : 0;
        return counter >= 2 ? Color.BLACK : Color.WHITE;
    }

    // 通过分析背景色来决定当前文字的匹配颜色，使文字颜色自适应背景颜色
    public static int parseBackgroundColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        if (red >= 128 && green >= 128      // 三选二
                || red >= 128 && blue >= 128
                || green >= 128 && blue >= 128) {
            return Color.rgb(0, 0, 0);
        }
        return Color.rgb(255, 255, 255);
    }

    // #FF55FF => color
    // int color = Color.parseColor("#b64242");

    // color -> #FF55FF
    public static String toRGBHexString(final int color) {
        return toRGBHexString(Color.red(color), Color.green(color), Color.blue(color));
    }

    // (r,g,b) -> #FF55FF
    public static String toRGBHexString(int red, int green, int blue) {
        return toARGBHexString(-1, red, green, blue);
    }

    // default prefix: "#"
    // (a,r,g,b) -> #FF55FF55
    public static String toARGBHexString(int alpha, int red, int green, int blue) {
        return toARGBHexString("#", alpha, red, green, blue);
    }

    public static String toARGBHexString(String prefix, int alpha, int red, int green, int blue) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        if (alpha != -1) {
            String mAlphaStr = Integer.toHexString(alpha);
            sb.append(mAlphaStr.length() == 1 ? "0" + mAlphaStr : mAlphaStr);
        }
        String mRedStr = Integer.toHexString(red);
        sb.append(mRedStr.length() == 1 ? "0" + mRedStr : mRedStr);
        String mGreenStr = Integer.toHexString(green);
        sb.append(mGreenStr.length() == 1 ? "0" + mGreenStr : mGreenStr);
        String mBlueStr = Integer.toHexString(blue);
        sb.append(mBlueStr.length() == 1 ? "0" + mBlueStr : mBlueStr);
        return sb.toString().toUpperCase();
    }

}
