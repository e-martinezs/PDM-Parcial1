package com.example.parcial1;

import android.app.SearchManager;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<Contact> contacts = new ArrayList<>();
    public static List<Contact> full_contacts = new ArrayList<>();
    public static ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillList();
        contacts.addAll(full_contacts);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.addFragment(ContactListFragment.newInstance(false), getString(R.string.tab_contacts));
        viewPagerAdapter.addFragment(ContactListFragment.newInstance(true), getString(R.string.tab_favorites));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                contacts = new ArrayList<>();
                doSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                contacts = new ArrayList<>();
                doSearch(query);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                contacts.addAll(full_contacts);
                return true;
            }
        });
    }

    private void fillList() {
        full_contacts.add(new Contact("name1", "lastname1", "1", "phone1", "email1", "add1", R.drawable.ic_person));
        full_contacts.add(new Contact("name2", "lastname2", "2", "phone2", "email2", "add2", R.drawable.ic_person));
        full_contacts.add(new Contact("name3", "lastname3", "3", "phone3", "email3", "add3", R.drawable.ic_person));
        full_contacts.add(new Contact("name4", "lastname4", "4", "phone4", "email4", "add4", R.drawable.ic_person));
        full_contacts.add(new Contact("name5", "lastname5", "5", "phone5", "email5", "add5", R.drawable.ic_person));
        full_contacts.add(new Contact("name6", "lastname6", "6", "phone6", "email6", "add6", R.drawable.ic_person));
        full_contacts.add(new Contact("name7", "lastname7", "7", "phone7", "email7", "add7", R.drawable.ic_person));
        full_contacts.add(new Contact("name8", "lastname8", "8", "phone8", "email8", "add8", R.drawable.ic_person));
        full_contacts.add(new Contact("name9", "lastname9", "9", "phone9", "email9", "add9", R.drawable.ic_person));
        full_contacts.add(new Contact("name10", "lastname10", "10", "phone10", "email10", "add10", R.drawable.ic_person));
        full_contacts.add(new Contact("name11", "lastname11", "11", "phone11", "email11", "add11", R.drawable.ic_person));
    }

    private void doSearch(String query){
        String queryRegex = query+".*";
        String fullName;
        for (Contact c:  full_contacts){
            fullName = c.getName()+" "+c.getLastName();
            if (fullName.matches(queryRegex)){
                contacts.add(c);
            }
        }
        viewPagerAdapter.notifyDataSetChanged();
    }
}
