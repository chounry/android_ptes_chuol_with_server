package com.group6.choul;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.group6.choul.adapters.HouseTypeAdapter;
import com.group6.choul.models.HouseTypeModel;
import com.group6.choul.models.RoomModel;
import com.group6.choul.shares.MyConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FilterActivity extends AppCompatActivity {
    private RecyclerView house_type_recyclerView;
    private List<HouseTypeModel> houseTypeModelList;
    private String url = MyConfig.SERVE_ADDRESS + "/api/house_type/index";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        house_type_recyclerView = findViewById(R.id.house_type_rcv);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        showActionBar();
        getHouseType();
        house_type_recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void getHouseType(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    houseTypeModelList = new ArrayList<>();
                    for (int i = 0; i < jsonResponse.length(); i++) {

                        houseTypeModelList.add(new HouseTypeModel(jsonResponse.get(i).toString()));
                    }
                    HouseTypeAdapter adapter = new HouseTypeAdapter(FilterActivity.this, houseTypeModelList);
                    house_type_recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Log.e("Json Error", e.toString());
                }

            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MYError", error.toString());
            }
        });
        requestQueue.add(request);
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_action_bar_filter, null);
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
}
