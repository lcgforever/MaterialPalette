package com.chenguang.materialpalette.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MaterialDesignColor implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("mainColor")
    private String mainColor;

    @SerializedName("list")
    private List<ColorInfo> colorInfoList;

    public String getName() {
        return name;
    }

    public String getMainColor() {
        return mainColor;
    }

    public List<ColorInfo> getColorInfoList() {
        int index;
        for (index = 0; index < colorInfoList.size(); index++) {
            if (colorInfoList.get(index).getHex().equalsIgnoreCase(mainColor)) {
                break;
            }
        }
        if (index != 0) {
            ColorInfo colorInfo = colorInfoList.get(index);
            colorInfoList.remove(index);
            colorInfoList.add(0, colorInfo);
        }
        return colorInfoList;
    }

    public int getIndexForShade(String shade) {
        int index = 0;
        for (int i = 0; i < colorInfoList.size(); i++) {
            if (shade.equalsIgnoreCase(colorInfoList.get(i).getShade())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public class ColorInfo implements Serializable {

        @SerializedName("shade")
        private String shade;

        @SerializedName("hex")
        private String hex;

        public String getShade() {
            return shade;
        }

        public String getHex() {
            return hex;
        }
    }
}
