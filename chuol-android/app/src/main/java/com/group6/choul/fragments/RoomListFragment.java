package com.group6.choul.fragments;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.group6.choul.R;
import com.group6.choul.adapters.RoomListAdapter;
import com.group6.choul.models.RoomModel;
import com.group6.choul.shares.MyConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoomListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView roomRecyclerView;
//    private ProgressBar mProgressBar;
    private List<RoomModel> roomModelist;
    private RoomListAdapter adapter;
    private String url = MyConfig.SERVE_ADDRESS + "/api/rooms/get";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_list, container, false);

//        mProgressBar = v.findViewById(R.id.fragment_post_progress_bar);

        roomRecyclerView = v.findViewById(R.id.post_recyclerView);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        roomModelist = new ArrayList<>();


        adapter = new RoomListAdapter(getContext(), roomModelist, roomRecyclerView);
        roomRecyclerView.setHasFixedSize(true);
        adapter.setHasStableIds(true);
        roomRecyclerView.setItemViewCacheSize(20);
        roomRecyclerView.setLayoutManager(new LinearLayoutManager((getContext())));
        roomRecyclerView.setAdapter(adapter);
//        mProgressBar.setVisibility(View.VISIBLE);
        getDataAysn();
        return v;

    }

    private void getData() {
        roomModelist.clear();
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(true);
        final String ImgUrl = MyConfig.SERVE_ADDRESS;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject each = jsonResponse.getJSONObject(i);
                        RoomModel room = new RoomModel();
                        room.setTitle(each.getString("title"));
                        room.setAddress(each.getString("address"));
                        room.setPrice(each.getString("price"));
                        room.setLocation(each.getString("location"));
                        room.setEstate_id(each.getInt("estate_id"));
                        room.setRoom_id(each.getInt("room_id"));
                        room.setImg_url(ImgUrl + each.getString("img"));
                        roomModelist.add(room);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "Cannot get the data", Toast.LENGTH_SHORT).show();
                    Log.e("Json Error", e.toString());
                }

            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    private void getDataAysn(){
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
