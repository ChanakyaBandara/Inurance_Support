package com.example.inurance_new;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class dashboard extends AppCompatActivity {

    public static String NIC;

    private FirebaseAuth mAuth;

    private String currentUId;

    private DatabaseReference usersDb;

    private TextView Username;
    private TextView NIC_no;
    private ImageView ProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Username = (TextView) findViewById(R.id.Username);
        NIC_no = (TextView) findViewById(R.id.nic_no);
        ProfileImage = (ImageView) findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        //Toast.makeText(dashboard.this, currentUId, Toast.LENGTH_LONG).show();

        Query query = FirebaseDatabase.getInstance().getReference().child("Customer")
                .orderByChild("UID")
                .equalTo(currentUId);

        query.addListenerForSingleValueEvent(valueEventListener);



    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                //Toast.makeText(dashboard.this, "Test Query", Toast.LENGTH_LONG).show();
                Log.d("ABC",dataSnapshot.toString());
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Username.setText(keyNode.child("contact_Full_name").getValue(String.class));
                    NIC_no.setText(keyNode.child("Customer_id").getValue(String.class));
                    NIC = keyNode.child("Customer_id").getValue(String.class);
                    Glide.clear(ProfileImage);
                    String profileImageUrl = keyNode.child("ImgURL").getValue(String.class);
                    switch(profileImageUrl){
                        case "default":
                            Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(ProfileImage);
                            break;
                        default:
                            Glide.with(getApplication()).load(profileImageUrl).into(ProfileImage);
                            break;
                    }
                    //String temp = keyNode.child("Customer_phone").getValue(String.class);
                    //Log.d("ABC",temp);
                }
            }else{
                Toast.makeText(dashboard.this, "No data Found", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void goTo_report_accident(View view) {
        Intent intent = new Intent(dashboard.this, add_accident.class);
        startActivity(intent);
    }

    public void goTo_pending_claims(View view) {
        Intent intent = new Intent(dashboard.this, pending_claims.class);
        startActivity(intent);
    }

    public void goTo_claim_history(View view) {
        Intent intent = new Intent(dashboard.this, claim_history.class);
        startActivity(intent);
    }

    public void goTo_my_assets(View view) {
        Intent intent = new Intent(dashboard.this, my_assets.class);
        startActivity(intent);
    }

    public void goTo_emergancy(View view) {
        Intent intent = new Intent(dashboard.this, Emergancy.class);
        startActivity(intent);
    }
}
