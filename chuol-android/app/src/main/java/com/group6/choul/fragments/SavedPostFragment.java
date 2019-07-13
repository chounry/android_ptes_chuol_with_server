package com.group6.choul.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.group6.choul.R;
import com.group6.choul.adapters.SavePostAdapter;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.models.HouseModel;
import com.group6.choul.shares.MyConfig;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SavedPostFragment extends Fragment {
    private ListView listView;
    private List<HouseModel> saveModelList;
    private SavePostAdapter adapter;
    private String url = MyConfig.SERVE_ADDRESS + "/api/estates/get_saved_post";
    private TokenManager tokenManager;
    private int user_id;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved_post,container,false);
        listView = v.findViewById(R.id.save_listview);
        saveModelList = new ArrayList<>();
        adapter = new SavePostAdapter(getContext(),saveModelList);
        listView.setAdapter(adapter);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE));
        user_id = tokenManager.getUserId();

        getData(url);
        return v;
    }
    private void getData(String url) {
        try {

            final String ImgUrl = MyConfig.SERVE_ADDRESS;
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", user_id);
            Log.e("User_id ", user_id+"");
            final String requestBody = jsonBody.toString();
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Some", "Some");
                    Log.e("all data", response);
                    try {
                        JSONArray jsonResponse = new JSONArray(response);
                        for (int i = 0; i < jsonResponse.length(); i++) {
                            JSONObject each = jsonResponse.getJSONObject(i);
                            HouseModel room = new HouseModel();
                            room.setTitle(each.getString("title"));
                            room.setAddress(each.getString("address"));
                            room.setPrice(each.getString("price"));
                            room.setLocation(each.getString("location"));
                            room.setEstaeId(each.getInt("estate_id"));
                            room.setImg_url(ImgUrl + each.getString("img"));
                            saveModelList.add(room);
                            adapter.notifyDataSetChanged();
                        }
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
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            requestQueue.add(request);
        }catch (Exception e){

        }

    }
}
