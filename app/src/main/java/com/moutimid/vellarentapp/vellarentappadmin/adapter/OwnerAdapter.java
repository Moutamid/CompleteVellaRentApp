package com.moutimid.vellarentapp.vellarentappadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.vellarentappadmin.model.Owner;
import com.moutimid.vellarentapp.vellarentappadmin.model.UserModel;

import java.util.List;

public class OwnerAdapter extends RecyclerView.Adapter<OwnerAdapter.OwnerViewHolder> {

    private List<UserModel> owners;
    Context context;

    public void setOwners(List<UserModel> owners, Context context) {
        this.owners = owners;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OwnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_owner, parent, false);
        return new OwnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerViewHolder holder, int position) {
        UserModel owner = owners.get(position);
        holder.bind(owner);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(context, AllVillaActivity.class);
//                Stash.put("id", owner.getOwnerId());
//                Stash.put("name", owner.getName());
//                Stash.put("image", owner.getImage());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return owners != null ? owners.size() : 0;
    }

    static class OwnerViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;
        private final TextView textViewPhone;
        private final TextView textViewEmail;

        public OwnerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
        }

        public void bind(UserModel owner) {
            textViewName.setText("Name: " + owner.getName());
            textViewEmail.setText("Email: " + owner.getEmail());
        }
    }
}
