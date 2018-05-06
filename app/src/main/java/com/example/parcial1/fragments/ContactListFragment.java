package com.example.parcial1.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parcial1.adapters.ContactAdapter;
import com.example.parcial1.R;

public class ContactListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        //Crea el layout para la lista, si es vertical lo hace con grid si es horizontal lo hace linear
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            GridLayoutManager gridManager = new GridLayoutManager(container.getContext(), 3);
            recyclerView.setLayoutManager(gridManager);

        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayoutManager linearManager = new LinearLayoutManager((container.getContext()));
            recyclerView.setLayoutManager(linearManager);
        }

        //Crea el adaptador con la bandera de favorito en false
        ContactAdapter adapter = new ContactAdapter(container.getContext(), false);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }
}
