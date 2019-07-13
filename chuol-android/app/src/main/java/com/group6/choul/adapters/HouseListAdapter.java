package com.group6.choul.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.group6.choul.HouseDetailActivity;
import com.group6.choul.MainActivity;
import com.group6.choul.R;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.models.HouseModel;
import com.group6.choul.models.RoomModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class HouseListAdapter extends RecyclerView.Adapter<HouseListAdapter.MyHomeRecyClerView>{

    private List<HouseModel> modelList;
    private Context context; // new
    private RecyclerView recyclerViewHome;
    private View.OnClickListener mOnClickListener;


    public HouseListAdapter(List<HouseModel> modelList, Context context,RecyclerView recyclerViewHome) {
        this.modelList = modelList;
        this.context = context;
        this.recyclerViewHome = recyclerViewHome;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public MyHomeRecyClerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_each_house,null);
        mOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int itemPos = recyclerViewHome.getChildLayoutPosition(v);
                Intent intent = new Intent(context, HouseDetailActivity.class);
                intent.putExtra("ESTATE_ID",modelList.get(itemPos).getEstate_id()+"");
                context.startActivity(intent);
            }
        };
        myView.setOnClickListener(mOnClickListener);
        return new MyHomeRecyClerView(myView,context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHomeRecyClerView holder, int position) {
        HouseModel obj = modelList.get(position);
        Picasso.get().load(obj.getImg_url()).into(holder.imageView);
        holder.textviewTitle.setText(obj.getTitle());
        holder.textviewPrice.setText(obj.getPrice());
        holder.textviewAddress.setText(obj.getAddress());
        holder.textviewLocation.setText(obj.getLocation());
        holder.textviewType.setText(obj.getType());

        holder.for_sale_rent_status.setText(obj.getFor_sale_rent_status());
        holder.for_sale_rent_status.setBackgroundColor(context.getResources().getColor(R.color.forRent));
        if(obj.getFor_sale_rent_status().equals("For Sale"))
            holder.for_sale_rent_status.setBackgroundColor(context.getResources().getColor(R.color.forSale));


        Picasso.get().load(obj.getImg_url()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.modelList.size();
    }

    class MyHomeRecyClerView extends RecyclerView.ViewHolder{

    TextView textviewTitle;
    TextView textviewPrice;
    TextView textviewAddress;
    TextView textviewLocation,for_sale_rent_status;
    TextView textviewType;
    ImageView imageView;
    TextView txtSave;
    Context context;
    TokenManager tokenManager;
    int user_id,estate_id;

    public MyHomeRecyClerView(@NonNull View itemView, Context context) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgView_house);

        for_sale_rent_status = itemView.findViewById(R.id.for_sale_rent_tv);
        textviewType = itemView.findViewById(R.id.type_home);
        textviewLocation = itemView.findViewById(R.id.location_home);
        textviewAddress = itemView.findViewById(R.id.address_home);
        textviewPrice = itemView.findViewById(R.id.price_home);
        textviewTitle = itemView.findViewById(R.id.title_home);
        txtSave = itemView.findViewById(R.id.house_save_tv);
        this.context = context;

        tokenManager = TokenManager.getInstance(context.getSharedPreferences("prefs",context.MODE_PRIVATE));
        user_id  = tokenManager.getUserId();

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                estate_id = modelList.get(pos).getEstate_id();
                addToSave();
            }
        });
    }

    public void addToSave(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String URL = "http://172.23.12.108:8000/api/estates/add_to_saved";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", user_id);
            jsonBody.put("estate_id", estate_id);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject responeJson = new JSONObject(response);
                        txtSave.setText(responeJson.getString("status"));
                    }catch (Exception e){
                        Log.e("HOUSE LIST JSON ",e.toString());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
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

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return super.parseNetworkResponse(response);
                }
            };

            requestQueue.add(stringRequest);
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

}


