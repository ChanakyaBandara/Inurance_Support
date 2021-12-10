package com.example.inurance_new;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Recycleview_vehicle_config {
    private Context mContext;
    private VehicleAddaptor mVehicleAddaptor;


    public void setConfig(RecyclerView recyclerView, Context context, List<Vehicle> vehicles, List<String> keys){
        mContext = context;
        mVehicleAddaptor = new VehicleAddaptor(vehicles,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mVehicleAddaptor);
    }

    class VehicleItemView extends RecyclerView.ViewHolder{


        private TextView Vehicle_name;
        private TextView Vehicle_no;
        private ImageView Vehicle_Img;


        private String key;

        public VehicleItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.assets_item,parent,false));

            Vehicle_name = (TextView) itemView.findViewById(R.id.vehicle_Name);
            Vehicle_no = (TextView) itemView.findViewById(R.id.vehicle_No);
            Vehicle_Img = (ImageView) itemView.findViewById(R.id.vehicle_Img);


        }

        public void bind( Vehicle  vehicle,String key){
            Vehicle_name.setText(vehicle.getVehicle_make() + " - " + vehicle.getVehicle_model());
            Vehicle_no.setText(vehicle.getVehicle_no());
            Glide.clear(Vehicle_Img);
            Glide.with(mContext).load(vehicle.getImgURL()).into(Vehicle_Img);
            this.key = key;
        }



    }

    class VehicleAddaptor extends RecyclerView.Adapter<VehicleItemView>{
        private List<Vehicle> mVehicle;
        private List<String> mKey;

        public VehicleAddaptor(List<Vehicle> mVehicle, List<String> mKey) {
            this.mVehicle = mVehicle;
            this.mKey = mKey;
        }

        @NonNull
        @Override
        public VehicleItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VehicleItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull VehicleItemView holder, int position) {
            holder.bind(mVehicle.get(position),mKey.get(position));
        }

        @Override
        public int getItemCount() {
            return mVehicle.size();
        }


    }

}
