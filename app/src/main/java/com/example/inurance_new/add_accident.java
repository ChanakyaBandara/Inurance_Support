package com.example.inurance_new;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class add_accident extends AppCompatActivity implements GoogleMap.OnMarkerClickListener{

    private List<String> dataList;
    private List<String> Keys;
    private List<String> Keys_img;
    private String Selected_Category;
    private AppCompatSpinner spinnerType;
    private EditText etxtDate, etxtDec, etxtLat, etxtLan;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String profileImageUrl, userId;
    private Uri resultUri;
    private DatePickerDialog picker;
    private RecyclerView recyclerView;
    private View lyImage;
    private ProgressDialog progressDialog;
    private ScrollView mScrollView;

    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    private List<String> imagesEncodedList;
    private ArrayList<Uri> ImageList;

    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    private Button confrmbtn;
    Location lastLocationclnew;
    Marker marker;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String TAG = "Mapactivity";
    private Boolean mLocationpermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEAFAULT_ZOOM = 15f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accident);

        getLocationPermission();
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view_1); //parent scrollview in xml, give your scrollview id value

        dataList = new ArrayList<String>();
        Keys = new ArrayList<String>();
        Keys_img = new ArrayList<String>();
        imagesEncodedList = new ArrayList<String>();
        ImageList = new ArrayList<Uri>();
        Selected_Category = "1";
        spinnerType = (AppCompatSpinner) findViewById(R.id.vehicleDrop);
        spinnerType.setPrompt("Choose Vehicle");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading ..........");

        etxtDate = (EditText) findViewById(R.id.editDate);
        etxtDec = (EditText) findViewById(R.id.editDec);
        etxtLat = (EditText) findViewById(R.id.editLat);
        etxtLan = (EditText) findViewById(R.id.editLan);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_images);
        lyImage = (LinearLayout) findViewById(R.id.lyImage);

        etxtDate.setInputType(InputType.TYPE_NULL);
        etxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(add_accident.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etxtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Accident");

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Selected_Category = Keys.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Selected_Category = "1";
            }
        });

        Query query = FirebaseDatabase.getInstance().getReference().child("Vehicle")
                .orderByChild("NIC").equalTo(dashboard.NIC);

        query.addListenerForSingleValueEvent(valueEventListener);

        new Recycleview_image_config().setConfig(recyclerView, add_accident.this, imagesEncodedList, Keys_img);
        String imageUri = "drawable://" + R.drawable.add_image_icon;
        Uri path = Uri.parse("android.resource://com.example.inurance_new/" + R.drawable.add_image_icon);
        imagesEncodedList.add(path.toString());
        Keys_img.add("empty");

        lyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
    }

    private void loadSpinnerData() {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataList);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerType.setAdapter(dataAdapter);
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                //Toast.makeText(dashboard.this, "Test Query", Toast.LENGTH_LONG).show();
                Log.d("ABC", dataSnapshot.toString());
                dataList.clear();
                Keys.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Log.d("ABC", keyNode.toString());
                    Log.d("ABC", keyNode.getKey());
                    dataList.add(keyNode.child("Vehicle_no").getValue(String.class));
                    Keys.add(keyNode.getKey());
                    //String temp = keyNode.child("Customer_phone").getValue(String.class);
                    //Log.d("ABC",temp);
                }
                loadSpinnerData();
            } else {
                Toast.makeText(add_accident.this, "No Data", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public void AddRec(View view) {
        //etxtDate,etxtDec,etxtLat,etxtLan
        String txtdate = etxtDate.getText().toString();
        String des = etxtDec.getText().toString();
        String lat = etxtLat.getText().toString();
        String lan = etxtLan.getText().toString();
        final Accident accident = new Accident();
        accident.setWID(Selected_Category);
        accident.setNIC(dashboard.NIC);
        accident.setDes(des);
        accident.setTxtdate(txtdate);
        accident.setLat(lat);
        accident.setLan(lan);
        final DatabaseReference newPostRef = mUserDatabase.push();
        newPostRef.setValue(accident);
//        if(resultUri != null){
//            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Accident").child(UUID.randomUUID().toString());
//            Bitmap bitmap = null;
//
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
//            byte[] data = baos.toByteArray();
//            UploadTask uploadTask = filepath.putBytes(data);
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    finish();
//                }
//            });
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {    //Errorrr Handleee
//                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            accident.setImageUrl(uri.toString());
//                            DatabaseReference newPostRef = mUserDatabase.push();
//                            newPostRef.setValue(accident);
//                            Toast.makeText(add_accident.this, "Record Added !", Toast.LENGTH_LONG).show();
//                            finish();
//                            return;
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            finish();
//                            return;
//                        }
//                    });
//                }
//            });
//        }else{
//            finish();
//        }
        progressDialog.show();
        final StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Accident");
        for (int uploads = 0; uploads < ImageList.size(); uploads++) {
            final StorageReference imagename = ImageFolder.child(UUID.randomUUID().toString());

            imagename.putFile(ImageList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = String.valueOf(uri);
                            newPostRef.child("images").child(UUID.randomUUID().toString()).setValue(url);
                            progressDialog.dismiss();
                        }
                    });

                }
            });
        }

        Toast.makeText(add_accident.this, "Record Added !", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //if(requestCode == 1 && resultCode == Activity.RESULT_OK){
        //final Uri imageUri = data.getData();
        //resultUri = imageUri;
        //mProfileImage.setImageURI(resultUri);
        //}
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                imagesEncodedList.clear();
                ImageList.clear();
                Keys_img.clear();
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();
                    ImageList.add(mImageUri);
                    imagesEncodedList.add(mImageUri.toString());
                    Keys_img.add("1");

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            ImageList.add(uri);
                            imagesEncodedList.add(uri.toString());
                            Keys_img.add(String.valueOf(i));
                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
                new Recycleview_image_config().setConfig(recyclerView, add_accident.this, imagesEncodedList, Keys_img);
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getDeviceLoctation() {
        Log.d(TAG, "getDeviceLocation:getting current Location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationpermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete:Found Location");
                            Location currentLocation = (Location) task.getResult();
                            lastLocationclnew = currentLocation;
                            assert currentLocation != null;
                            double lat = currentLocation.getLatitude();
                            double lan = currentLocation.getLongitude();
                            moveCamera(new LatLng(lat, lan),
                                    DEAFAULT_ZOOM);
                            etxtLat.setText(String.valueOf(lat));
                            etxtLan.setText(String.valueOf(lan));

                        } else {
                            Log.d(TAG, "onComplete:current location is null");
                            Toast.makeText(add_accident.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }


                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation:SecurityException: " + e.getMessage());

        }

    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera:moving the camera to:lat:" + latLng.latitude + ",lng:" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap() {
        Log.d(TAG, "initMap:initializingMap");
//        final SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
//        assert mapFragment != null;
//        mapFragment.getMapAsync(EnterPark.this);


        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                //.findFragmentById(R.id.google_map);
        //assert mapFragment != null;
        //mapFragment.getMapAsync(this);

        // check if we have got the googleMap already
        if (mMap == null) {
            SupportMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {
                    mMap = googleMap;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    if (mLocationpermissionGranted) {
                        getDeviceLoctation();
                        if (ActivityCompat.checkSelfPermission(add_accident.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(add_accident.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                            @Override
                            public void onCameraIdle() {
                                //get latlng at the center by calling
                                LatLng midLatLng = mMap.getCameraPosition().target;
                                etxtLat.setText(String.valueOf(midLatLng.latitude));
                                etxtLan.setText(String.valueOf(midLatLng.longitude));
                            }
                        });
                    }

                    mScrollView = findViewById(R.id.scroll_view_1); //parent scrollview in xml, give your scrollview id value
                    ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map))
                            .setListener(new WorkaroundMapFragment.OnTouchListener() {
                                @Override
                                public void onTouch()
                                {
                                    mScrollView.requestDisallowInterceptTouchEvent(true);
                                }
                            });
                }
            });
        }

    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission:getting Location Permission");
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
        //     Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            mLocationpermissionGranted = true;
            initMap();
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "OnRequestPermissionResult:called");
        //mLocationpermissionGranted=false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationpermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionResult: permissionFailed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionResult: permission granted");
                    mLocationpermissionGranted = true;
                    initMap();

                }


            }
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }




}
