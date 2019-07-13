package com.group6.choul;

import android.app.Person;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.bumptech.glide.Glide;
import com.group6.choul.login_register_handling.AccessToken;
import com.group6.choul.login_register_handling.ApiService;
import com.group6.choul.login_register_handling.RetrofitBuilder;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.models.ResponseStatus;
import com.group6.choul.models.UserModel;
import com.group6.choul.shares.MyConfig;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;

import java.io.File;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PersonalInfoActivity extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener,BSImagePicker.ImageLoaderDelegate{
    private EditText et_fname,et_lname,et_email,et_phone;
    private Button submit_btn,select_img_btn,cancel_img_btn;
    private ImageView profile_imgV;
    private ProgressBar mProgressbar;

    private ApiService service;
    private TokenManager tokenManager;
    private Call<UserModel> call;
    private Call<ResponseStatus> callResponse;
    private UserModel userModel;
    private Uri selected_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        mProgressbar = findViewById(R.id.activity_personal_progressbar);
        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        submit_btn = findViewById(R.id.submit_btn);
        select_img_btn = findViewById(R.id.select_img_btn);
        cancel_img_btn = findViewById(R.id.cancel_img_btn);
        profile_imgV = findViewById(R.id.profile_img_view);

        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        cancel_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(MyConfig.SERVE_ADDRESS + userModel.getUser_profile_img()).into(profile_imgV);
                cancel_img_btn.setVisibility(View.GONE);
                selected_uri = null;
            }
        });

        call = service.get_user();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                mProgressbar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    userModel = response.body();
                    loadData(userModel);
                }else{
                    Toast.makeText(PersonalInfoActivity.this, "Cannot get the data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                mProgressbar.setVisibility(View.GONE);
                Toast.makeText(PersonalInfoActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });

        // <------- handle toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        showActionBar();
        // <------- handle toolbar

        select_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BSImagePicker singleSelectionPicker = new BSImagePicker.Builder("com.group6.choul.fileprovider")
                        .setMaximumDisplayingImages(200) //Default: Integer.MAX_VALUE. Don't worry about performance :)
                        .setSpanCount(4) //Default: 3. This is the number of columns
                        .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                        .setPeekHeight(Utils.dp2px(360)) //Default: 360dp. This is the initial height of the dialog.
                        .hideGalleryTile() //Default: show. Set this if you don't want to further let user select from a gallery app. In such case, I suggest you to set maximum displaying images to Integer.MAX_VALUE.
                        .setTag("A request ID")//Default: null. Set this if you need to identify which picker is calling back your fragment / activity.
                        // Default: true. Set this if you do not want the picker to dismiss right after selection. But then you will have to dismiss by yourself.
                        .build();
                singleSelectionPicker.show(getSupportFragmentManager(),"Select Profile Image");
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user_id = userModel.getId();
                String fname = et_fname.getText().toString();
                String lname = et_lname.getText().toString();
                String email = et_email.getText().toString();
                String phone = et_phone.getText().toString();


                fname = fname.substring(0,1).toUpperCase() + fname.substring(1).toLowerCase();
                lname = lname.substring(0,1).toUpperCase() + lname.substring(1).toLowerCase();

                service = RetrofitBuilder.createService(ApiService.class);

                // use the FileUtils to get the actual file by uri
                MultipartBody.Part body = null;
                RequestBody requestFile = null;
                File file = null;
                if(selected_uri != null) {
                    file = new File(selected_uri.getPath());
                    // create RequestBody instance from file
                    Log.e("My Path ",selected_uri.getPath());

                    requestFile = RequestBody.create(MediaType.parse("image/*"),file);
                    body = MultipartBody.Part.createFormData("img", file.getName(), requestFile);
                }

                RequestBody fnameRB = RequestBody.create(okhttp3.MultipartBody.FORM, fname);
                RequestBody lnameRB = RequestBody.create(okhttp3.MultipartBody.FORM, lname);
                RequestBody emailRB = RequestBody.create(okhttp3.MultipartBody.FORM, email);
                RequestBody phoneRB = RequestBody.create(okhttp3.MultipartBody.FORM, phone);
                RequestBody user_idRB = RequestBody.create(okhttp3.MultipartBody.FORM, user_id+"");
                // MultipartBody.Part is used to send also the actual file name



                callResponse = service.update_user(user_idRB,fnameRB, lnameRB,emailRB, phoneRB,body );
                callResponse.enqueue(new Callback<ResponseStatus>() {
                    @Override
                    public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                        if(response.isSuccessful()){
                            String responseStatus = response.body().getStatus();
                            if(responseStatus.equals("success")){
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseStatus> call, Throwable t) {

                    }
                });
            }
        });
    }



    private void loadData(UserModel responseBody){
        et_fname.setText(responseBody.getFname());
        et_lname.setText(responseBody.getLname());
        et_email.setText(responseBody.getEmail());
        et_phone.setText(responseBody.getPhone());
        Picasso.get().load(MyConfig.SERVE_ADDRESS + responseBody.getUser_profile_img()).into(profile_imgV);
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_action_bar, null);
        ImageButton backBtn = v.findViewById(R.id.nav_back_btn);

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
    }

    @Override
    public void loadImage(File imageFile, ImageView ivImage) {
        Glide.with(PersonalInfoActivity.this).load(imageFile).into(ivImage);
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        profile_imgV.setImageURI(uri);
        selected_uri = uri;
        cancel_img_btn.setVisibility(View.VISIBLE);
    }

}
