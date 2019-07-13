package com.group6.choul.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputLayout;
import com.group6.choul.MainActivity;
import com.group6.choul.R;
import com.group6.choul.login_register_handling.AccessToken;
import com.group6.choul.login_register_handling.ApiError;
import com.group6.choul.login_register_handling.ApiService;
import com.group6.choul.login_register_handling.RetrofitBuilder;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.login_register_handling.Utils;
import com.victor.loading.rotate.RotateLoading;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SigninFragement extends Fragment {

    private Button loginBtn;
    private TextInputLayout email_til,password_til;
    private RotateLoading rotateLoading;

    ApiService service;
    TokenManager tokenManager;
    AwesomeValidation validator;
    Call<AccessToken> call;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signin,container,false);
        loginBtn = v.findViewById(R.id.login_btn);
        email_til = v.findViewById(R.id.email_til);
        password_til = v.findViewById(R.id.password_til);
        rotateLoading = v.findViewById(R.id.rotateloading);

        ButterKnife.bind(getActivity());
        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateLoading.start();
                loginBtn.setVisibility(View.GONE);
                String email = email_til.getEditText().getText().toString();
                String password = password_til.getEditText().getText().toString();

                email_til.setError(null);
                password_til.setError(null);

                call = service.login(email,password );
                call.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        rotateLoading.stop();
                        loginBtn.setVisibility(View.VISIBLE);
                        if(response.isSuccessful()) {
                            tokenManager.saveToken(response.body());

                            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();

                        }else if(response.code() == 422){
                            handleErrors(response.errorBody());
                        }else if(response.code() ==  401){
                            // unauthorize
                            ApiError apiError = Utils.convertErrors(response.errorBody());
                            Toast.makeText(getActivity(), apiError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        rotateLoading.stop();
                        loginBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        return v;
    }

    private void handleErrors(ResponseBody responseBody){
        ApiError apiError = Utils.convertErrors(responseBody);
        for(Map.Entry<String, List<String>> errors: apiError.getErrors().entrySet()){
            if(errors.getKey().equals("email")){
                email_til.setError(errors.getValue().get(0));
            }
            if(errors.getKey().equals("password")){
                password_til.setError(errors.getValue().get(0));
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }
}
