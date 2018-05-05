package com.example.parcial1;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class EditContactActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    private Uri uri;
    private ImageView imageView;
    private ArrayList<String> phones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        final EditText nameEditText = findViewById(R.id.add_nameEditText);
        final EditText lastNameEditText = findViewById(R.id.add_lastNameEditText);
        final EditText idEditText = findViewById(R.id.add_idEditText);
        final EditText emailEditText = findViewById(R.id.add_emailEditText);
        final EditText addressEditText = findViewById(R.id.add_addressEditText);
        imageView = findViewById(R.id.add_imageImageView);

        final Contact contact = MainActivity.selectedContact;
        phones = new ArrayList<>();
        if (savedInstanceState == null) {
            uri = Contact.defaultUri;
            phones = contact.getPhones();
            uri = Uri.parse(contact.getImageUri());
            nameEditText.setText(contact.getName());
            lastNameEditText.setText(contact.getLastName());
            emailEditText.setText(contact.getEmail());
            addressEditText.setText(contact.getAddress());
            idEditText.setText(contact.getId());
        } else {
            uri = Uri.parse(savedInstanceState.getString("URI"));
            phones.addAll(savedInstanceState.getStringArrayList("PHONES"));
        }

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (Exception e) {
        }
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        FloatingActionButton imageButton = findViewById(R.id.add_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.add_numbersRecyclerView);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearManager);

        final PhoneEditAdapter adapter = new PhoneEditAdapter(this, phones);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        Button numberAddButton = findViewById(R.id.add_numberAddButton);
        numberAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phones.add("");
                adapter.notifyDataSetChanged();
            }
        });

        Button editButton = findViewById(R.id.add_addButton);
        editButton.setText("Save");
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String id = idEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();
                if (uri == null) {
                    uri = Contact.defaultUri;
                }
                contact.setName(name);
                contact.setLastName(lastName);
                contact.setId(id);
                contact.setEmail(email);
                contact.setAddress(address);
                contact.setImageUri(uri.toString());
                contact.setPhones(phones);
                MainActivity.viewPagerAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), ContactInfoActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                uri = selectedImage;
                imageView.setImageURI(selectedImage);

                if (uri == null) {
                    uri = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_person");
                }
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (Exception e) {
                }
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
        bundle.putStringArrayList("PHONES", phones);
        super.onSaveInstanceState(bundle);
    }
}
