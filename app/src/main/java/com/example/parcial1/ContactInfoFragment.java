package com.example.parcial1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactInfoFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact_info, container, false);

        ImageView imageImageView = view.findViewById(R.id.info_imageImageView);
        TextView nameTextView = view.findViewById(R.id.info_nameTextView);
        TextView emailTextView = view.findViewById(R.id.info_emailTextView);
        TextView idTextView = view.findViewById(R.id.info_idTextView);
        TextView addressTextView = view.findViewById(R.id.info_addressTextView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Contact contact = bundle.getParcelable("CONTACT");

            Uri imageUri = Uri.parse(contact.getImageUri());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            }catch (Exception e){}
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageImageView.setImageDrawable(roundedBitmapDrawable);

            RecyclerView recyclerView = view.findViewById(R.id.info_phonesRecyclerView);
            LinearLayoutManager linearManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearManager);
            PhoneInfoAdapter adapter = new PhoneInfoAdapter(getContext(), contact.getPhones());
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);

            String fullName = contact.getName() + " " + contact.getLastName();
            nameTextView.setText(fullName);
            emailTextView.setText(contact.getEmail());
            idTextView.setText(contact.getId());
            addressTextView.setText(contact.getAddress());
        }

        return view;
    }
}
