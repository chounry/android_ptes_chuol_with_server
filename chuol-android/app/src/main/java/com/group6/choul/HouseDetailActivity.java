package com.group6.choul;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.group6.choul.adapters.SlideAdapter;
import com.group6.choul.adapters.RelatedAdapter;
import com.group6.choul.login_register_handling.ApiService;
import com.group6.choul.login_register_handling.RetrofitBuilder;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.models.ImageModel;
import com.group6.choul.models.RelatedModel;
import com.group6.choul.models.ResponseStatus;
import com.group6.choul.shares.MyConfig;
import com.group6.choul.shares.MyUtilFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class HouseDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ViewPager viewPager;
    private FrameLayout map;
    private ProgressDialog mProgessDialog;

    private GoogleMap mMap;
    private RelatedAdapter adapter;
    private List<RelatedModel> modelList;
    private RecyclerView recyclerViewHome;
    private ArrayList<ImageModel> images;
    private String estate_id;
    private String dataUrl = MyConfig.SERVE_ADDRESS + "/api/houses/get_detail";

    private double lat,lng;

    private ImageButton send_btn;
    private EditText msg_et;

    private int estate_user_id;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);

        mProgessDialog = MyUtilFunctions.createProgressDialog(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        images = new ArrayList<>();
        viewPager = findViewById(R.id.slide);
        recyclerViewHome = findViewById(R.id.related_recycler_home);
        estate_id = getIntent().getStringExtra("ESTATE_ID");
        send_btn = findViewById(R.id.send_btn);
        msg_et = findViewById(R.id.msg_et);

        // <------- handle toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        showActionBar();
        // <------- handle toolbar

        SlideAdapter sladapter = new SlideAdapter(getApplicationContext(),images);

        viewPager.setAdapter(sladapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        mItemSelected = findViewById(R.id.tvItemSelected);
        map = findViewById(R.id.map);

        modelList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(layoutManager);

        adapter = new RelatedAdapter(modelList);
        recyclerViewHome.setAdapter(adapter);

        getDataAsyn();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!msg_et.getText().toString().trim().isEmpty()){
                    sendMessage(msg_et.getText().toString());
                }
            }
        });
    }

    private  void setInfo(String title, String for_sale_status, String price, String description, String street, String city, String bathroom, String bedroom, String floor, String size, String email, String phone, String phone_option, String currency){
        TextView description_tv,street_tv,city_tv,email_tv,phone_tv,title_tv,price_tv,for_sale_status_tv,bedroom_tv,bathroom_tv,floor_tv,size_tv;

        description_tv = findViewById(R.id.house_description);
        street_tv = findViewById(R.id.house_street);
        city_tv = findViewById(R.id.house_city);
        email_tv = findViewById(R.id.house_email);
        phone_tv = findViewById(R.id.house_phone);
        title_tv = findViewById(R.id.house_title);
        price_tv = findViewById(R.id.house_price);
        for_sale_status_tv =findViewById(R.id.for_sale_rent);
        bathroom_tv = findViewById(R.id.bathroom_tv);
        bedroom_tv = findViewById(R.id.bedroom_tv);
        floor_tv = findViewById(R.id.floor_tv);
        size_tv = findViewById(R.id.house_size_tv);

        title_tv.setText(title);
        description_tv.setText(description);
        email_tv.setText(email);
        street_tv.setText(street);
        city_tv.setText(city);
        for_sale_status_tv.setText(for_sale_status);
        bathroom_tv.setText(bathroom);
        bedroom_tv.setText(bedroom);
        floor_tv.setText(floor);
        size_tv.setText(size);

        phone_tv.setText(phone);
        if(phone_option != null)
            phone_tv.setText(phone  + "/" + phone_option);

        // image slide
        SlideAdapter slAdapter = new SlideAdapter(getApplicationContext(), images);
        viewPager.setAdapter(slAdapter);

        if (currency.equals("Dollar")){
            price_tv.setText(price + "$");
        }
        else{
            price_tv.setText(price + "R");
        }

    }
    private void getData( ) {

        try{
            RequestQueue requestQueue = Volley.newRequestQueue(HouseDetailActivity.this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("estate_id", estate_id);

            final String requestBody = jsonBody.toString();
            StringRequest request = new StringRequest(Request.Method.POST, dataUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    mProgessDialog.cancel();
                    try {
                        JSONObject responeJson = new JSONObject(response);
                        String title, for_sale_status, description, street, city, bathroom, bedroom, floor, size, email, phone, price, phone_option, currency;
                        Log.e("onResponse: ", responeJson.getJSONArray("img") + "");
                        JSONArray img_arr = responeJson.getJSONArray("img");
                        for (int i = 0; i < img_arr.length(); i++) {

                            ImageModel img = new ImageModel(MyConfig.SERVE_ADDRESS + img_arr.get(i).toString());
                            images.add(img);
                        }
                        estate_id = responeJson.getString("estate_id");
                        title = responeJson.getString("title");
                        for_sale_status = responeJson.getString("for_sale_status");
                        description = responeJson.getString("description");
                        street = responeJson.getString("address");
                        city = responeJson.getString("location");
                        email = responeJson.getString("email");
                        phone = responeJson.getString("phone");
                        price = responeJson.getString("price");
                        phone_option = responeJson.getString("phone_option");
                        currency = responeJson.getString("currency");
                        lat = responeJson.getDouble("lat");
                        lng = responeJson.getDouble("lng");
                        bathroom = responeJson.getString("bathroom");
                        bedroom = responeJson.getString("bedroom");
                        floor = responeJson.getString("floor");
                        size = responeJson.getString("house_size");
                        estate_user_id = responeJson.getInt("estate_user_id");

                        setInfo(title, for_sale_status, price, description, street, city, bathroom, bedroom, floor, size, email, phone, phone_option, currency);
                    } catch (Exception e) {
                        finish();
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
        }catch(Exception e){
            finish();
        }

    }

    private void getDataAsyn(){
        mProgessDialog.show();
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                getData();
                return null;
            }
        }.execute();
    }

    private void sendMessage(String content){
        TokenManager tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
        ApiService service = RetrofitBuilder.createService(ApiService.class);
        int from_self_user_id,to_user_user_id;
        from_self_user_id = tokenManager.getUserId();
        to_user_user_id = estate_user_id;

        Call<ResponseStatus> call = service.create_message(content, from_self_user_id,to_user_user_id, estate_id);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, retrofit2.Response<ResponseStatus> response) {
                if(response.isSuccessful()){
                    Intent chatInIntent = new Intent(HouseDetailActivity.this,ChatInActivity.class);
                    chatInIntent.putExtra("ESTATE_ID",estate_id);
                    startActivity(chatInIntent);
                    msg_et.setText("");
                }
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Phnom Penh"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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

}

