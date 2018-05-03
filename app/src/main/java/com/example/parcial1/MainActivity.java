package com.example.parcial1;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Contact> contacts = new ArrayList<>();
    public static ArrayList<Contact> full_contacts = new ArrayList<>();
    public static ViewPagerAdapter viewPagerAdapter;
    public static Contact selectedContact;
    public static String lastQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri defaultUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_person");
        Contact.defaultUri = defaultUri;

        if (savedInstanceState == null) {
            full_contacts = new ArrayList<>();
            //fillList();
            getContacts();
        } else {
            full_contacts = savedInstanceState.getParcelableArrayList("CONTACTS");
        }
        contacts = new ArrayList<>();
        contacts.addAll(full_contacts);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (selectedContact != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("CONTACT", selectedContact);
                ContactInfoFragment fragment = new ContactInfoFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.contactInfoFragment, fragment);
                transaction.commit();

                selectedContact = null;
            }
        }

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.addFragment(new ContactListFragment(), getString(R.string.tab_contacts));
        viewPagerAdapter.addFragment(new ContactListFavFragment(), getString(R.string.tab_favorites));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        final MenuItem item = menu.findItem(R.id.searchView);
        final SearchView searchView = (SearchView) item.getActionView();

        searchView.setQuery(lastQuery, false);
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

    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList("CONTACTS", contacts);
        super.onSaveInstanceState(bundle);
    }

    private void fillList() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_person");
        full_contacts.add(new Contact("name1", "lastname1", "1", "phone1", "email1", "add1", uri.toString()));
        full_contacts.add(new Contact("name2", "lastname2", "2", "phone2", "email2", "add2", uri.toString()));
        full_contacts.add(new Contact("name3", "lastname3", "3", "phone3", "email3", "add3", uri.toString()));
        full_contacts.add(new Contact("name4", "lastname4", "4", "phone4", "email4", "add4", uri.toString()));
        full_contacts.add(new Contact("name5", "lastname5", "5", "phone5", "email5", "add5", uri.toString()));
        full_contacts.add(new Contact("name6", "lastname6", "6", "phone6", "email6", "add6", uri.toString()));
        full_contacts.add(new Contact("name7", "lastname7", "7", "phone7", "email7", "add7", uri.toString()));
        full_contacts.add(new Contact("name8", "lastname8", "8", "phone8", "email8", "add8", uri.toString()));
        full_contacts.add(new Contact("name9", "lastname9", "9", "phone9", "email9", "add9", uri.toString()));
        full_contacts.add(new Contact("name10", "lastname10", "10", "phone10", "email10", "add10", uri.toString()));
        full_contacts.add(new Contact("name11", "lastname11", "11", "phone11", "email11", "add11", uri.toString()));
    }

    private void getContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Uri defaultUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_person");
                Contact contact = new Contact(name, "lastname", id, "phone", "email", "add", defaultUri.toString());
                full_contacts.add(contact);

                //Get phone number
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.setPhone(phone);
                    }
                    pCur.close();
                }

                //Get email
                Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                while (emailCur.moveToNext()) {
                    String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    contact.setEmail(email);
                }
                emailCur.close();

                //Get address
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

                //Get photo
                Uri imageUri = getPhotoUri(Long.parseLong(id));
                if (imageUri == null) {
                    imageUri = defaultUri;
                }
                contact.setImageUri(imageUri.toString());
            }
        }
    }

    public Uri getPhotoUri(long contactId) {
        ContentResolver contentResolver = getContentResolver();
        try {
            Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + "=" + contactId + " AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null, null);
            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }

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

    public static void addContact(Contact contact) {
        full_contacts.add(contact);
        contacts.add(contact);
        viewPagerAdapter.notifyDataSetChanged();
    }
}
