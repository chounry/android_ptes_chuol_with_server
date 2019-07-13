package com.group6.choul;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.material.textfield.TextInputLayout;
import com.group6.choul.login_register_handling.AccessToken;
import com.group6.choul.login_register_handling.ApiError;
import com.group6.choul.login_register_handling.ApiService;
import com.group6.choul.login_register_handling.RetrofitBuilder;
import com.group6.choul.login_register_handling.Utils;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.fname_et)
    TextInputLayout fname_et;
    @BindView(R.id.lname_et)
    TextInputLayout lname_et;
    @BindView(R.id.email_et)
    TextInputLayout email_et;
    @BindView(R.id.phone_et)
    TextInputLayout phone_et;
    @BindView(R.id.password_et)
    TextInputLayout password_et;
    @BindView(R.id.confirm_password_et)
    TextInputLayout confirm_password_et;

    ApiService service;
    Call<AccessToken> call;
    AwesomeValidation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        service = RetrofitBuilder.createService(ApiService.class);
    }

    @OnClick(R.id.submit_btn)
    void register(){
        String fname = fname_et.getEditText().getText().toString();
        String lname = lname_et.getEditText().getText().toString();
        String email = email_et.getEditText().getText().toString();
        String password = password_et.getEditText().getText().toString();
        String phone = phone_et.getEditText().getText().toString();
        String con_password = confirm_password_et.getEditText().getText().toString();

        fname_et.setError(null);
        lname_et.setError(null);
        email_et.setError(null);
        password_et.setError(null);
        phone_et.setError(null);

        call = service.register(fname,lname,email,password,con_password,phone);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.e("onResponse : ", response+"");
                if(response.isSuccessful()){

                }else{
                    handleErrors(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {

            }
        });

    }

    public void setupValidation(){

    }

    private void handleErrors(ResponseBody responseBody){
        ApiError apiError = Utils.convertErrors(responseBody);
        for(Map.Entry<String, List<String>> errors: apiError.getErrors().entrySet()){
            if(errors.getKey().equals("fname")){
                fname_et.setError(errors.getValue().get(0));
            }
            if(errors.getKey().equals("lname")){
                lname_et.setError(errors.getValue().get(0));
            }
            if(errors.getKey().equals("email")){
                email_et.setError(errors.getValue().get(0));
            }
            if(errors.getKey().equals("password")){
                confirm_password_et.setError(errors.getValue().get(0));
            }
            if(errors.getKey().equals("phone")){
                phone_et.setError(errors.getValue().get(0));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }
}
