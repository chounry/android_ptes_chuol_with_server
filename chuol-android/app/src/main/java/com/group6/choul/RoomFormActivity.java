package com.group6.choul;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import com.group6.choul.adapters.ImgFormAdapter;
import com.group6.choul.login_register_handling.TokenManager;
import com.group6.choul.models.ImgFormModel;
import com.group6.choul.shares.MyConfig;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomFormActivity extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate{
    private ImageButton map_imgBtn;
    private LinearLayout upload_img_btn;
    private RecyclerView recyclerView;
    private Button service_btn,submit_btn;
    private Toolbar myToolbar;
    private Switch contact_swtich;
    private EditText title_et, price_et, description_et, phone_et, phone_option_et, address_et,size_r;
    private String title, price, description, phone, phone_opt, address,size,city,currency,duration;
    private Spinner cityspinner,currency_spinner,duration_spinner;
    private CheckBox checkBoxTerm;
    private static final String TAG = "RoomFromActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;


    private static final int STORAGE_PERMISSION_CODE = 123;
    private String[] listItems = {"Free Wifi","Available Parking Space"};
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>(); // store the index of 'listItem' that use have selected
    private List<ImgFormModel> img_models_list;
    private RecyclerView.Adapter img_adapter;
    private List<Uri> imgs_uri;
    private List<String> service_aval;
    private double lat,lng;
    private TokenManager tokenManager;
    private int user_id;
    private TextInputLayout priceTil;

    private String upload_url = MyConfig.SERVE_ADDRESS + "/api/rooms/create";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_form);

        map_imgBtn = findViewById(R.id.map_imgBtn);
        recyclerView = findViewById(R.id.img_recyler_view);
        submit_btn = findViewById(R.id.submit_btn);
        service_btn = findViewById(R.id.select_service_btn);
        upload_img_btn = findViewById(R.id.upload_img_btn);
        contact_swtich = findViewById(R.id.contact_switch);
        price_et = findViewById(R.id.price_et);
        currency_spinner = findViewById(R.id.currency_sp);
        duration_spinner = findViewById(R.id.duration_sp);
        phone_option_et = findViewById(R.id.phone_opt_et);
        title_et = findViewById(R.id.title_et);
        price_et = findViewById(R.id.price_et);
        description_et = findViewById(R.id.description_et);
        address_et = findViewById(R.id.address_et);
        phone_et = findViewById(R.id.phone_et);
        phone_option_et = findViewById(R.id.phone_opt_et);
        size_r = findViewById(R.id.size_et);
        cityspinner = findViewById(R.id.city_spinner);
        checkBoxTerm = findViewById(R.id.ckboxTerm);
        priceTil = findViewById(R.id.price_til);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
        user_id = tokenManager.getUserId();

        checkBoxTerm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    checkBoxTerm.setTag("Y");
//
//                }
                if(isChecked==false){
                    checkBoxTerm.setTag("N");
                    Toast.makeText(RoomFormActivity.this, "You can't make map requests", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // <------- handle toolbar
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        showActionBar();
        // <------- handle toolbar

        service_aval = new ArrayList<>();

        contact_swtich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceTil.setVisibility(View.VISIBLE);
                if(contact_swtich.isChecked()){
                    priceTil.setVisibility(View.GONE);
                }
            }
        });

        //<------------- Init img selection
        upload_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BSImagePicker multiSelectionPicker = new BSImagePicker.Builder("com.yourdomain.yourpackage.fileprovider")
                        .isMultiSelect() //Set this if you want to use multi selection mode.
                        .setMinimumMultiSelectCount(2) //Default: 2
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

        // submit form
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = title_et.getText().toString();
                price = price_et.getText().toString();
                description = description_et.getText().toString();
                phone = phone_et.getText().toString();
                phone_opt = phone_option_et.getText().toString();
                address = address_et.getText().toString();
                size = size_r.getText().toString();
                city = cityspinner.getSelectedItem().toString();
                currency = currency_spinner.getSelectedItem().toString();
                duration = duration_spinner.getSelectedItem().toString();


                if(!title.isEmpty() && !price.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !size.isEmpty() &&
                        !city.isEmpty() && checkBoxTerm.isChecked()){
                    uploadMultipart(imgs_uri);
                }

            }
        });



        // handle multiple select services
        checkedItems = new boolean[listItems.length]; // var for multiple select
        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RoomFormActivity.this);
                mBuilder.setTitle("Select All Available Services");

                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){

                            mUserItems.add(position);
                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        service_aval.clear();
                        for (int i = 0; i < mUserItems.size(); i++) {
                            service_aval.add(listItems[mUserItems.get(i)]);
                        }
//                        mItemSelected.setText(item);
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();

                mDialog.show();
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
                lat = data.getDoubleExtra("lat",10);
                lng = data.getDoubleExtra("lng",10);
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

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(RoomFormActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occure but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(RoomFormActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private void goBack(){
        startActivity(new Intent(RoomFormActivity.this, MainActivity.class));
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
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            MultipartUploadRequest mUploadRequest = new MultipartUploadRequest(this, uploadId, upload_url)
                    .addParameter("title", title) //Adding text parameter to the request
                    .addParameter("price", price)
                    .addParameter("description", description)
                    .addParameter("phone", phone)
                    .addParameter("address", address)
                    .addParameter("phone_option",phone_opt)
                    .addParameter("size",size)
                    .addParameter("city",city)
                    .addParameter("lat",lat+"")
                    .addParameter("lng",lng+"")
                    .addParameter("currency",currency)
                    .addParameter("duration",duration)
                    .addParameter("user_id",user_id+"")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2); // try request at least 2 time before give up

            for(int i = 0;i < service_aval.size();i++){
                // add many imgs to the request
                mUploadRequest.addParameter( "services"+"["+i+"]",service_aval.get(i));
            }


            for(int i = 0;i < filePath.size();i++){
                // add many imgs to the request
                String path = filePath.get(i).getPath();
                mUploadRequest.addFileToUpload(path, "imgs"+"["+i+"]");
            }

            mUploadRequest.startUpload();

            Thread.sleep(2000);
            startActivity(new Intent(this,MainActivity.class));
            finish();
        } catch (Exception exc) {
            Toast.makeText(this,"Multipart Error" + exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // image selection handling
    @Override
    public void loadImage(File imageFile, ImageView ivImage) {
        Glide.with(RoomFormActivity.this).load(imageFile).into(ivImage);
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        Log.e("test select img", uriList.size()+"");
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
