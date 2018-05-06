package com.example.parcial1.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.parcial1.model.Contact;
import com.example.parcial1.fragments.ContactInfoFragment;
import com.example.parcial1.fragments.ContactListFavFragment;
import com.example.parcial1.fragments.ContactListFragment;
import com.example.parcial1.R;
import com.example.parcial1.adapters.ViewPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Contact> contacts = new ArrayList<>();
    public static ArrayList<Contact> full_contacts = new ArrayList<>();
    public static ViewPagerAdapter viewPagerAdapter;
    public static Contact selectedContact;
    public static String lastQuery = "";
    private static final int READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri defaultUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_person");
        Contact.defaultUri = defaultUri;

        //Si hay informacion guardada la obtiene, si no obtiene datos nuevos por primera vez
        if (savedInstanceState == null) {
            full_contacts = new ArrayList<>();
            checkContactPermission();
        } else {
            full_contacts = savedInstanceState.getParcelableArrayList("CONTACTS");
        }
        contacts = new ArrayList<>();
        contacts.addAll(full_contacts);

        loadFragment();


        //Crea las tabs y el view pager que contiene los fragmentos de los contactos
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.addFragment(new ContactListFragment(), getString(R.string.tab_contacts));
        viewPagerAdapter.addFragment(new ContactListFavFragment(), getString(R.string.tab_favorites));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //Da funcionalidad al boton de agregar un nuevo contacto
        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
                startActivity(intent);
            }
        });
    }


    //Crea el fragmento de informacion para el contacto seleccionado
    private void loadFragment() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (selectedContact == null && contacts.size() > 0) {
                selectedContact = contacts.get(0);
            }
            if (selectedContact != null) {
                ContactInfoFragment fragment = new ContactInfoFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.contactInfoFragment, fragment);
                transaction.commit();
            }
        }
    }

    //Crea la barra de busqueda
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.searchView);
        final SearchView searchView = (SearchView) item.getActionView();

        //Revisa si se habia realizado una busqueda antes de pausar la aplicacion
        searchView.setQuery(lastQuery, false);
        if (!lastQuery.equals("")) {
            contacts = new ArrayList<>();
            doSearch(lastQuery);
        }
        lastQuery = "";

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                contacts = new ArrayList<>();
                lastQuery = query;
                doSearch(query);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                contacts = new ArrayList<>();
                contacts.addAll(full_contacts);
                viewPagerAdapter.notifyDataSetChanged();
                searchView.clearFocus();
                item.collapseActionView();
                return false;
            }
        });
        return true;
    }

    //Realiza la busqueda por nombre de contacto
    private void doSearch(String query) {
        String queryRegex = query + ".*";
        String fullName;
        for (Contact c : full_contacts) {
            fullName = c.getName() + " " + c.getLastName();
            if (fullName.matches(queryRegex)) {
                contacts.add(c);
            }
        }
        viewPagerAdapter.notifyDataSetChanged();
    }

    //Revisa si la aplicacion tiene permiso de acceder a los contactos del dispositivo, si no pide el permiso al usuario
    private void checkContactPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS);
        } else {
            getContacts();
        }
    }

    //Revisa si el usuario le ha dado permiso a la aplicacion de acceder a los contactos del dispositivo
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                }
        }
    }

    //Obtiene la informacion de los contactos del dispositivo
    private void getContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                //Obtiene el nombre y el id
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Uri defaultUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_person");
                Contact contact = new Contact(name, "", id, new ArrayList<String>(), "", "", "", defaultUri.toString());
                full_contacts.add(contact);

                //Obtiene el numero de telefono
                ArrayList<String> phones = new ArrayList<>();
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String newNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        boolean duplicate = false;
                        for (int i=0; i<phones.size(); i++) {
                            String p = phones.get(i);
                            //Revisa si el numero es un duplicado
                            if (PhoneNumberUtils.compare(p, newNumber)){
                                duplicate = true;
                                break;
                            }
                        }
                        if (!duplicate) {
                            phones.add(newNumber);
                        }
                    }
                    pCur.close();
                }
                contact.setPhones(phones);

                //Obtiene el email
                Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                while (emailCur.moveToNext()) {
                    String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    contact.setEmail(email);
                }
                emailCur.close();

                //Obtiene la direccion
                Cursor addrCur = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ?", new String[]{id}, null);
                while (addrCur.moveToNext()) {
                    String street = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                    String city = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                    String state = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                    String country = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                    String address = street + " " + city + " " + state + " " + country;
                    contact.setAddress(address);
                }
                addrCur.close();

                //Obtiene la foto
                Cursor phCur = cr.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + "= ? AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", new String[]{id}, null);
                while (phCur.moveToNext()) {
                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
                    Uri imageUri = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                    contact.setImageUri(imageUri.toString());
                }
                phCur.close();

                //Obtiene la fecha de cumpleaños
                Cursor bCur = cr.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + "= ? AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY, new String[]{id}, null);
                while (bCur.moveToNext()) {
                    String date = bCur.getString(bCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                    contact.setDate(date);
                }
                bCur.close();
            }
        }
        cursor.close();
    }

    //Agrega un contacto
    public static void addContact(Contact contact) {
        full_contacts.add(contact);
        contacts.add(contact);
        viewPagerAdapter.notifyDataSetChanged();
    }

    //Elimina el contacto seleccionado
    public static void deleteContact() {
        full_contacts.remove(selectedContact);
        contacts.remove(selectedContact);
        selectedContact = null;
        viewPagerAdapter.notifyDataSetChanged();
    }

    //Evita que se pierdan los datos al presionar el boton back
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    //Guarda los datos
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList("CONTACTS", full_contacts);
        super.onSaveInstanceState(bundle);
    }
}
