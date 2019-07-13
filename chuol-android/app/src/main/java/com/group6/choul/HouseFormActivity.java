package com.group6.choul;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.textfield.TextInputLayout;
import com.group6.choul.adapters.ImgFormAdapter;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.models.ImgFormModel;
import com.group6.choul.shares.MyConfig;
//import com.squareup.haha.perflib.Main;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HouseFormActivity extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate{
    private ImageButton map_imgBtn;
    private Toolbar myToolbar;
    private LinearLayout upload_img_btn;
    private RecyclerView recyclerView;
    private Button submit_btn;
    private EditText title_et, price_et, description_et, phone_et, phone_opt_et, address_et, bathroom_h, bedroom_h, floor_h, size_et,house_yard_size_et;
    private TextInputLayout priceTil;

    private List<ImgFormModel> img_models_list;
    private List<Uri> imgs_uri;
    private ImgFormAdapter img_adapter;
    private String title,type_house,price, description, phone, phone_opt, address, bathroom, bedroom, floor, house_size, yard_size,forSale_status,city_id,currency,duration;
    private Spinner for_sale_status,type,city,currency_spinner,duration_spinner;
    private Switch contact_swtich;
    private static final String TAG = "HouseFromActivity";
    private double lat,lng;
    private TokenManager tokenManager;
    private int user_id;

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private String upload_url = MyConfig.SERVE_ADDRESS + "/api/houses/create";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_form);


        recyclerView = findViewById(R.id.img_recyler_view);
        myToolbar = findViewById(R.id.my_toolbar);
        map_imgBtn = findViewById(R.id.map_imgBtn);
        upload_img_btn = findViewById(R.id.upload_img_btn);
        submit_btn = findViewById(R.id.submit_btn);
        title_et = findViewById(R.id.title_et);
        price_et = findViewById(R.id.price_et);
        description_et = findViewById(R.id.description_et);
        address_et = findViewById(R.id.address_et);
        phone_et = findViewById(R.id.phone_et);
        phone_opt_et = findViewById(R.id.phone_opt_et);
        contact_swtich = findViewById(R.id.contact_switch_house);
        bathroom_h = findViewById(R.id.bathroom_et);
        bedroom_h = findViewById(R.id.bedroom_et);
        floor_h = findViewById(R.id.floor_et);
        size_et = findViewById(R.id.house_size_et);
        house_yard_size_et = findViewById(R.id.yard_size_et);
        for_sale_status = findViewById(R.id.for_what_spinner);
        type = findViewById(R.id.house_type_spinner);
        city = findViewById(R.id.city_spinner);
        currency_spinner = findViewById(R.id.currency_sp);
        duration_spinner = findViewById(R.id.duration_spinner);
        priceTil = findViewById(R.id.price_til);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
        user_id = tokenManager.getUserId();

        // <------- handle toolbar
        setSupportActionBar(myToolbar);
        showActionBar();
        // <------- handle toolbar

        upload_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BSImagePicker multiSelectionPicker = new BSImagePicker.Builder("com.yourdomain.yourpackage.fileprovider")
                        .isMultiSelect() //Set this if you want to use multi selection mode.
                        .setMinimumMultiSelectCount(2) //Default: 1.
                        .setMaximumMultiSelectCount(10) //Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
                        .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                        .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                        .setMultiSelectDoneTextColor(R.color.colorAccent) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                        .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                        .disableOverSelectionMessage() //You can also decide not to show this over select message.
                        .build();
                multiSelectionPicker.show(getSupportFragmentManager(), "picker");
            }
        });
        img_models_list = new ArrayList<>();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // Init img upload ------------------->

        map_imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapFormActivity.class);
                startActivity(intent);
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = title_et.getText().toString();
                price = price_et.getText().toString();
                description = description_et.getText().toString();
                phone = phone_et.getText().toString();
                phone_opt = phone_opt_et.getText().toString();
                address = address_et.getText().toString();
                bedroom = bedroom_h.getText().toString();
                bathroom = bathroom_h.getText().toString();
                floor = floor_h.getText().toString();
                house_size = size_et.getText().toString();
                yard_size = house_yard_size_et.getText().toString();
                forSale_status = for_sale_status.getSelectedItem().toString();
                type_house = type.getSelectedItem().toString();
                city_id = city.getSelectedItem().toString();
                currency = currency_spinner.getSelectedItem().toString();
                duration = duration_spinner.getSelectedItem().toString();
                if(!title.isEmpty() && !price.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !bedroom.isEmpty() &&
                        !bathroom.isEmpty() && !floor.isEmpty() && !house_size.isEmpty() && !forSale_status.isEmpty()) {
                    uploadMultipart(imgs_uri);
                }
              }

        });
        contact_swtich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceTil.setVisibility(View.VISIBLE);
                if(contact_swtich.isChecked()){
                    priceTil.setVisibility(View.GONE);
                }
            }
        });


        //init map
        if(isServicesOK()){
            init();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == MapFormActivity.RESULT_OK){
                lat = data.getDoubleExtra("lat",3);
                lng = data.getDoubleExtra("lng",3);
                lat = Math.floor(lat * 100000) / 100000;
                lng = Math.floor(lng * 100000) / 100000;
                Log.d("Latlng", "lat:"+lat+"lng"+lng);
            }
            if (resultCode == MapFormActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
    private void init(){
        map_imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapFormActivity.class);
                startActivityForResult(intent,1 );
            }
        });
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HouseFormActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HouseFormActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private void goBack(){
        startActivity(new Intent(HouseFormActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_action_bar, null);

        ImageButton backBtn = v.findViewById(R.id.nav_back_btn);

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
    }


    public void uploadMultipart(List<Uri> filePath) {

        /* *********************************************************************
         * **********************  place to upload data to server ******************
         ******************************************************************* */
        ProgressDialog dialog = ProgressDialog.show(this, "",
                "Uploading. Please wait...", true);
        //Uploading code
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                try {

                    String uploadId = UUID.randomUUID().toString();

                    //Creating a multi part request
                    MultipartUploadRequest mUploadRequest = new MultipartUploadRequest(HouseFormActivity.this, uploadId, upload_url)
                            .addParameter("title", title)
                            .addParameter("price", price)
                            .addParameter("description", description)
                            .addParameter("phone", phone)
                            .addParameter("address", address)
                            .addParameter("bathroom", bathroom)
                            .addParameter("bedroom", bedroom)
                            .addParameter("floor", floor)
                            .addParameter("house_size", house_size)
                            .addParameter("yard_size", yard_size)
                            .addParameter("for_sale_status", forSale_status)
                            .addParameter("type", type_house)
                            .addParameter("city_id", city_id)
                            .addParameter("lat", lat + "")
                            .addParameter("lng", lng + "")
                            .addParameter("phone_option", phone_opt)
                            .addParameter("currency", currency)
                            .addParameter("duration", duration)
                            .addParameter("user_id", user_id + "")
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2); // try request at least 2 time before give up

                    for (int i = 0; i < filePath.size(); i++) {
                        // add many imgs to the request
                        String path = filePath.get(i).getPath();
                        mUploadRequest.addFileToUpload(path, "imgs" + "[" + i + "]");
                    }

                    mUploadRequest.startUpload();

                    startActivity(new Intent(HouseFormActivity.this, MainActivity.class));
                    finish();
//                    Toast.makeText(HouseFormActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                } catch (Exception exc) {
//                    Toast.makeText(HouseFormActivity.this, "Multipart Error" + exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    Thread.sleep(2000);
                }catch (Exception e){

                }
            }
        }.execute();
    }

    // image selection handling
    @Override
    public void loadImage(File imageFile, ImageView ivImage) {
        Glide.with(HouseFormActivity.this).load(imageFile).into(ivImage);
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        // first clear out all the last img
        img_models_list.clear();

        imgs_uri = uriList;
        for(int i=0;i < uriList.size();i++){

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriList.get(i));
                img_models_list.add(new ImgFormModel(bitmap));
            }catch(Exception e){
                Log.e("Bitamp : " ,e.toString());
            }
        }

        // refresh and set new adapter
        img_adapter = new ImgFormAdapter(img_models_list, this);
        img_adapter.notifyDataSetChanged();
        recyclerView.setAdapter(img_adapter);
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
