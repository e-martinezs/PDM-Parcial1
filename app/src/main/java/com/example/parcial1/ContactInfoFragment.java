package com.example.parcial1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

public class ContactInfoFragment extends Fragment {

    ImageView imageImageView;
    TextView nameTextView;
    TextView emailTextView;
    TextView idTextView;
    TextView addressTextView;
    RecyclerView recyclerView;
    Contact contact;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact_info, container, false);

        imageImageView = view.findViewById(R.id.info_imageImageView);
        nameTextView = view.findViewById(R.id.info_nameTextView);
        emailTextView = view.findViewById(R.id.info_emailTextView);
        idTextView = view.findViewById(R.id.info_idTextView);
        addressTextView = view.findViewById(R.id.info_addressTextView);
        recyclerView = view.findViewById(R.id.info_phonesRecyclerView);

        loadContactData();

        Button shareButton = view.findViewById(R.id.info_shareButton);
        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String text = "Name: "+contact.getName()+" "+contact.getLastName()+"\n"+"Email: "+contact.getEmail()+"\n"+"Address: "+contact.getAddress()+"\nPhone numbers: ";
                for (String s:contact.getPhones()){
                    text = text.concat("\n "+s);
                }
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        Button editButton = view.findViewById(R.id.info_editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditContactActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                startActivity(intent);
            }
        });

        Button deleteButton = view.findViewById(R.id.info_deleteButton);
        final Fragment fragment = this;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.deleteContact();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.remove(fragment);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadContactData();
    }

    private void loadContactData(){
        if (MainActivity.selectedContact != null) {
            contact = MainActivity.selectedContact;

            Uri imageUri = Uri.parse(contact.getImageUri());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            } catch (Exception e) {
            }
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageImageView.setImageDrawable(roundedBitmapDrawable);

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
    }
}
