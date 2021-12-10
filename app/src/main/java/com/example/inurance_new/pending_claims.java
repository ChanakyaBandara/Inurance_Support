package com.example.inurance_new;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class pending_claims extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Accident> ACClist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_claims);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_accident);
        Query query = FirebaseDatabase.getInstance().getReference().child("Accident")
                .orderByChild("nic").equalTo(dashboard.NIC);

        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                //Toast.makeText(dashboard.this, "Test Query", Toast.LENGTH_LONG).show();
                Log.d("ABC",dataSnapshot.toString());
                ACClist.clear();
                List<String> Keys = new ArrayList<String>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Log.d("ABC",keyNode.toString());
                    Log.d("ABC",keyNode.getKey());
                    if(!keyNode.child("states").getValue(String.class).equals("Completed")){
                        Accident accident = new Accident();
                        accident.setWID(keyNode.child("wid").getValue(String.class));
                        accident.setTxtdate(keyNode.child("txtdate").getValue(String.class));
                        accident.setDes(keyNode.child("estimate").getValue(String.class));
                        accident.setStates(keyNode.child("states").getValue(String.class));
                        ACClist.add(accident);
                        Keys.add(keyNode.getKey());
                    }
                }
                if(ACClist.isEmpty()){
                    Toast.makeText(pending_claims.this, "No Records", Toast.LENGTH_LONG).show();
                }else {
                    new Recycleview_accident_config().setConfig(recyclerView,pending_claims.this,ACClist,Keys);
                }
            }else{
                Toast.makeText(pending_claims.this, "No Records", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
