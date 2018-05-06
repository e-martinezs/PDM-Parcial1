package com.example.parcial1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> contacts = new ArrayList<>();
    private Context context;

    public ContactAdapter(Context context, boolean favorite) {
        this.context = context;
        contacts = new ArrayList<>();
        if (favorite) {
            for (Contact contact : MainActivity.contacts) {
                if (contact.isFavorite()) {
                    contacts.add(contact);
                }
            }
        } else {
            for (Contact contact : MainActivity.contacts) {
                contacts.add(contact);
            }
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_cardview, parent, false);

        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        final Contact contact = contacts.get(position);
        String fullName = contact.getName() + " " + contact.getLastName();
        holder.nameTextView.setText(fullName);

        Uri imageUri = Uri.parse(contact.getImageUri());
        ImageHandler.loadImage(context, holder.imageImageView, imageUri);

        if (contact.isFavorite()) {
            holder.favoriteButton.setChecked(true);
        } else {
            holder.favoriteButton.setChecked(false);
        }

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.favoriteButton.isChecked()) {
                    contacts.get(holder.getAdapterPosition()).setFavorite(true);
                    MainActivity.viewPagerAdapter.notifyDataSetChanged();
                } else {
                    contacts.get(holder.getAdapterPosition()).setFavorite(false);
                    MainActivity.viewPagerAdapter.notifyDataSetChanged();
                }
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Intent intent = new Intent(context.getApplicationContext(), ContactInfoActivity.class);
                    intent.setAction(Intent.ACTION_SEND);
                    MainActivity.selectedContact = contact;
                    context.startActivity(intent);
                } else if (v.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    MainActivity.selectedContact = contact;
                    ContactInfoFragment fragment = new ContactInfoFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.contactInfoFragment, fragment);
                    transaction.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView imageImageView;
        private TextView nameTextView;
        private CheckBox favoriteButton;

        public ContactViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            imageImageView = view.findViewById(R.id.card_imageImageView);
            nameTextView = view.findViewById(R.id.card_nameTextView);
            favoriteButton = view.findViewById(R.id.card_favoriteButton);
        }
    }
}
