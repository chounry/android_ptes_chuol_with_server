package com.group6.choul.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.group6.choul.R;
import com.group6.choul.models.HouseModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class SavePostAdapter extends BaseAdapter {
    private Context context;
    private List<HouseModel> modeList;
    private String text = "Save";

    public SavePostAdapter(Context context, List<HouseModel> modeList) {
        this.context = context;
        this.modeList = modeList;
    }

    @Override
    public int getCount() {
        return modeList.size();
    }


    @Override
    public Object getItem(int position) {
        HouseModel model = modeList.get(position);
        return modeList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textviewTitle;
        TextView textviewPrice;
        TextView textviewAddress;
        TextView textviewLocation, for_sale_rent_status;
        TextView textviewType;
        ImageView imageView;
        TextView txtSave;
//        int user_id,estate_id;
        View v ;

        if (modeList.get(position).getType() == null) {
            v = View.inflate(context, R.layout.item_room_each, null);
            imageView = v.findViewById(R.id.imgView_room);
            textviewLocation = v.findViewById(R.id.location_room);
            textviewAddress = v.findViewById(R.id.address_room);
            textviewPrice = v.findViewById(R.id.price_room);
            textviewTitle = v.findViewById(R.id.title_room);
            txtSave = v.findViewById(R.id.room_save_tv);

            HouseModel obj = modeList.get(position);
            Picasso.get().load(obj.getImg_url()).into(imageView);
            textviewLocation.setText(obj.getLocation());
            textviewAddress.setText(obj.getAddress());
            textviewPrice.setText(obj.getPrice());
            textviewTitle.setText(obj.getTitle());
            txtSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    addToSave();
                }
            });

        }
        else{
            v = View.inflate(context, R.layout.item_each_house, null);
            textviewTitle = v.findViewById(R.id.title_home);
            textviewPrice = v.findViewById(R.id.price_home);
            textviewAddress = v.findViewById(R.id.address_home);
            textviewLocation= v.findViewById(R.id.location_home);
            textviewType = v.findViewById(R.id.type_home);
            for_sale_rent_status = v.findViewById(R.id.for_sale_rent_tv);
            imageView = v.findViewById(R.id.imgView_house);

            HouseModel obj = modeList.get(position);
            Picasso.get().load(obj.getImg_url()).into(imageView);
            textviewLocation.setText(obj.getLocation());
            textviewAddress.setText(obj.getAddress());
            textviewPrice.setText(obj.getPrice());
            textviewTitle.setText(obj.getTitle());
            textviewType.setText(obj.getType());
            for_sale_rent_status.setText(obj.getFor_sale_rent_status());
        }
        return v;
    }


//    public void addToSave(){
//        try {
//            RequestQueue requestQueue = Volley.newRequestQueue(context);
//            String URL = "http://192.168.100.208:8000/api/estates/add_to_saved";
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("user_id", user_id);
//            jsonBody.put("estate_id", estate_id);
//            final String requestBody = jsonBody.toString();
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try{
//                        JSONObject responeJson = new JSONObject(response);
//                        txtSave.setText(responeJson.getString("status"));
//                    }catch (Exception e){
//                        Log.e("HOUSE LIST JSON ",e.toString());
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("VOLLEY", error.toString());
//                }
//            }) {
//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
//
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return requestBody == null ? null : requestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                        return null;
//                    }
//                }
//
//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                        // can get more details such as response.headers
//                    }
//                    return super.parseNetworkResponse(response);
//                }
//            };
//
//            requestQueue.add(stringRequest);
//        }
//
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

}


