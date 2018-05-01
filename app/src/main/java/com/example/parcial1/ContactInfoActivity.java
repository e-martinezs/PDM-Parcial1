package com.example.parcial1;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
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
        TextView phoneTextView = findViewById(R.id.info_phoneTextView);
        TextView idTextView = findViewById(R.id.info_idTextView);

        Intent intent = getIntent();
        Contact contact = (Contact)intent.getSerializableExtra("CONTACT");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            MainActivity.selectedContact = contact;
            finish();
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), contact.getImageId());
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageImageView.setImageDrawable(roundedBitmapDrawable);

        String fullName = contact.getName()+" "+contact.getLastName();
        nameTextView.setText(fullName);

        emailTextView.setText(contact.getEmail());

        phoneTextView.setText(contact.getPhone());

        idTextView.setText(contact.getId());
    }
}
