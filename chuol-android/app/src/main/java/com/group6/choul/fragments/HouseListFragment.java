package com.group6.choul.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.group6.choul.FilterActivity;
import com.group6.choul.R;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.models.HouseModel;
import com.group6.choul.adapters.HouseListAdapter;
import com.group6.choul.shares.MyConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HouseListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView houseRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<HouseModel> homeModelist;
    private ProgressBar mProgressBar;
    private HouseListAdapter adapter;
    private String url = MyConfig.SERVE_ADDRESS + "/api/houses/get";
    private int user_id;
    private LinearLayout filterBtn;

    private TokenManager tokenManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_list, container, false);
        filterBtn = v.findViewById(R.id.filter_btn);
//        mProgressBar = v.findViewById(R.id.fragment_post_progress_bar);
        houseRecyclerView = v.findViewById(R.id.post_recyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        homeModelist = new ArrayList<>();

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        user_id = tokenManager.getUserId();

        adapter = new HouseListAdapter(homeModelist, getContext(), houseRecyclerView);
        houseRecyclerView.setHasFixedSize(true);
        adapter.setHasStableIds(true);
        houseRecyclerView.setItemViewCacheSize(20);
        houseRecyclerView.setLayoutManager(new LinearLayoutManager((getContext())));
        houseRecyclerView.setAdapter(adapter);

        getDataAysn();
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FilterActivity.class));
            }
        });

        return v;
    }

    private void getData() {
        mSwipeRefreshLayout.setRefreshing(true);
        final String baseImgUrl = MyConfig.SERVE_ADDRESS;
        homeModelist.clear();
        adapter.notifyDataSetChanged();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
//                mProgressBar.setVisibility(View.GONE);
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject each = jsonResponse.getJSONObject(i);
                        HouseModel tmp = new HouseModel();
                        tmp.setTitle(each.getString("title"));
                        tmp.setAddress(each.getString("address"));
                        tmp.setPrice(each.getString("price"));
                        tmp.setType(each.getString("house_type"));
                        tmp.setLocation(each.getString("location"));
                        tmp.setFor_sale_rent_status(each.getString("for_sale_status"));
                        tmp.setEstaeId(each.getInt("estate_id"));

                        tmp.setImg_url(baseImgUrl + each.getString("img"));
                        homeModelist.add(tmp);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "Cannot get data", Toast.LENGTH_SHORT).show();
                    Log.e("Json Error", e.toString());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                mProgressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Cannot connect to the server", Toast.LENGTH_SHORT).show();
                        Log.e("MYError", error.toString());
                    }
                });
        requestQueue.add(request);
    }

    private void getDataAysn() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                getData();
                return null;
            }
        }.execute();
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
