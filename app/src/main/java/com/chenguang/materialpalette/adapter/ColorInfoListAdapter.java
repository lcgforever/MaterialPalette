package com.chenguang.materialpalette.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenguang.materialpalette.R;
import com.chenguang.materialpalette.data.MaterialDesignColor.ColorInfo;
import com.chenguang.materialpalette.util.ColorUtils;

import java.util.List;

public class ColorInfoListAdapter extends RecyclerView.Adapter<ColorInfoListAdapter.ColorInfoViewHolder> {

    public interface ColorInfoClickListener {
        void onColorInfoClicked(ColorInfo colorInfo);
    }

    private String rgbFormatString;
    private LayoutInflater layoutInflater;
    private ClipboardManager clipboardManager;
    private List<ColorInfo> colorInfoList;
    private ColorInfoClickListener colorInfoClickListener;
    private int textColor = Color.WHITE;

    public ColorInfoListAdapter(Context context, List<ColorInfo> colorInfoList, ColorInfoClickListener colorInfoClickListener) {
        layoutInflater = LayoutInflater.from(context);
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        this.colorInfoList = colorInfoList;
        this.colorInfoClickListener = colorInfoClickListener;
        rgbFormatString = context.getString(R.string.rgb_format_text);
    }

    @Override
    public ColorInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_color_info_item, parent, false);
        return new ColorInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorInfoViewHolder holder, int position) {
        ColorInfo colorInfo = colorInfoList.get(position);

        holder.colorInfoContainer.setBackgroundColor(Color.parseColor(colorInfo.getHex()));
        holder.colorInfoShadeTextView.setText(colorInfo.getShade());
        String colorHex = colorInfo.getHex();
        holder.colorInfoHexTextView.setText(colorHex);
        int[] rgb = ColorUtils.getRGBFromHex(colorHex);
        holder.colorInfoRGBTextView.setText(String.format(rgbFormatString, rgb[0], rgb[1], rgb[2]));

        holder.colorInfoShadeTextView.setTextColor(textColor);
        holder.colorInfoHexTextView.setTextColor(textColor);
        holder.colorInfoRGBTextView.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return colorInfoList == null ? 0 : colorInfoList.size();
    }

    public void invertTextColor() {
        if (textColor == Color.WHITE) {
            textColor = Color.BLACK;
        } else {
            textColor = Color.WHITE;
        }
        notifyDataSetChanged();
    }


    class ColorInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View colorInfoContainer;
        TextView colorInfoShadeTextView;
        TextView colorInfoHexTextView;
        TextView colorInfoRGBTextView;

        ColorInfoViewHolder(View itemView) {
            super(itemView);

            colorInfoContainer = itemView.findViewById(R.id.color_info_container);
            colorInfoShadeTextView = (TextView) itemView.findViewById(R.id.color_info_shade_text_view);
            colorInfoHexTextView = (TextView) itemView.findViewById(R.id.color_info_hex_text_view);
            colorInfoRGBTextView = (TextView) itemView.findViewById(R.id.color_info_rgb_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ColorInfo colorInfo = colorInfoList.get(getLayoutPosition());
            ClipData clipData = ClipData.newPlainText("Color Hex", colorInfo.getHex());
            clipboardManager.setPrimaryClip(clipData);
            colorInfoClickListener.onColorInfoClicked(colorInfo);
        }
    }
}
