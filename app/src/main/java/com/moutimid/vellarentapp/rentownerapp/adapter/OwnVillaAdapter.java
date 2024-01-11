package com.moutimid.vellarentapp.rentownerapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.rentownerapp.activities.Home.VillaDetailsActivity;
import com.moutimid.vellarentapp.rentownerapp.dailogues.CalenderDialogClass;
import com.moutimid.vellarentapp.rentownerapp.helper.Config;
import com.moutimid.vellarentapp.rentownerapp.model.Villa;

import java.util.ArrayList;
import java.util.List;

public class OwnVillaAdapter extends RecyclerView.Adapter<OwnVillaAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    List<Villa> productModels;
    Activity activity;

    public OwnVillaAdapter(Activity activity, Context ctx, List<Villa> productModels) {
        this.ctx = ctx;
        this.productModels = productModels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.own_villa_item, parent, false);
        return new GalleryPhotosViewHolder(view);
    }

    public void filterList(ArrayList<Villa> filterlist) {
        productModels = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        Villa villa = productModels.get(position);
        holder.villa_name.setText(villa.getName());
        Log.d("dataa", villa.getAvailable() + "  dtaa");

        if (villa.verified) {
            holder.verified.setText("Verified");
            holder.verified.setTextColor(ctx.getResources().getColor(R.color.green_color));

        } else {
            holder.verified.setText("Not Verified");
            holder.verified.setTextColor(ctx.getResources().getColor(R.color.red));

        }
        holder.available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference propertyRef = database.getReference("RentApp").child("Villas");
                propertyRef.child(villa.getKey()).child("available").setValue("available");
                CalenderDialogClass calenderDialogClass = new CalenderDialogClass(activity, villa.getKey(), villa.available_dates);
                calenderDialogClass.show();

            }
        });


//        holder.available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    Stash.put(Config.currentModel, villa);
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    Villa villaModel = (Villa) Stash.getObject(Config.currentModel, Villa.class);
//                    DatabaseReference propertyRef = database.getReference("RentApp").child("Villas");
//                    propertyRef.child(villaModel.getKey()).child("available").setValue("available");
//                    CalenderDialogClass calenderDialogClass = new CalenderDialogClass(activity);
//                    calenderDialogClass.show();
//
//                }
//                else
//                {
//                    holder.not_available.setChecked(true);
//                    Stash.put(Config.currentModel, villa);
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    Villa villaModel = (Villa) Stash.getObject(Config.currentModel, Villa.class);
//                    DatabaseReference propertyRef = database.getReference("RentApp").child("Villas");
//                    propertyRef.child(villaModel.getKey()).child("available").setValue("not_available");
//                }
//            }
//        });
        holder.not_available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    holder.not_available.setChecked(true);
                    Stash.put(Config.currentModel, villa);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference propertyRef = database.getReference("RentApp").child("Villas");
                    propertyRef.child(villa.getKey()).child("available").setValue("not_available".toString());

                }
                if(!b)
                {
                    Stash.put(Config.currentModel, villa);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference propertyRef = database.getReference("RentApp").child("Villas");
                    propertyRef.child(villa.getKey()).child("available").setValue("available");
//                    CalenderDialogClass calenderDialogClass = new CalenderDialogClass(activity, villa.getKey(), villa.available_dates);
//                    calenderDialogClass.show();

                }

            }
        });
        holder.avaialble_for.setText(villa.getBedroom()+ " rooms left here");

        if (villa.getAvailable().equals("not_available")) {
            holder.not_available.setChecked(true);
        } else {
            holder.available.setChecked(true);
        }
        Glide.with(ctx).load(villa.getImage()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stash.put(Config.currentModel, villa);
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

        TextView villa_name, verified, avaialble_for;
        ImageView image;

        RadioButton not_available, available;

        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            villa_name = itemView.findViewById(R.id.villa_name);
            image = itemView.findViewById(R.id.image);
            not_available = itemView.findViewById(R.id.not_available);
            available = itemView.findViewById(R.id.available);
            verified = itemView.findViewById(R.id.verified);
            avaialble_for = itemView.findViewById(R.id.avaialble_for);
        }
    }
}
