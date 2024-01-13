package com.moutimid.vellarentapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.moutamid.vellarentapp.R;
import com.moutimid.vellarentapp.adapter.AllVillaAdapter;
import com.moutimid.vellarentapp.adapter.FavouriteVillaAdapter;
import com.moutimid.vellarentapp.helper.Config;
import com.moutimid.vellarentapp.model.Villa;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    RecyclerView content_rcv;

    public List<Villa> productModelList = new ArrayList<>();
    FavouriteVillaAdapter retaurantAdapter;
    TextView no_text;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favourite, container, false);
        content_rcv = view.findViewById(R.id.content_rcv);
        no_text = view.findViewById(R.id.no_text);
        content_rcv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ArrayList<Villa> VillaArrayList = Stash.getArrayList(Config.favourite, Villa.class);
        retaurantAdapter = new FavouriteVillaAdapter(getContext(), VillaArrayList);
        content_rcv.setAdapter(retaurantAdapter);
        retaurantAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<Villa> VillaArrayList = Stash.getArrayList(Config.favourite, Villa.class);
        retaurantAdapter = new FavouriteVillaAdapter(getContext(), VillaArrayList);
        content_rcv.setAdapter(retaurantAdapter);
        retaurantAdapter.notifyDataSetChanged();

    }
}