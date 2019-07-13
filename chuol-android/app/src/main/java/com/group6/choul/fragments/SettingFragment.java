package com.group6.choul.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.group6.choul.AboutusActivity;
import com.group6.choul.PersonalInfoActivity;
import com.group6.choul.R;
import com.group6.choul.TermofUseActivity;
import com.group6.choul.login_register_handling.AccessToken;
import com.group6.choul.login_register_handling.ApiService;
import com.group6.choul.login_register_handling.RetrofitBuilder;
import com.group6.choul.login_register_handling.TokenManager;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment{
    private LinearLayout linearSignOut, linearPersonal;
    private TextView aboutUs, termOfUse;

    ApiService service;
    TokenManager tokenManager;
    Call<AccessToken> call;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_m_setting,container,false);
        linearSignOut = v.findViewById(R.id.linear_signout);
        linearPersonal = v.findViewById(R.id.linear_personal);
        aboutUs = v.findViewById(R.id.aboutUs);
        termOfUse = v.findViewById(R.id.termofUse);

        ButterKnife.bind(getActivity());
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs",MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        linearSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call = service.logout();
                call.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        tokenManager.deleteToken();
                        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
                        Fragment homeFragment = new HomeFragment();
                        fragmentTransaction.replace(R.id.container_home, homeFragment);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.e("Request Error : ", t.toString());
                    }
                });
            }
        });

        linearPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivity(intent);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutusActivity.class);
                startActivity(intent);
            }
        });

        termOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TermofUseActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
