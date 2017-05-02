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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chenguang.materialpalette.MaterialPaletteApp;
import com.chenguang.materialpalette.R;
import com.chenguang.materialpalette.adapter.PaletteColorAdapter;
import com.chenguang.materialpalette.data.MaterialDesignColors;

public class PaletteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout navigationDrawerLayout;
    private ActionBarDrawerToggle navigationDrawerToggle;
    private NavigationView navigationView;
    private PaletteColorAdapter paletteColorAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, PaletteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        Toolbar toolbar = (Toolbar) findViewById(R.id.palette_activity_toolbar);
        setSupportActionBar(toolbar);

        navigationDrawerLayout = (DrawerLayout) findViewById(R.id.palette_activity_drawer_layout);
        navigationDrawerToggle = new ActionBarDrawerToggle(this, navigationDrawerLayout, toolbar,
                R.string.action_open_drawer, R.string.action_close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (navigationView.getMenu().findItem(R.id.menu_colors).isChecked()) {
                    MainActivity.start(PaletteActivity.this);
                    finish();
                } else if (navigationView.getMenu().findItem(R.id.menu_about).isChecked()) {
                    AboutAppActivity.start(PaletteActivity.this);
                }
                navigationView.setCheckedItem(R.id.menu_palette);
            }
        };
        navigationDrawerLayout.addDrawerListener(navigationDrawerToggle);

        navigationView = (NavigationView) findViewById(R.id.palette_activity_nav_view);
        navigationView.setCheckedItem(R.id.menu_palette);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView paletteRecyclerView = (RecyclerView) findViewById(R.id.palette_activity_recycler_view);
        paletteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MaterialDesignColors materialDesignColors = ((MaterialPaletteApp) getApplication()).getMaterialDesignColors();
        paletteColorAdapter = new PaletteColorAdapter(this, materialDesignColors.getColorList());
        paletteRecyclerView.setAdapter(paletteColorAdapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_palette, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_invert_color:
                paletteColorAdapter.invertTextColor();
                break;

            case R.id.action_preview:
                PreviewActivity.start(this, paletteColorAdapter.getSelectedColors());
                break;
        }
        return true;
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
}
