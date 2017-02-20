package com.chenguang.materialpalette.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chenguang.materialpalette.R;

public class PreviewActivity extends AppCompatActivity {

    private static final String EXTRA_THEME_COLORS = "EXTRA_THEME_COLORS";
    private static final int INDEX_PRIMARY_COLOR = 0;
    private static final int INDEX_PRIMARY_DARK_COLOR = 1;
    private static final int INDEX_ACCENT_COLOR = 2;
    private static final int INDEX_PRIMARY_TEXT_COLOR = 3;
    private static final int INDEX_SECONDARY_TEXT_COLOR = 4;

    private CoordinatorLayout coordinatorLayout;
    private int[] themeColors;

    public static void start(Context context, int[] themeColors) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(EXTRA_THEME_COLORS, themeColors);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        if (getIntent() != null) {
            themeColors = getIntent().getIntArrayExtra(EXTRA_THEME_COLORS);
        } else {
            themeColors = savedInstanceState.getIntArray(EXTRA_THEME_COLORS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(themeColors[INDEX_PRIMARY_DARK_COLOR]);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.preview_activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(themeColors[INDEX_PRIMARY_COLOR]);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.preview_activity_coordinator_layout);

        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.preview_activity_switch);
        switchCompat.setThumbTintList(new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}},
                new int[]{themeColors[INDEX_ACCENT_COLOR], getResources().getColor(R.color.control_normal)}));
        int partialAccentColor = themeColors[INDEX_ACCENT_COLOR];
        partialAccentColor = (partialAccentColor & 0x00FFFFFF) | 0x55000000;
        switchCompat.setTrackTintList(new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}},
                new int[]{partialAccentColor, getResources().getColor(R.color.control_highlight)}));

        AppCompatCheckBox checkBox = (AppCompatCheckBox) findViewById(R.id.preview_activity_check_box);
        checkBox.setSupportButtonTintList(new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}},
                new int[]{themeColors[INDEX_ACCENT_COLOR], getResources().getColor(R.color.control_normal)}));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.preview_activity_fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(themeColors[INDEX_ACCENT_COLOR]));

        AppCompatImageView accountImageView = (AppCompatImageView) findViewById(R.id.preview_activity_account_image_view);
        Drawable imageDrawable = DrawableCompat.wrap(accountImageView.getDrawable());
        DrawableCompat.setTint(imageDrawable, themeColors[INDEX_PRIMARY_TEXT_COLOR]);
        accountImageView.setImageDrawable(imageDrawable);

        TextView accountNameTextView = (TextView) findViewById(R.id.preview_activity_account_name_text_view);
        accountNameTextView.setTextColor(themeColors[INDEX_PRIMARY_TEXT_COLOR]);

        TextView accountEmailTextView = (TextView) findViewById(R.id.preview_activity_account_email_text_view);
        accountEmailTextView.setTextColor(themeColors[INDEX_SECONDARY_TEXT_COLOR]);

        TextView checkBoxTextView = (TextView) findViewById(R.id.preview_activity_check_box_text_view);
        checkBoxTextView.setTextColor(themeColors[INDEX_PRIMARY_TEXT_COLOR]);

        TextView switchTextView = (TextView) findViewById(R.id.preview_activity_switch_text_view);
        switchTextView.setTextColor(themeColors[INDEX_PRIMARY_TEXT_COLOR]);

        final AppCompatSeekBar seekBar = (AppCompatSeekBar) findViewById(R.id.preview_activity_seek_bar);
        seekBar.getThumb().setColorFilter(themeColors[INDEX_ACCENT_COLOR], PorterDuff.Mode.SRC_IN);
        seekBar.getProgressDrawable().setColorFilter(themeColors[INDEX_ACCENT_COLOR], PorterDuff.Mode.SRC_IN);

        Button saveButton = (Button) findViewById(R.id.preview_activity_save_button);
        saveButton.setTextColor(themeColors[INDEX_PRIMARY_TEXT_COLOR]);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackbar(String.valueOf(seekBar.getProgress()));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntArray(EXTRA_THEME_COLORS, themeColors);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void showSnackbar(String message) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout,
                String.format(getString(R.string.preview_snackbar_message_format), message),
                Snackbar.LENGTH_LONG)
                .setActionTextColor(themeColors[INDEX_ACCENT_COLOR]);
        snackbar.setAction(R.string.preview_snackbar_undo_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
