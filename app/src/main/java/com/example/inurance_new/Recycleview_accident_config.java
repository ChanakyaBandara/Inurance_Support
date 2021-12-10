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

public class Recycleview_accident_config {
    private Context mContext;
    private VehicleAddaptor mAccidentAddaptor;


    public void setConfig(RecyclerView recyclerView, Context context, List<Accident> vehicles, List<String> keys){
        mContext = context;
        mAccidentAddaptor = new VehicleAddaptor(vehicles,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAccidentAddaptor);
    }

    class AccidentItemView extends RecyclerView.ViewHolder{


        private TextView Acc_name;
        private TextView Acc_Date;
        private TextView Acc_sts;
        private TextView Acc_Des;


        private String key;

        public AccidentItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.accitem,parent,false));

            Acc_name = (TextView) itemView.findViewById(R.id.accName);
            Acc_Date = (TextView) itemView.findViewById(R.id.accDate);
            Acc_sts = (TextView) itemView.findViewById(R.id.accstates);
            Acc_Des = (TextView) itemView.findViewById(R.id.accDes);

        }

        public void bind( Accident  accident,String key){
            Acc_name.setText(accident.getWID());
            Acc_Date.setText(accident.getTxtdate());
            Acc_sts.setText(accident.getStates());
            Acc_Des.setText(accident.getDes());
            this.key = key;
        }



    }

    class VehicleAddaptor extends RecyclerView.Adapter<AccidentItemView>{
        private List<Accident> mAccident;
        private List<String> mKey;

        public VehicleAddaptor(List<Accident> mAccident, List<String> mKey) {
            this.mAccident = mAccident;
            this.mKey = mKey;
        }

        @NonNull
        @Override
        public AccidentItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AccidentItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AccidentItemView holder, int position) {
            holder.bind(mAccident.get(position),mKey.get(position));
        }

        @Override
        public int getItemCount() {
            return mAccident.size();
        }


    }

}
