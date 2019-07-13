package com.group6.choul;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.choul.adapters.MessageListAdapter;
import com.group6.choul.login_register_handling.ApiService;
import com.group6.choul.login_register_handling.RetrofitBuilder;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.models.MemberDataForChat;
import com.group6.choul.models.MessageModel;
import com.group6.choul.models.ResponseStatus;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Message;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatInActivity extends AppCompatActivity implements RoomListener,SwipeRefreshLayout.OnRefreshListener {

    private String channelID = "DGIuZ5gOpwXwUfB1",roomID,estate_id;
    private Scaledrone scaledrone;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EditText mstEt;
    private ImageButton sendBtn;
    private ListView listView;
    private List<MessageModel> messageModelList;
    private ProgressBar mProgressbar;
    private MessageListAdapter messageAdapter;

    private ApiService service;
    private TokenManager tokenManager;
    private Intent fromChatOutFragment;
    private Call<List<MessageModel>> callGetMessages;
    private Call<ResponseStatus> callCreateMessage;


    private int user_id;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_in);
        mstEt = findViewById(R.id.msg_et);
        sendBtn = findViewById(R.id.send_btn);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_chat_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        fromChatOutFragment = getIntent();
        estate_id = fromChatOutFragment.getStringExtra("ESTATE_ID");
        roomID = "observable-"+estate_id;

//        roomID = "observable-myRoomName";

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
        user_id = tokenManager.getUserId();

        //------------ Action bar thing
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        showActionBar();
        // Action bar thing ------------

        listView = findViewById(R.id.message_listView);

        sendBtn = findViewById(R.id.send_btn);
        messageModelList = new ArrayList<>();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        initMessage();
    }


    private void initMessage(){
        // get the associated message to display
        mSwipeRefreshLayout.setRefreshing(true);
        String username = tokenManager.getUserName();
        MemberDataForChat data = new MemberDataForChat(username,"https://wowsciencecamp.org/wp-content/uploads/2018/07/dummy-user-img-1-400x400_x_acf_cropped.png",user_id);

        service = RetrofitBuilder.createService(ApiService.class);
        callGetMessages = service.get_messages(Integer.parseInt(estate_id), user_id);
        callGetMessages.enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
//                mProgressbar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);

                if(response.isSuccessful()) {
                    messageModelList = response.body();
                    for (int i = 0; i < messageModelList.size(); i++) {
                        messageModelList.get(i).setBelongsToCurrentUser(user_id);
                        messageModelList.get(i).setMemberData(data);
                        messageAdapter = new MessageListAdapter(ChatInActivity.this, messageModelList);
                        listView.setDivider(null);
                        listView.setAdapter(messageAdapter);
                        listView.setSelection(listView.getCount() - 1);
                    }
                }else{
                    Toast.makeText(ChatInActivity.this, "Cannot get the data...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
//                mProgressbar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);

                Toast.makeText(ChatInActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });

        scaledrone = new Scaledrone(channelID, data);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                Log.e("On Open ", "Success");
                // Since the MainActivity itself already implement RoomListener we can pass it as a target
                scaledrone.subscribe(roomID, ChatInActivity.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                Log.e("Scale Drone ","Connection Lost");
                System.err.println(ex);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);
            }
        });
    }

    @Override
    public void onOpen(Room room) {
        Log.e("Scale Drom onOpen", room.getName());
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        Log.e("Scale Drom onOpen", "Faild");
    }

    @Override
    public void onMessage(Room room, Message message) {

        final ObjectMapper mapper = new ObjectMapper();
        try {
            // member.clientData is a MemberData object, let's parse it as such
            final MemberDataForChat data = mapper.treeToValue(message.getMember().getClientData(), MemberDataForChat.class);

            // if the clientID of the message sender is the same as our's it was sent by us
            boolean belongsToCurrentUser = data.getUser_id() == user_id;
            // since the message body is a simple string in our case we can use json.asText() to parse it as such
            // if it was instead an object we could use a similar pattern to data parsing
            final MessageModel recieve_message = new MessageModel(message.getData().asText(), belongsToCurrentUser, data);
            if(belongsToCurrentUser){
                // we save when the message is the client user
//                Log.e("User id",data.getUser_id() + " "+user_id);
                saveMyMessage(recieve_message.getMessage());
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mstEt.getText().clear(); // clear edit text
                    messageAdapter.add(recieve_message);
                    // scroll the ListView to the last added element
                    listView.setSelection(listView.getCount() - 1);
                }
            });
        } catch (JsonProcessingException e) {
            initMessage();
            e.printStackTrace();
        }
    }

    public void sendMessage(){
        String message = mstEt.getText().toString().trim();
        if (!message.isEmpty()) {
//            messageAdapter.add(new MessageModel(message, false, new MemberData("Chounry","https://wowsciencecamp.org/wp-content/uploads/2018/07/dummy-user-img-1-400x400_x_acf_cropped.png")));
            scaledrone.publish(roomID, message);

        }
    }

    public void saveMyMessage(String msg){
        MessageModel tmpMsg = messageModelList.get(0);
        callCreateMessage = service.create_message(
                msg,
                user_id,
                tmpMsg.getToUserId(user_id),
                estate_id
            );
        callCreateMessage.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

                if(response.isSuccessful()){
                    Toast.makeText(ChatInActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                Log.e("request error ",t.getMessage());
            }
        });
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
    public void onRefresh() {
        initMessage();
    }
}
