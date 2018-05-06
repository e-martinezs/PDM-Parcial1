package com.example.parcial1.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parcial1.model.Contact;
import com.example.parcial1.tools.ImageHandler;
import com.example.parcial1.R;
import com.example.parcial1.activities.ContactInfoActivity;
import com.example.parcial1.activities.MainActivity;
import com.example.parcial1.fragments.ContactInfoFragment;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> contacts;
    private Context context;

    public ContactAdapter(Context context, boolean favorite) {
        this.context = context;

        //Obtiene la lista normal o la de favoritos dependiendo de la bandera favorite
        contacts = new ArrayList<>();
        if (favorite) {
            for (Contact contact : MainActivity.contacts) {
                if (contact.isFavorite()) {
                    contacts.add(contact);
                }
            }
        } else {
            contacts.addAll(MainActivity.contacts);
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

        //Si el contacto ya esta marcado como favorito cambia la imagen del checkbox
        if (contact.isFavorite()) {
            holder.favoriteButton.setChecked(true);
        } else {
            holder.favoriteButton.setChecked(false);
        }

        //Da funcionalidad al checkbox, si se marca el contacto se agrega a la lista de favoritos
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

        //Abre una actividad o crea un fragmento dependiendo de la orientacion
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    openContact(contact);
                } else if (v.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    loadContactFragment(contact);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    //Abre una nueva actividad para la informacion del contacto en orientacion portrait
    private void openContact(Contact contact) {
        Intent intent = new Intent(context.getApplicationContext(), ContactInfoActivity.class);
        intent.setAction(Intent.ACTION_SEND);
        MainActivity.selectedContact = contact;
        context.startActivity(intent);
    }

    //Crea el fragmento del contacto para la orientacion landscape
    private void loadContactFragment(Contact contact) {
        MainActivity.selectedContact = contact;
        ContactInfoFragment fragment = new ContactInfoFragment();
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.contactInfoFragment, fragment);
        transaction.commit();
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
