package com.example.parcial1;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

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
            checkContactPermission();
        } else {
            full_contacts = savedInstanceState.getParcelableArrayList("CONTACTS");
        }
        contacts = new ArrayList<>();
        contacts.addAll(full_contacts);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (selectedContact != null) {
                ContactInfoFragment fragment = new ContactInfoFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.contactInfoFragment, fragment);
                transaction.commit();
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
        inflater.inflate(R.menu.menu_search, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList("CONTACTS", contacts);
        super.onSaveInstanceState(bundle);
    }

    private void checkContactPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }else{
            getContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getContacts();
                }
        }
    }

    private void getContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Uri defaultUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/ic_person");
                Contact contact = new Contact(name, "", id, new ArrayList<String>(), "", "", "", defaultUri.toString());
                full_contacts.add(contact);

                //Get phone number
                ArrayList<String> phones = new ArrayList<>();
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phones.add(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }
                    pCur.close();
                }
                contact.setPhones(phones);

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

                //Get birthday
                Cursor bdcur = getContactsBirthdays();
                int bDayColumn = bdcur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
                String bDay = "";
                while (bdcur.moveToNext()) {
                    bDay = bdcur.getString(bDayColumn);
                }
                contact.setDate(bDay);
            }
        }
    }

    public Uri getPhotoUri(long contactId) {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor;
        try {
            cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + "=" + contactId + " AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null, null);
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
        cursor.close();
        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }

    private Cursor getContactsBirthdays() {
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[] {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Event.CONTACT_ID, ContactsContract.CommonDataKinds.Event.START_DATE};
        String where = ContactsContract.Data.MIMETYPE + "= ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;
        String[] selectionArgs = new String[] {ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE};
        String sortOrder = null;
        return managedQuery(uri, projection, where, selectionArgs, sortOrder);
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

    public static void deleteContact(){
        full_contacts.remove(selectedContact);
        contacts.remove(selectedContact);
        selectedContact = null;
        viewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
