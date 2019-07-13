package com.group6.choul.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.group6.choul.ChatInActivity;
import com.group6.choul.R;
import com.group6.choul.login_register_handling.AccessToken;
import com.group6.choul.login_register_handling.ApiService;
import com.group6.choul.login_register_handling.RetrofitBuilder;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.models.ChatModel;
import com.group6.choul.adapters.ListChatAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ChatOutFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView listView ;
    private List<ChatModel> chatModelist;
    private ListChatAdapter adapter;

    ApiService service;
    TokenManager tokenManager;
    Call<List<ChatModel>> call;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_out,container,false);
        listView = v.findViewById(R.id.item_chat_id);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.fragment_chat_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        chatModelist = new ArrayList<>();
        adapter = new ListChatAdapter(getContext(),chatModelist);
        listView.setAdapter(adapter);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs",MODE_PRIVATE));
        service = RetrofitBuilder.createService(ApiService.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatModel item = ((ArrayList<ChatModel>)parent.getItemAtPosition(position)).get(position);
                Log.e("ES ",item.getChatRoom_id() +" ");
                Intent intent = new Intent(getActivity(), ChatInActivity.class);
                intent.putExtra("ESTATE_ID",item.getChatRoom_id()+"");
                startActivity(intent);
            }
        });

        getData();
        return v;
    }

    private void getData(){
        mSwipeRefreshLayout.setRefreshing(true);
        chatModelist.clear();
        call = service.get_chat_room(tokenManager.getUserId());
        call.enqueue(new Callback<List<ChatModel>>() {
            @Override
            public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if(response.isSuccessful()){
                    chatModelist.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(), "Cannot get the data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);

                Toast.makeText(getActivity(), "Cannot connect to the server", Toast.LENGTH_SHORT).show();
                Log.e("Chat Out Fragment : ", t.toString());
            }
        });
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
