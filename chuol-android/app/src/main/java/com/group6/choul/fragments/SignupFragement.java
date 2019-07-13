package com.group6.choul.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.material.textfield.TextInputLayout;
import com.group6.choul.FilterActivity;
import com.group6.choul.HouseDetailActivity;
import com.group6.choul.MainActivity;
import com.group6.choul.R;
import com.group6.choul.adapters.HouseListAdapter;
import com.group6.choul.adapters.SignupAdapter;
import com.group6.choul.login_register_handling.AccessToken;
import com.group6.choul.login_register_handling.ApiError;
import com.group6.choul.login_register_handling.ApiService;
import com.group6.choul.login_register_handling.RetrofitBuilder;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.login_register_handling.Utils;
import com.group6.choul.models.HouseModel;
import com.group6.choul.models.SignupModel;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SignupFragement extends Fragment {
    private EditText editText;
    private TextView textView;
    private SignupAdapter adapter;

//    @BindView(R.id.fname_til)
    TextInputLayout fname_til;
//    @BindView(R.id.lname_til)
    TextInputLayout lname_til;
//    @BindView(R.id.email_til)
    TextInputLayout email_til;
//    @BindView(R.id.phone_til)
    TextInputLayout phone_til;
//    @BindView(R.id.password_til)
    TextInputLayout password_til;
//    @BindView(R.id.confirm_password_til)
    TextInputLayout confirm_password_til;
    Button createBtn;

    ApiService service;
    Call<AccessToken> call;
    AwesomeValidation validation;
    TokenManager tokenManager;

    RotateLoading rotateLoading;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup,container,false);
        fname_til = v.findViewById(R.id.fname_til);
        lname_til = v.findViewById(R.id.lname_til);
        email_til = v.findViewById(R.id.email_til);
        phone_til = v.findViewById(R.id.phone_til);
        password_til = v.findViewById(R.id.password_til);
        confirm_password_til = v.findViewById(R.id.confirm_password_til);
        createBtn = v.findViewById(R.id.create_btn);
        rotateLoading = v.findViewById(R.id.rotateloading);

        ButterKnife.bind(getActivity());
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateLoading.start();
                createBtn.setVisibility(View.GONE);
                String fname = fname_til.getEditText().getText().toString();
                String lname = lname_til.getEditText().getText().toString();
                String email = email_til.getEditText().getText().toString();
                String password = password_til.getEditText().getText().toString();
                String phone = phone_til.getEditText().getText().toString();
                String con_password = confirm_password_til.getEditText().getText().toString();

                ButterKnife.bind(getActivity());

                fname_til.setError(null);
                lname_til.setError(null);
                email_til.setError(null);
                password_til.setError(null);
                confirm_password_til.setError(null);
                phone_til.setError(null);

                if(!(fname.isEmpty() && lname.isEmpty())) {
                    fname = fname.substring(0, 1).toUpperCase() + fname.substring(1).toLowerCase();
                    lname = lname.substring(0, 1).toUpperCase() + lname.substring(1).toLowerCase();
                }

                call = service.register(fname,lname,email,password,con_password,phone);
                call.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        rotateLoading.stop();
                        createBtn.setVisibility(View.VISIBLE);
                        if(response.isSuccessful()){
                            tokenManager.saveToken(response.body());
                            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }else{
                            handleErrors(response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        rotateLoading.stop();
                        createBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        service = RetrofitBuilder.createService(ApiService.class);

        return v;
    }

    @OnClick(R.id.create_btn)
    void register(){
        String fname = fname_til.getEditText().getText().toString();
        String lname = lname_til.getEditText().getText().toString();
        String email = email_til.getEditText().getText().toString();
        String password = password_til.getEditText().getText().toString();
        String phone = phone_til.getEditText().getText().toString();
        String con_password = confirm_password_til.getEditText().getText().toString();

        fname_til.setError(null);
        lname_til.setError(null);
        email_til.setError(null);
        password_til.setError(null);
        phone_til.setError(null);

        call = service.register(fname,lname,email,password,con_password,phone);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.e("onResponse : ", response+"");
                if(response.isSuccessful()){
                    tokenManager.saveToken(response.body());
                    getActivity().setResult(300);
                    getActivity().finish();
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
                fname_til.setError(errors.getValue().get(0));
            }
            if(errors.getKey().equals("lname")){
                lname_til.setError(errors.getValue().get(0));
            }
            if(errors.getKey().equals("email")){
                email_til.setError(errors.getValue().get(0));
            }
            if(errors.getKey().equals("password")){
                confirm_password_til.setError(errors.getValue().get(0));
            }
            if(errors.getKey().equals("phone")){
                phone_til.setError(errors.getValue().get(0));
            }
        }
    }



}
