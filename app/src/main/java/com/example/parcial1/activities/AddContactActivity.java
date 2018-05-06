package com.example.parcial1.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parcial1.model.Contact;
import com.example.parcial1.fragments.DatePickerFragment;
import com.example.parcial1.tools.ImageHandler;
import com.example.parcial1.adapters.PhoneEditAdapter;
import com.example.parcial1.R;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final int PICK_IMAGE = 1;
    private Uri uri;
    private ImageView imageView;
    private EditText nameEditText;
    private EditText lastNameEditText;
    private EditText idEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private EditText dateEditText;
    private ArrayList<String> phones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        nameEditText = findViewById(R.id.add_nameEditText);
        lastNameEditText = findViewById(R.id.add_lastNameEditText);
        idEditText = findViewById(R.id.add_idEditText);
        emailEditText = findViewById(R.id.add_emailEditText);
        addressEditText = findViewById(R.id.add_addressEditText);
        dateEditText = findViewById(R.id.add_dateTextView);
        imageView = findViewById(R.id.add_imageImageView);

        //Revisa si hay datos guardados o si debe crearlos
        phones = new ArrayList<>();
        if (savedInstanceState == null) {
            uri = Contact.defaultUri;
            phones.add("");
        } else {
            uri = Uri.parse(savedInstanceState.getString("URI"));
            phones.addAll(savedInstanceState.getStringArrayList("PHONES"));
        }
        ImageHandler.loadImage(this, imageView, uri);

        RecyclerView recyclerView = findViewById(R.id.add_numbersRecyclerView);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearManager);

        final PhoneEditAdapter adapter = new PhoneEditAdapter(this, phones);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton imageButton = findViewById(R.id.add_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });

        ImageButton numberAddButton = findViewById(R.id.add_numberAddButton);
        numberAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phones.add("");
                adapter.notifyDataSetChanged();
            }
        });

        final DatePickerFragment dialog = new DatePickerFragment();
        ImageButton dateButton = findViewById(R.id.add_dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show(getSupportFragmentManager(), "datePicker");
            }
        });

        final Button addButton = findViewById(R.id.add_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact(view);
            }
        });
    }

    //Agrega el nuevo contacto a la lista de contactos
    private void addContact(View view) {
        String name = nameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String id = idEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String date = dateEditText.getText().toString();

        //Revisa si el nombre no esta vacio, si no muestra una advertencia
        if (name.equals("")) {
            Snackbar snackbar = Snackbar.make(view, R.string.text_warning_name, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            if (uri == null) {
                uri = Contact.defaultUri;
            }
            //Remueve los numeros de telefono vacios
            for (int i = 0; i < phones.size(); i++) {
                String s = phones.get(i);
                if (s.isEmpty()) {
                    phones.remove(i);
                }
            }
            Contact contact = new Contact(name, lastName, id, phones, email, address, date, uri.toString());
            MainActivity.selectedContact = contact;
            MainActivity.addContact(contact);
            finish();
        }
    }

    //Revisa si la aplicacion tiene permiso de acceder a los contactos del dispositivo, si no pide el permiso al usuario
    private void getImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE);
        } else {
            //Crea un intent para seleccionar la imagen
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.text_intent_image));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, PICK_IMAGE);
        }
    }

    //Revisa si el usuario le ha dado permiso a la aplicacion de acceder a los archivos del dispositivo
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImage();
                }
        }
    }

    //Obtiene la imagen seleccionada por el usuario
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

    //Guarda los datos de telefonos e imagen
    protected void onSaveInstanceState(Bundle bundle) {
        if (uri != null) {
            bundle.putString("URI", uri.toString());
        }
        bundle.putStringArrayList("PHONES", phones);
        super.onSaveInstanceState(bundle);
    }

    //Ingresa la fecha seleccionada con formato al text view
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = year + "-" + month + "-" + day;
        dateEditText.setText(date);
    }
}
