package com.chenguang.materialpalette.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MaterialDesignColors {

    @SerializedName("colors")
    private List<MaterialDesignColor> colorList;

    public List<MaterialDesignColor> getColorList() {
        return colorList;
    }
}
