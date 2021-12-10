package com.example.inurance_new;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class my_assets extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Vehicle> Vehiclelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_assets);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_vehicle);
        Query query = FirebaseDatabase.getInstance().getReference().child("Vehicle")
                .orderByChild("NIC").equalTo(dashboard.NIC);

        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                //Toast.makeText(dashboard.this, "Test Query", Toast.LENGTH_LONG).show();
                Log.d("ABC",dataSnapshot.toString());
                Vehiclelist.clear();
                List<String> Keys = new ArrayList<String>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Log.d("ABC",keyNode.toString());
                    Log.d("ABC",keyNode.getKey());
                    Vehicle vehicle = new Vehicle();
                    vehicle.setCustomer_Name(keyNode.child("Customer_Name").getValue(String.class));
                    vehicle.setNIC(keyNode.child("NIC").getValue(String.class));
                    vehicle.setVehicle_no(keyNode.child("Vehicle_no").getValue(String.class));
                    vehicle.setVehicle_make(keyNode.child("Vehicle_make").getValue(String.class));
                    vehicle.setVehicle_model(keyNode.child("Vehicle_model").getValue(String.class));
                    vehicle.setVehicle_engine_No(keyNode.child("Vehicle_engine_No").getValue(String.class));
                    vehicle.setVehicle_chassis_No(keyNode.child("Vehicle_chassis_No").getValue(String.class));
                    vehicle.setImgURL(keyNode.child("ImgURL").getValue(String.class));
                    Vehiclelist.add(vehicle);
                    Keys.add(keyNode.getKey());
                    //String temp = keyNode.child("Customer_phone").getValue(String.class);
                    //Log.d("ABC",temp);
                }
                new Recycleview_vehicle_config().setConfig(recyclerView,my_assets.this,Vehiclelist,Keys);
            }else{
                Toast.makeText(my_assets.this, "Test Query1", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
