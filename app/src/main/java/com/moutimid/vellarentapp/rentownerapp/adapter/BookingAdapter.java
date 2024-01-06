package com.moutimid.vellarentapp.rentownerapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.rentownerapp.model.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.OwnerViewHolder> {

    private List<Booking> owners;
    Context context;

    public void setOwners(List<Booking> owners, Context context) {
        this.owners = owners;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OwnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new OwnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerViewHolder holder, int position) {
        Booking owner = owners.get(position);
        holder.bind(owner);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view1 = inflater.inflate(R.layout.alert_dialogue_layout, null);
                builder.setView(view1);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                        mDatabase.child("RentApp").child("Bookings").child(owner.getId());
                        mDatabase.child("verified").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                            }

                        });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle button click
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return owners != null ? owners.size() : 0;
    }

    static class OwnerViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;
        private final TextView textViewPhone;
        private final TextView textViewEmail;
        private final TextView textViewCredit;

        public OwnerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewCredit = itemView.findViewById(R.id.textViewCredit);
        }

        public void bind(Booking owner) {
            textViewName.setText("Name: " + owner.getCustomerName());
            textViewPhone.setText("Credit Card Number: " + owner.getCreditCardNumber());
            textViewEmail.setText("Date: " + owner.getDate());
            textViewCredit.setText("No of Persons: " + owner.getNumberOfPersons());
        }
    }
}
