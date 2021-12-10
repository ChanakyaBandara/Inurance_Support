package com.example.inurance_new;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Map extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    //Map
    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    private Button confrmbtn;
    Location lastLocationclnew;
    Marker marker;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION= Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE=1234;
    private static final String TAG ="Mapactivity";
    private Boolean mLocationpermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEAFAULT_ZOOM =15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getLocationPermission();
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
    }



    private void getDeviceLoctation(){
        Log.d(TAG,"getDeviceLocation:getting current Location");
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationpermissionGranted){
                Task location=mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener(){
                    public void onComplete(@NonNull Task task){
                        if(task.isSuccessful()){
                            Log.d(TAG,"onComplete:Found Location");
                            Location currentLocation=(Location)task.getResult();
                            lastLocationclnew=currentLocation;
                            assert currentLocation != null;
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEAFAULT_ZOOM);


                        }else{
                            Log.d(TAG,"onComplete:current location is null");
                            Toast.makeText(Map.this,"unable to get current location",Toast.LENGTH_SHORT).show();
                        }
                    }


                });
            }

        }catch(SecurityException e){
            Log.e(TAG,"getDeviceLocation:SecurityException: " +e.getMessage());

        }

    }

    private  void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG,"moveCamera:moving the camera to:lat:"+latLng.latitude+",lng:"+latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    private void initMap(){
        Log.d(TAG,"initMap:initializingMap");
//        final SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
//        assert mapFragment != null;
//        mapFragment.getMapAsync(EnterPark.this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    private  void getLocationPermission(){
        Log.d(TAG,"getLocationPermission:getting Location Permission");
        String[]permission = {Manifest.permission.ACCESS_FINE_LOCATION};
        //     Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            // if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            mLocationpermissionGranted=true;
            initMap();
        }
        else{
            ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"OnRequestPermissionResult:called");
        //mLocationpermissionGranted=false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 ){
                    for(int i=0; i< grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationpermissionGranted=false;
                            Log.d(TAG,"onRequestPermissionResult: permissionFailed");
                            return;
                        }
                    }
                    Log.d(TAG,"onRequestPermissionResult: permission granted");
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"Your Current Location",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onMapReady:map is ready");
        mMap = googleMap;
        if(mLocationpermissionGranted){
            getDeviceLoctation();
            mMap.setMyLocationEnabled(true);
        }
    }
}

