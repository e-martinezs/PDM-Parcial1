package com.example.parcial1;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<Contact> contacts = new ArrayList<>();
    public static ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillList();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.addFragment(ContactListFragment.newInstance(false), getString(R.string.tab_contacts));
        viewPagerAdapter.addFragment(ContactListFragment.newInstance(true), getString(R.string.tab_favorites));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void fillList() {
        contacts.add(new Contact("name1", "lastname1", "1", "phone1", "email1", "add1", R.drawable.ic_person));
        contacts.add(new Contact("name2", "lastname2", "2", "phone2", "email2", "add2", R.drawable.ic_person));
        contacts.add(new Contact("name3", "lastname3", "3", "phone3", "email3", "add3", R.drawable.ic_person));
        contacts.add(new Contact("name4", "lastname4", "4", "phone4", "email4", "add4", R.drawable.ic_person));
        contacts.add(new Contact("name5", "lastname5", "5", "phone5", "email5", "add5", R.drawable.ic_person));
        contacts.add(new Contact("name6", "lastname6", "6", "phone6", "email6", "add6", R.drawable.ic_person));
        contacts.add(new Contact("name7", "lastname7", "7", "phone7", "email7", "add7", R.drawable.ic_person));
        contacts.add(new Contact("name8", "lastname8", "8", "phone8", "email8", "add8", R.drawable.ic_person));
        contacts.add(new Contact("name9", "lastname9", "9", "phone9", "email9", "add9", R.drawable.ic_person));
        contacts.add(new Contact("name10", "lastname10", "10", "phone10", "email10", "add10", R.drawable.ic_person));
        contacts.add(new Contact("name11", "lastname11", "11", "phone11", "email11", "add11", R.drawable.ic_person));
    }
}
