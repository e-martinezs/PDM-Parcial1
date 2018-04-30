package com.example.parcial1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ContactListFragment extends Fragment {

    private boolean favorite;

    public static ContactListFragment newInstance(boolean favorite) {
        ContactListFragment fragment = new ContactListFragment();
        fragment.favorite = favorite;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager manager = new GridLayoutManager(container.getContext(), 3);
        ContactAdapter adapter = new ContactAdapter(container.getContext(), favorite);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }
}
