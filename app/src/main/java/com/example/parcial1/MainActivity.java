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
        contacts.add(new Contact("name1", "lastname1", 1, "email1", "add1", R.drawable.ic_person));
        contacts.add(new Contact("name2", "lastname2", 2, "email2", "add2", R.drawable.ic_person));
        contacts.add(new Contact("name3", "lastname3", 3, "email3", "add3", R.drawable.ic_person));
        contacts.add(new Contact("name4", "lastname4", 4, "email4", "add4", R.drawable.ic_person));
        contacts.add(new Contact("name5", "lastname5", 5, "email5", "add5", R.drawable.ic_person));
    }
}
