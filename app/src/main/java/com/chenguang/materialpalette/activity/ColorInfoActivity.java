package com.chenguang.materialpalette.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.chenguang.materialpalette.R;
import com.chenguang.materialpalette.adapter.ColorInfoListAdapter;
import com.chenguang.materialpalette.data.MaterialDesignColor;

import java.util.List;

public class ColorInfoActivity extends AppCompatActivity implements ColorInfoListAdapter.ColorInfoClickListener {

    private static final String EXTRA_MATERIAL_DESIGN_COLOR = "EXTRA_MATERIAL_DESIGN_COLOR";

    private CoordinatorLayout coordinatorLayout;
    private ColorInfoListAdapter colorInfoListAdapter;
    private MaterialDesignColor materialDesignColor;
    private boolean isBlackOrWhite;

    public static void start(Context context, MaterialDesignColor materialDesignColor) {
        Intent intent = new Intent(context, ColorInfoActivity.class);
        intent.putExtra(EXTRA_MATERIAL_DESIGN_COLOR, materialDesignColor);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_info);

        if (getIntent() != null) {
            materialDesignColor = (MaterialDesignColor) getIntent().getSerializableExtra(EXTRA_MATERIAL_DESIGN_COLOR);
        } else {
            materialDesignColor = (MaterialDesignColor) savedInstanceState.getSerializable(EXTRA_MATERIAL_DESIGN_COLOR);
        }

        String colorName = materialDesignColor.getName();
        isBlackOrWhite = getString(R.string.black_color_name).toLowerCase().equals(colorName.toLowerCase())
                || getString(R.string.white_color_name).toLowerCase().equals(colorName.toLowerCase());
        List<MaterialDesignColor.ColorInfo> colorInfoList = materialDesignColor.getColorInfoList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.color_info_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(colorName);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }

        if (!isBlackOrWhite) {
            String mainColorHex = colorInfoList.get(materialDesignColor.getIndexForShade("500")).getHex();
            toolbar.setBackgroundColor(Color.parseColor(mainColorHex));
            if (!isBlackOrWhite && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String darkColorHex = colorInfoList.get(materialDesignColor.getIndexForShade("700")).getHex();
                getWindow().setStatusBarColor(Color.parseColor(darkColorHex));
            }
        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.color_info_activity_container);
        RecyclerView colorInfoRecyclerView = (RecyclerView) findViewById(R.id.color_info_activity_recycler_view);
        colorInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        colorInfoListAdapter = new ColorInfoListAdapter(this, colorName, colorInfoList, this);
        colorInfoRecyclerView.setAdapter(colorInfoListAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.getBoolean(getString(R.string.pref_hint_shown), false)) {
            sharedPreferences.edit().putBoolean(getString(R.string.pref_hint_shown), true).apply();
            showHintDialog();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_MATERIAL_DESIGN_COLOR, materialDesignColor);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_color_info, menu);
        MenuItem invertMenuItem = menu.findItem(R.id.action_invert_color);
        invertMenuItem.setVisible(!isBlackOrWhite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_invert_color:
                colorInfoListAdapter.invertTextColor();
                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onColorInfoClicked(MaterialDesignColor.ColorInfo colorInfo) {
        String copyMessage = String.format(getString(R.string.color_copy_message),
                materialDesignColor.getName(), colorInfo.getShade(), colorInfo.getHex());
        Snackbar.make(coordinatorLayout, copyMessage, Snackbar.LENGTH_SHORT).show();
    }

    private void showHintDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.hint_dialog_title)
                .setMessage(R.string.hint_dialog_message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
