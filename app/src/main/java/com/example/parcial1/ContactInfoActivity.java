package com.example.parcial1;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactInfoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        ImageView imageImageView = findViewById(R.id.info_imageImageView);
        TextView nameTextView = findViewById(R.id.info_nameTextView);
        TextView emailTextView = findViewById(R.id.info_emailTextView);
        TextView idTextView = findViewById(R.id.info_idTextView);
        TextView addressTextView = findViewById(R.id.info_addressTextView);

        Intent intent = getIntent();
        Contact contact = intent.getParcelableExtra("CONTACT");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            MainActivity.selectedContact = contact;
            finish();
        }

        Uri imageUri = Uri.parse(contact.getImageUri());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        }catch (Exception e){}
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageImageView.setImageDrawable(roundedBitmapDrawable);

        RecyclerView recyclerView = findViewById(R.id.info_phonesRecyclerView);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearManager);
        PhoneInfoAdapter adapter = new PhoneInfoAdapter(this, contact.getPhones());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        String fullName = contact.getName()+" "+contact.getLastName();
        nameTextView.setText(fullName);
        emailTextView.setText(contact.getEmail());
        idTextView.setText(contact.getId());
        addressTextView.setText(contact.getAddress());
    }
}
