package com.example.parcial1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddContactActivity extends AppCompatActivity{

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
                Contact contact = new Contact(name, lastName, id, phone, email, address, R.drawable.ic_person);

                MainActivity.addContact(contact);
                finish();
            }
        });
    }
}
