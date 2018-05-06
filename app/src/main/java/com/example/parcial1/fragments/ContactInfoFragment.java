package com.example.parcial1.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parcial1.model.Contact;
import com.example.parcial1.tools.ImageHandler;
import com.example.parcial1.adapters.PhoneInfoAdapter;
import com.example.parcial1.R;
import com.example.parcial1.activities.EditContactActivity;
import com.example.parcial1.activities.MainActivity;

public class ContactInfoFragment extends Fragment {

    private ImageView imageImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView idTextView;
    private TextView addressTextView;
    private TextView dateTextView;
    private RecyclerView recyclerView;
    private Contact contact;

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
        dateTextView = view.findViewById(R.id.info_dateTextView);

        loadContactData();

        ImageButton shareButton = view.findViewById(R.id.info_shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareData();
            }
        });

        ImageButton editButton = view.findViewById(R.id.info_editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditContactActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                startActivity(intent);
            }
        });

        ImageButton deleteButton = view.findViewById(R.id.info_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox();
            }
        });

        return view;
    }

    //Al reiniciar actualiza los datos
    @Override
    public void onResume() {
        super.onResume();
        loadContactData();
    }

    //Obtiene los datos del contacto seleccionado
    private void loadContactData() {
        if (MainActivity.selectedContact != null) {
            contact = MainActivity.selectedContact;

            Uri imageUri = Uri.parse(contact.getImageUri());
            ImageHandler.loadImage(getContext(), imageImageView, imageUri);

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
            dateTextView.setText(contact.getDate());
        }
    }

    //Crea un intent para compartir la informacion del contacto
    private void shareData() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String text = "Name: " + contact.getName() + " " + contact.getLastName();
        if (!contact.getEmail().equals("")) {
            text = text.concat("\nEmail: " + contact.getEmail());
        }
        if (!contact.getAddress().equals("")) {
            text = text.concat("\nAddress: " + contact.getAddress());
        }
        if (!contact.getDate().equals("")) {
            text = text.concat("\nBirthday: " + contact.getDate());
        }
        text = text.concat("\nPhone numbers: ");
        for (String s : contact.getPhones()) {
            text = text.concat("\n " + s);
        }
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share"));
    }

    //Muestra una dialogo para asegurarse que no se apreto el boton borrar por error
    private void showDialogBox() {
        final Fragment fragment = this;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                switch (option) {
                    case DialogInterface.BUTTON_POSITIVE:
                        MainActivity.deleteContact();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.remove(fragment);
                        transaction.commit();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }
}
