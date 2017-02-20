package com.chenguang.materialpalette.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenguang.materialpalette.R;
import com.chenguang.materialpalette.data.MaterialDesignColor;

import java.util.List;

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ColorViewHolder> {

    public interface ColorClickListener {
        void onColorClicked(MaterialDesignColor color);
    }

    private LayoutInflater layoutInflater;
    private List<MaterialDesignColor> colorList;
    private ColorClickListener colorClickListener;

    public ColorListAdapter(Context context,
                            List<MaterialDesignColor> colorList,
                            ColorClickListener colorClickListener) {
        layoutInflater = LayoutInflater.from(context);
        this.colorList = colorList;
        this.colorClickListener = colorClickListener;
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_color_item, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, int position) {
        MaterialDesignColor materialDesignColor = colorList.get(position);
        holder.colorNameTextView.setText(materialDesignColor.getName());
        holder.colorNameTextView.setBackgroundColor(Color.parseColor(materialDesignColor.getMainColor()));
    }

    @Override
    public int getItemCount() {
        return colorList == null ? 0 : colorList.size();
    }


    class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView colorNameTextView;

        ColorViewHolder(View itemView) {
            super(itemView);
            colorNameTextView = (TextView) itemView.findViewById(R.id.color_name_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MaterialDesignColor currentColor = colorList.get(getLayoutPosition());
            colorClickListener.onColorClicked(currentColor);
        }
    }
}
