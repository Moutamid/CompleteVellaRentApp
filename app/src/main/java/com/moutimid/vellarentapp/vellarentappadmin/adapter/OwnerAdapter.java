package com.moutimid.vellarentapp.vellarentappadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.helper.Constants;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.showProgressDialog(context);
                Constants.auth().signInWithEmailAndPassword(
                        owner.email,
                        owner.password
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Config.dismissProgressDialog();
                        showCustomDialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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

    private void showCustomDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editText);
        Button btnDismiss = dialogView.findViewById(R.id.btnDismiss);
        Button btnGetData = dialogView.findViewById(R.id.btnGetData);

        final AlertDialog alertDialog = dialogBuilder.create();

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.showProgressDialog(context);
                String enteredText = editText.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    if (editText.getText().toString() != null) {
                        String newPassword = enteredText; // Replace with the new password

                        user.updatePassword(newPassword)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Config.dismissProgressDialog();
                                        // Password updated successfully
                                        Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss(); // Dismiss the dialog after getting data

                                    } else {
                                        // Failed to update password
                                        Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
                else
                {
                    editText.setError("Please enter");
                }
            }

        });

        alertDialog.show();
    }
}
