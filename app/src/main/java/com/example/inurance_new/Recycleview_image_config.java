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

public class Recycleview_image_config {
    private Context mContext;
    private ImageAddaptor mImageAddaptor;


    public void setConfig(RecyclerView recyclerView, Context context, List<String> image, List<String> keys){
        mContext = context;
        mImageAddaptor = new ImageAddaptor(image,keys);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mImageAddaptor);
    }

    class ImageItemView extends RecyclerView.ViewHolder{

        private ImageView Vehicle_Img;


        private String key;

        public ImageItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.pic_item,parent,false));

            Vehicle_Img = (ImageView) itemView.findViewById(R.id.pic_image);
        }

        public void bind(String img,String key){
            Glide.clear(Vehicle_Img);
            Glide.with(mContext).load(img).into(Vehicle_Img);
            this.key = key;
        }



    }

    class ImageAddaptor extends RecyclerView.Adapter<ImageItemView>{
        private List<String> mImage;
        private List<String> mKey;

        public ImageAddaptor(List<String> mString, List<String> mKey) {
            this.mImage = mString;
            this.mKey = mKey;
        }

        @NonNull
        @Override
        public ImageItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ImageItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageItemView holder, int position) {
            holder.bind(mImage.get(position),mKey.get(position));
        }


        @Override
        public int getItemCount() {
            return mImage.size();
        }


    }

}
