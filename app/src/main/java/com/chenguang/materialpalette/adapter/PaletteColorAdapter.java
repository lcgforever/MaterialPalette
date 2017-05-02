package com.chenguang.materialpalette.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chenguang.materialpalette.R;
import com.chenguang.materialpalette.data.MaterialDesignColor;

import java.util.ArrayList;
import java.util.List;

public class PaletteColorAdapter extends RecyclerView.Adapter<PaletteColorAdapter.PaletteColorViewHolder> {

    private static final int NUMBER_OF_ITEMS = 5;

    private Context context;
    private LayoutInflater layoutInflater;
    private List<MaterialDesignColor> colorList;
    private String[] paletteOptionNames;
    private int[] selectedColorPositions;
    private int[] selectedColorInfoPositions;
    private int[] selectedColors;
    private int colorInfoTextColor;

    public PaletteColorAdapter(Context context, List<MaterialDesignColor> colorList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.colorList = colorList;
        colorInfoTextColor = Color.WHITE;
        paletteOptionNames = new String[NUMBER_OF_ITEMS];
        paletteOptionNames[0] = context.getString(R.string.primary_color_palette_name);
        paletteOptionNames[1] = context.getString(R.string.primary_dark_color_palette_name);
        paletteOptionNames[2] = context.getString(R.string.accent_color_palette_name);
        paletteOptionNames[3] = context.getString(R.string.primary_text_color_palette_name);
        paletteOptionNames[4] = context.getString(R.string.secondary_text_color_palette_name);
        selectedColorPositions = new int[NUMBER_OF_ITEMS];
        selectedColorInfoPositions = new int[NUMBER_OF_ITEMS];
        selectedColors = new int[NUMBER_OF_ITEMS];
        for (int i = 0; i < selectedColorPositions.length; i++) {
            selectedColorPositions[i] = i;
            selectedColorInfoPositions[i] = 0;
            selectedColors[i] = Color.parseColor(colorList.get(i).getMainColor());
        }
        selectedColorPositions[1] = selectedColorPositions[0];
        int darkShadeIndex = colorList.get(0).getIndexForShade("700");
        selectedColorInfoPositions[1] = darkShadeIndex;
        selectedColors[1] = Color.parseColor(colorList.get(0).getColorInfoList().get(darkShadeIndex).getHex());
    }

