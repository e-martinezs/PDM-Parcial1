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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        ImageView imageImageView = findViewById(R.id.info_imageImageView);
        TextView nameTextView = findViewById(R.id.info_nameTextView);
        TextView emailTextView = findViewById(R.id.info_emailTextView);
        TextView idTextView = findViewById(R.id.info_idTextView);
        TextView addressTextView = findViewById(R.id.info_addressTextView);

        final Contact contact = MainActivity.selectedContact;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        }

        Uri imageUri = Uri.parse(contact.getImageUri());
        ImageHandler.loadImage(this, imageImageView, imageUri);

        RecyclerView recyclerView = findViewById(R.id.info_phonesRecyclerView);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearManager);
        PhoneInfoAdapter adapter = new PhoneInfoAdapter(this, contact.getPhones());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        String fullName = contact.getName() + " " + contact.getLastName();
        nameTextView.setText(fullName);
        emailTextView.setText(contact.getEmail());
        idTextView.setText(contact.getId());
        addressTextView.setText(contact.getAddress());

        Button shareButton = findViewById(R.id.info_shareButton);
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

        Button editButton = findViewById(R.id.info_editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditContactActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                startActivity(intent);
                finish();
            }
        });

        Button deleteButton = findViewById(R.id.info_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.deleteContact();
                finish();
            }
        });
    }
}