package com.moutimid.vellarentapp.vellarentappadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.vellarentappadmin.activities.VillaDetailsActivity;
import com.moutimid.vellarentapp.vellarentappadmin.helper.Config;
import com.moutimid.vellarentapp.vellarentappadmin.model.Villa;

import java.util.ArrayList;
import java.util.List;

public class OwnVillaAdapter extends RecyclerView.Adapter<OwnVillaAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    List<com.moutimid.vellarentapp.model.Villa> productModels;

    public OwnVillaAdapter(Context ctx, List<com.moutimid.vellarentapp.model.Villa> productModels) {
        this.ctx = ctx;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.own_villa_, parent, false);
        return new GalleryPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        com.moutimid.vellarentapp.model.Villa villa = productModels.get(position);
        holder.villa_name.setText(villa.getName());
        holder.villa_bill.setText("$"+villa.getBill()+"/month");
        holder.villa_location.setText(villa.getTitle());
        Glide.with(ctx).load(villa.getImage()).into(holder.image);
        if(villa.isBills_included())
        {
            holder.villa_bill_included.setText("Bill included");
        }
        else
        {holder.villa_bill_included.setText("Bill not included");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Stash.put(Config.currentModel, villa);
                    String.format("%.2f ", villa.distance);
                    Stash.put("distance",villa);
                Stash.put("onetime", true);

                ctx.startActivity(new Intent(ctx, VillaDetailsActivity.class));
              }
        });

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView villa_location, villa_name, villa_bill, villa_bill_included;
        ImageView image;


        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            villa_location = itemView.findViewById(R.id.villa_location);
            villa_name = itemView.findViewById(R.id.villa_name);
            villa_bill = itemView.findViewById(R.id.villa_bill);
            image = itemView.findViewById(R.id.image);
            villa_bill_included = itemView.findViewById(R.id.villa_bill_included);
//
        }
    }
}
