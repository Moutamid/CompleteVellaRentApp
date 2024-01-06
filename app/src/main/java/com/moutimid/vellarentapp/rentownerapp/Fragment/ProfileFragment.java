package com.moutimid.vellarentapp.rentownerapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.activities.Authentication.LoginActivity;
import com.moutimid.vellarentapp.rentownerapp.helper.Constants;
import com.moutimid.vellarentapp.vellarentappadmin.model.Owner;

public class ProfileFragment extends Fragment {
    TextView name, dob, email, number;
    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = view.findViewById(R.id.logout_btn);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        number = view.findViewById(R.id.number);
        Constants.databaseReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("data", snapshot.getValue() + " values");
                Owner userNew = snapshot.getValue(Owner.class);

                name.setText(userNew.getName());
                email.setText(userNew.getEmail());
                number.setText(userNew.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stash.clearAll();
                FirebaseAuth.getInstance().signOut();
                getActivity().startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finishAffinity();
            }
        });

        return view;
    }


}