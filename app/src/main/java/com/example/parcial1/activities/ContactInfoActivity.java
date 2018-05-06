package com.example.parcial1.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parcial1.model.Contact;
import com.example.parcial1.tools.ImageHandler;
import com.example.parcial1.adapters.PhoneInfoAdapter;
import com.example.parcial1.R;

public class ContactInfoActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView idTextView;
    private TextView addressTextView;
    private TextView dateTextView;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        imageView = findViewById(R.id.info_imageImageView);
        nameTextView = findViewById(R.id.info_nameTextView);
        emailTextView = findViewById(R.id.info_emailTextView);
        idTextView = findViewById(R.id.info_idTextView);
        addressTextView = findViewById(R.id.info_addressTextView);
        dateTextView = findViewById(R.id.info_dateTextView);

        //Obtiene el contacto seleccionado
        contact = MainActivity.selectedContact;
        loadContactData();

        //Si la orientacion cambia a landscape se destruye la actividad
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        }

        ImageButton shareButton = findViewById(R.id.info_shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareData();
            }
        });

        ImageButton editButton = findViewById(R.id.info_editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditContactActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                startActivity(intent);
                finish();
            }
        });

        ImageButton deleteButton = findViewById(R.id.info_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox();
            }
        });
    }


    //Obtiene los datos del contacto seleccionado
    private void loadContactData() {
        Uri imageUri = Uri.parse(contact.getImageUri());
        ImageHandler.loadImage(this, imageView, imageUri);

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
        dateTextView.setText(contact.getDate());
    }

    //Crea un intent para compartir la informacion del contacto
    private void shareData() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String text = getString(R.string.text_name)+": " + contact.getName() + " " + contact.getLastName();
        if (!contact.getEmail().equals("")) {
            text = text.concat("\n"+getString(R.string.text_email)+": " + contact.getEmail());
        }
        if (!contact.getAddress().equals("")) {
            text = text.concat("\n"+getString(R.string.text_address)+": " + contact.getAddress());
        }
        if (!contact.getDate().equals("")) {
            text = text.concat("\n"+getString(R.string.text_birthday)+": " + contact.getDate());
        }
        text = text.concat("\n"+getString(R.string.text_phone_numbers)+": ");
        for (String s : contact.getPhones()) {
            text = text.concat("\n " + s);
        }
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.text_intent_share)));
    }

    //Muestra una dialogo para asegurarse que no se apreto el boton borrar por error
    private void showDialogBox() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                switch (option) {
                    case DialogInterface.BUTTON_POSITIVE:
                        MainActivity.deleteContact();
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.text_dialog_are_you_sure).setPositiveButton(R.string.text_yes, dialogClickListener).setNegativeButton(R.string.text_no, dialogClickListener).show();
    }
}