package com.example.parcial1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddContactActivity extends AppCompatActivity{

    public static final int PICK_IMAGE = 1;
    Uri uri;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        final EditText nameEditText = findViewById(R.id.add_nameEditText);
        final EditText lastNameEditText = findViewById(R.id.add_lastNameEditText);
        final EditText idEditText = findViewById(R.id.add_idEditText);
        final EditText phoneEditText = findViewById(R.id.add_phoneEditText);
        final EditText emailEditText = findViewById(R.id.add_emailEditText);
        final EditText addressEditText = findViewById(R.id.add_addressEditText);
        imageView = findViewById(R.id.add_imageImageView);

        final Uri defaultUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_person");
        if (savedInstanceState == null) {
            uri = defaultUri;
        }else{
            uri = Uri.parse(savedInstanceState.getString("URI"));
        }
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        }catch (Exception e){}
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        FloatingActionButton imageButton = findViewById(R.id.add_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        Button addButton = findViewById(R.id.add_addButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String id = idEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();
                if (uri == null) {
                    uri = defaultUri;
                }
                Contact contact = new Contact(name, lastName, id, phone, email, address, uri.toString());

                MainActivity.addContact(contact);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                uri = selectedImage;
                imageView.setImageURI(selectedImage);

                if (uri == null) {
                    uri = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_person");
                }
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                }catch (Exception e){}
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                roundedBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(roundedBitmapDrawable);
            }
        }
    }

    protected void onSaveInstanceState(Bundle bundle) {
        if (uri != null) {
            bundle.putString("URI", uri.toString());
        }
        super.onSaveInstanceState(bundle);
    }
}