    @Override
    public PaletteColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_palette_item, parent, false);
        return new PaletteColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaletteColorViewHolder holder, int position) {
        holder.paletteNameTextView.setText(paletteOptionNames[position]);
        holder.colorSpinner.setSelection(selectedColorPositions[position]);
        MaterialDesignColor selectedColor = colorList.get(selectedColorPositions[position]);
        List<MaterialDesignColor.ColorInfo> colorInfoList = selectedColor.getColorInfoList();
        holder.updateColorInfoList(colorInfoList);
    }

    @Override
    public int getItemCount() {
        return NUMBER_OF_ITEMS;
    }

    public void invertTextColor() {
        if (colorInfoTextColor == Color.WHITE) {
            colorInfoTextColor = Color.BLACK;
        } else {
            colorInfoTextColor = Color.WHITE;
        }
        notifyDataSetChanged();
    }

    public int[] getSelectedColors() {
        return selectedColors;
    }


    class PaletteColorViewHolder extends RecyclerView.ViewHolder {

        TextView paletteNameTextView;
        Spinner colorSpinner;
        Spinner colorInfoSpinner;
        private ColorInfoSpinnerAdapter colorInfoSpinnerAdapter;

        PaletteColorViewHolder(View itemView) {
            super(itemView);

            paletteNameTextView = (TextView) itemView.findViewById(R.id.palette_name_text_view);
            colorSpinner = (Spinner) itemView.findViewById(R.id.color_spinner);
            colorInfoSpinner = (Spinner) itemView.findViewById(R.id.color_info_spinner);

            ColorSpinnerAdapter colorSpinnerAdapter = new ColorSpinnerAdapter(context, colorList);
            colorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            colorSpinner.setAdapter(colorSpinnerAdapter);

            colorInfoSpinnerAdapter = new ColorInfoSpinnerAdapter(context, new ArrayList<MaterialDesignColor.ColorInfo>());
            colorInfoSpinner.setAdapter(colorInfoSpinnerAdapter);

            colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedColorPositions[getLayoutPosition()] = position;
                    MaterialDesignColor materialDesignColor = colorList.get(position);
                    if (getLayoutPosition() == 1) {
                        selectedColorInfoPositions[1] = materialDesignColor.getIndexForShade("700");
                    }
                    updateColorInfoList(materialDesignColor.getColorInfoList());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            colorInfoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int selectedColorPosition = selectedColorPositions[getLayoutPosition()];
                    MaterialDesignColor materialDesignColor = colorList.get(selectedColorPosition);
                    selectedColorInfoPositions[getLayoutPosition()] = position;
                    MaterialDesignColor.ColorInfo colorInfo = materialDesignColor.getColorInfoList().get(position);
                    selectedColors[getLayoutPosition()] = Color.parseColor(colorInfo.getHex());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        void updateColorInfoList(List<MaterialDesignColor.ColorInfo> colorInfoList) {
            colorInfoSpinnerAdapter.updateColorInfoList(colorInfoList);
            int selectedIndex = selectedColorInfoPositions[getLayoutPosition()];
            colorInfoSpinner.setSelection(selectedIndex);
            selectedColors[getLayoutPosition()] = Color.parseColor(colorInfoList.get(selectedIndex).getHex());
        }
    }

    private class ColorSpinnerAdapter extends ArrayAdapter<MaterialDesignColor> {

        private LayoutInflater layoutInflater;
        private List<MaterialDesignColor> materialDesignColorList;

        ColorSpinnerAdapter(Context context, List<MaterialDesignColor> materialDesignColorList) {
            super(context, R.layout.spinner_color_item, materialDesignColorList);
            layoutInflater = LayoutInflater.from(context);
            this.materialDesignColorList = materialDesignColorList;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ColorViewHolder colorViewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.spinner_color_item, parent, false);
                colorViewHolder = new ColorViewHolder();
                colorViewHolder.colorTextView = (TextView) convertView.findViewById(R.id.spinner_color_text_view);
                convertView.setTag(colorViewHolder);
            } else {
                colorViewHolder = (ColorViewHolder) convertView.getTag();
            }

            MaterialDesignColor color = materialDesignColorList.get(position);

            colorViewHolder.colorTextView.setText(color.getName());
            colorViewHolder.colorTextView.setTextColor(Color.parseColor(color.getMainColor()));
            if (Color.parseColor(color.getMainColor()) == Color.WHITE) {
                colorViewHolder.colorTextView.setBackgroundColor(Color.GRAY);
            } else {
                colorViewHolder.colorTextView.setBackgroundColor(context.getResources().getColor(R.color.background_normal));
            }

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getView(position, convertView, parent);
        }


        private class ColorViewHolder {
            TextView colorTextView;
        }
    }

    private class ColorInfoSpinnerAdapter extends ArrayAdapter<MaterialDesignColor.ColorInfo> {

        private LayoutInflater layoutInflater;
        private List<MaterialDesignColor.ColorInfo> colorInfoList;

        ColorInfoSpinnerAdapter(Context context, List<MaterialDesignColor.ColorInfo> colorInfoList) {
            super(context, R.layout.spinner_color_info_item, colorInfoList);
            layoutInflater = LayoutInflater.from(context);
            this.colorInfoList = colorInfoList;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ColorInfoViewHolder colorInfoViewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.spinner_color_info_item, parent, false);
                colorInfoViewHolder = new ColorInfoViewHolder();
                colorInfoViewHolder.colorInfoTextView = (TextView) convertView.findViewById(R.id.spinner_color_info_text_view);
                convertView.setTag(colorInfoViewHolder);
            } else {
                colorInfoViewHolder = (ColorInfoViewHolder) convertView.getTag();
            }

            MaterialDesignColor.ColorInfo colorInfo = colorInfoList.get(position);

            String colorHex = colorInfo.getHex();
            colorInfoViewHolder.colorInfoTextView.setText(colorInfo.getShade() + " " + colorHex);
            colorInfoViewHolder.colorInfoTextView.setTextColor(colorInfoTextColor);
            if (Color.parseColor(colorHex) == Color.WHITE) {
                colorInfoViewHolder.colorInfoTextView.setBackgroundColor(Color.GRAY);
            } else {
                colorInfoViewHolder.colorInfoTextView.setBackgroundColor(Color.parseColor(colorHex));
            }

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getView(position, convertView, parent);
        }

        void updateColorInfoList(List<MaterialDesignColor.ColorInfo> colorInfoList) {
            this.colorInfoList.clear();
            this.colorInfoList.addAll(colorInfoList);
            notifyDataSetChanged();
        }

        private class ColorInfoViewHolder {
            TextView colorInfoTextView;
        }
    }
}
