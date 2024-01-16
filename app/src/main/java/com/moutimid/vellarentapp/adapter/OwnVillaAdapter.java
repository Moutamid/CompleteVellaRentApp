package com.moutimid.vellarentapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.activities.Home.VillaDetailsActivity;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.model.Villa;

import java.util.ArrayList;
import java.util.List;

public class OwnVillaAdapter extends RecyclerView.Adapter<OwnVillaAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    List<Villa> productModels;
    private static final double EARTH_RADIUS = 6371;


    public OwnVillaAdapter(Context ctx, List<Villa> productModels) {
        this.ctx = ctx;
        this.productModels = productModels;
    }


    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.all_villa, parent, false);
        return new GalleryPhotosViewHolder(view);
    }

    public void filterList(ArrayList<Villa> filterlist) {
        productModels = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        Villa villa = productModels.get(position);

        double distance = calculateDistance(Config.lat, Config.lng, villa.getLat(), villa.getLng());
        Log.d("distance", distance + "  distance");
//        if (distance < 10) {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.villa_name.setText(villa.getName());
            holder.villa_location.setText(villa.getTitle());
            holder.bill.setText("$" + villa.getBill() + "");
            holder.avaialble_for.setText(villa.getBedroom() + " rooms left here");
            Glide.with(ctx).load(villa.getImage()).into(holder.image);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (villa.available.equals("available")) {
                    Stash.put(Config.currentModel, villa);
                    String.format("%.2f ", villa.distance);
                    Stash.put("distance", villa);
                    Stash.put("onetime", true);
                    ctx.startActivity(new Intent(ctx, VillaDetailsActivity.class));
                } else {
                    Toast.makeText(ctx, "Villa is not available yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView villa_location, villa_name, avaialble_for, bill;
        ImageView image;
        CardView cardView;

        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            villa_location = itemView.findViewById(R.id.distance);
            villa_name = itemView.findViewById(R.id.villa_name);
            bill = itemView.findViewById(R.id.bill);
            image = itemView.findViewById(R.id.image_bg);
            avaialble_for = itemView.findViewById(R.id.avaialble_for);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;

        return distance;
    }

}
