package com.group6.choul;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.util.Log;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import com.group6.choul.fragments.ChatOutFragment;
import com.group6.choul.fragments.HomeFragment;
import com.group6.choul.fragments.SavedPostFragment;
import com.group6.choul.fragments.SettingFragment;
import com.group6.choul.login_register_handling.ApiService;
import com.group6.choul.login_register_handling.RetrofitBuilder;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.models.UserModel;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayoutHome;
    private NavigationView navigationViewHome;
    private ActionBarDrawerToggle toggle;
    private Dialog formSelectDialog,loginDialog;
    private Button btnHouseForm, btnRoomForm;
    private TextView login_signup_btn;
    private Toolbar toolBar;
    private View login_signup_view,nav_header;
    private TokenManager tokenManager;

    private final int LOGOUT_CODE = 401;

    public ApiService service;
    public Call<UserModel> call;

    /* Things to do
    * - Change token time expired duration
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        formSelectDialog = new Dialog(this);
        loginDialog = new Dialog(this);
        drawerLayoutHome = findViewById(R.id.drawer_home);
        navigationViewHome = findViewById(R.id.nav_home);
        toolBar = findViewById(R.id.toolbar);
//        Log.e("My Toolbar ",);
        setSupportActionBar(toolBar);
        getSupportActionBar().setElevation(0);

        toggle = new ActionBarDrawerToggle(this,drawerLayoutHome,toolBar,R.string.opened_menu,R.string.closed_menu);
        drawerLayoutHome.addDrawerListener(toggle);
        toggle.syncState();
        navigationViewHome.setNavigationItemSelectedListener(this);


        getUserInfo(); // get user id and save it to sharedPrefs
        checkLogin();
        loadFragment(new HomeFragment()); // init fragment
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LOGOUT_CODE) {
            checkLogin();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        switch (item.getItemId()) {
            case R.id.post:
                if(tokenManager.getToken().getAccessToken() == null){
                    showLoginPopUp();
                }else{
                    showFormSelectPopup();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_home);
        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.item_home) {
            drawerLayoutHome.closeDrawers();
            if(fragment instanceof HomeFragment){
                return true;
            }
            loadFragment(new HomeFragment());
            return true;
        } else if (id == R.id.item_chat) {
            drawerLayoutHome.closeDrawers();
            if(!tokenManager.isLogin(tokenManager)){
                showLoginPopUp();
            }else {
                loadFragment(new ChatOutFragment());
            }
            return true;
        } else if (id == R.id.item_saved) {
            drawerLayoutHome.closeDrawers();
            if(!tokenManager.isLogin(tokenManager)){
                showLoginPopUp();
            }else {
                loadFragment(new SavedPostFragment());
            }
            return true;
        } else if (id == R.id.item_setting) {
            drawerLayoutHome.closeDrawers();
            if(!tokenManager.isLogin(tokenManager)){
                showLoginPopUp();
            }else {
                loadFragment(new SettingFragment());
            }
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_home);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    /*

   ============================ MY Functions =====================================
     */
    private void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        fm.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentCreated(fm, f, savedInstanceState);
                if(f instanceof HomeFragment){
                    // if user logout we check change the view in the nav head;
                    checkLogin();
                }
            }
        },true);

        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.container_home,fragment);
        tr.commit();
    }


    public void showFormSelectPopup() {
        formSelectDialog.setContentView(R.layout.custom_post_pop_up);
        btnHouseForm = formSelectDialog.findViewById(R.id.btn_house);
        btnRoomForm = formSelectDialog.findViewById(R.id.btn_room);

        btnHouseForm.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HouseFormActivity.class);
                startActivity(intent);
                finish();
                formSelectDialog.cancel();
            }
        });
        btnRoomForm.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RoomFormActivity.class);
                startActivity(intent);
                finish();
                formSelectDialog.cancel();
            }
        });

        formSelectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        formSelectDialog.show();
    }


    public void showLoginPopUp() {
        loginDialog.setContentView(R.layout.dialog_login);
        TextView login_sign_up_btn = loginDialog.findViewById(R.id.login_sign_up_btn);
        login_sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                startActivity(intent);
                finish();
                loginDialog.cancel();
            }
        });

        loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loginDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loginDialog.getWindow().setGravity(Gravity.TOP);
        loginDialog.show();
    }

    public void checkLogin(){
        // if user not login we put login register dialog to the nav header
        // else we put the name to the nav header
        nav_header = navigationViewHome.getHeaderView(0);
        LinearLayout user_info_lin = nav_header.findViewById(R.id.user_info_lin);
        login_signup_view = nav_header.findViewById(R.id.login_dialog_include);
        TextView username_tv = nav_header.findViewById(R.id.user_name_tv);
        TextView email_tv = nav_header.findViewById(R.id.email_tv);

        username_tv.setText(tokenManager.getUserName());
        email_tv.setText(tokenManager.getEmail());
        login_signup_view.setVisibility(View.GONE);
        user_info_lin.setVisibility(View.VISIBLE);

        if(!tokenManager.isLogin(tokenManager)) {
            user_info_lin.setVisibility(View.GONE);
            login_signup_view.setVisibility(View.VISIBLE);
            login_signup_btn = login_signup_view.findViewById(R.id.login_sign_up_btn);
            login_signup_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    public void getUserInfo(){
        // get the user info from the server and save them
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        call = service.get_user();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()){
                    tokenManager.saveUserInfo(response.body().getId(),response.body().getUsername(),response.body().getEmail());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Retrofit error : " , t.toString());
            }
        });
    }

}

