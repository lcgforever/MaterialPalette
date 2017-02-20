package com.chenguang.materialpalette.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.chenguang.materialpalette.MaterialPaletteApp;
import com.chenguang.materialpalette.R;
import com.chenguang.materialpalette.adapter.ColorListAdapter;
import com.chenguang.materialpalette.data.MaterialDesignColor;
import com.chenguang.materialpalette.data.MaterialDesignColors;

public class MainActivity extends AppCompatActivity implements ColorListAdapter.ColorClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout navigationDrawerLayout;
    private ActionBarDrawerToggle navigationDrawerToggle;
    private NavigationView navigationView;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);

        navigationDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_drawer_layout);
        navigationDrawerToggle = new ActionBarDrawerToggle(this, navigationDrawerLayout, toolbar,
                R.string.action_open_drawer, R.string.action_close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (navigationView.getMenu().findItem(R.id.menu_palette).isChecked()) {
                    PaletteActivity.start(MainActivity.this);
                    finish();
                } else if (navigationView.getMenu().findItem(R.id.menu_about).isChecked()) {
                    AboutAppActivity.start(MainActivity.this);
                }
                navigationView.setCheckedItem(R.id.menu_colors);
            }
        };
        navigationDrawerLayout.addDrawerListener(navigationDrawerToggle);

        navigationView = (NavigationView) findViewById(R.id.main_activity_nav_view);
        navigationView.setCheckedItem(R.id.menu_colors);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView colorRecyclerView = (RecyclerView) findViewById(R.id.main_activity_recycler_view);
        colorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MaterialDesignColors materialDesignColors = ((MaterialPaletteApp) getApplication()).getMaterialDesignColors();
        ColorListAdapter colorListAdapter = new ColorListAdapter(this, materialDesignColors.getColorList(), this);
        colorRecyclerView.setAdapter(colorListAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navigationDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navigationDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            navigationDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.setCheckedItem(item.getItemId());
        navigationDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onColorClicked(MaterialDesignColor color) {
        ColorInfoActivity.start(this, color);
    }
}
