package com.chenguang.materialpalette;

import android.app.Application;

import com.chenguang.materialpalette.data.MaterialDesignColors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MaterialPaletteApp extends Application {

    private MaterialDesignColors materialDesignColors;

    @Override
    public void onCreate() {
        super.onCreate();

        String colorJsonString = loadColorJsonFromRawFile();
        Gson gson = new GsonBuilder().create();
        materialDesignColors = gson.fromJson(colorJsonString, MaterialDesignColors.class);
    }

    public MaterialDesignColors getMaterialDesignColors() {
        return materialDesignColors;
    }

    private String loadColorJsonFromRawFile() {
        String json = "";
        InputStream inputStream = null;

        try {
            inputStream = getResources().openRawResource(R.raw.colors);
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return json;
    }
}
