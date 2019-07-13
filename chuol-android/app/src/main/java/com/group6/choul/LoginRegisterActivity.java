package com.group6.choul;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.group6.choul.adapters.PageAdapter;
import com.group6.choul.adapters.SignupAdapter;

public class LoginRegisterActivity extends AppCompatActivity {

    private SignupAdapter tab_adapter;
    private TabLayout tabLayout;
    private AppBarLayout myAppBar;
    private ViewPager tabPageView;
    private View app_bar_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

//         <------- handle toolbar
        app_bar_view = findViewById(R.id.app_bar_include);
        Toolbar myToolbar = findViewById(R.id.toolbar_login_resgister);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setElevation(0);
        showActionBar();

        // <------- handle toolbar

        tabPageView = findViewById(R.id.viewPage_home);
        tabLayout = findViewById(R.id.login_signup_tab_layout);


        tab_adapter = new SignupAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        tabPageView.setAdapter(tab_adapter);
        tabPageView.setOffscreenPageLimit(0);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPageView.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabPageView.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_action_bar, null);
        ImageButton backBtn = v.findViewById(R.id.nav_back_btn);

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginRegisterActivity.this, MainActivity.class));
                finish();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setCustomView(v);
    }
}
