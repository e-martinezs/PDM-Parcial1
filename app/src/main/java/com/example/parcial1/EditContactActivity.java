package com.example.parcial1;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EditContactActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final int PICK_IMAGE = 1;
    private Uri uri;
    private ImageView imageView;
    private ArrayList<String> phones = new ArrayList<>();
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        final EditText nameEditText = findViewById(R.id.add_nameEditText);
        final EditText lastNameEditText = findViewById(R.id.add_lastNameEditText);
        final EditText idEditText = findViewById(R.id.add_idEditText);
        final EditText emailEditText = findViewById(R.id.add_emailEditText);
        final EditText addressEditText = findViewById(R.id.add_addressEditText);
        dateTextView = findViewById(R.id.add_dateTextView);
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

        ImageHandler.loadImage(this, imageView, uri);

        FloatingActionButton imageButton = findViewById(R.id.add_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getImage();
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

        final DatePickerFragment dialog = new DatePickerFragment();
        final Button dateButton = findViewById(R.id.add_dateButton);
        dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.show(getSupportFragmentManager(), "datePicker");
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
                String date = dateTextView.getText().toString();
                if (uri == null) {
                    uri = Contact.defaultUri;
                }
                for (int i=0; i<phones.size(); i++){
                    String s = phones.get(i);
                    if (s.isEmpty()){
                        phones.remove(i);
                    }
                }
                contact.setName(name);
                contact.setLastName(lastName);
                contact.setId(id);
                contact.setEmail(email);
                contact.setAddress(address);
                contact.setImageUri(uri.toString());
                contact.setPhones(phones);
                contact.setDate(date);
                MainActivity.viewPagerAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), ContactInfoActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private void getImage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, PICK_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImage();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                uri = selectedImage;
                ImageHandler.loadImage(this, imageView, selectedImage);
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = year+"-"+month+"-"+day;
        dateTextView.setText(date);
    }
}
