package com.example.parcial1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> contacts = new ArrayList<>();
    private Context context;
    private boolean favorite;

    public ContactAdapter(Context context, boolean favorite) {
        this.context = context;
        this.favorite = favorite;

        if (favorite) {
            for (Contact contact : MainActivity.contacts) {
                if (contact.isFavorite()) {
                    contacts.add(contact);
                }
            }
        } else {
           for (Contact contact : MainActivity.contacts){
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

        if (contact.isFavorite()) {
            holder.favoriteCheckbox.setChecked(true);
        } else {
            holder.favoriteCheckbox.setChecked(false);
        }

        holder.favoriteCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.favoriteCheckbox.isChecked()) {
                    contacts.get(holder.getAdapterPosition()).setFavorite(true);
                    MainActivity.viewPagerAdapter.notifyDataSetChanged();
                } else {
                    contacts.get(holder.getAdapterPosition()).setFavorite(false);
                    MainActivity.viewPagerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageImageView;
        private TextView nameTextView;
        private CheckBox favoriteCheckbox;

        public ContactViewHolder(View view) {
            super(view);
            imageImageView = view.findViewById(R.id.card_imageImageView);
            nameTextView = view.findViewById(R.id.card_nameTextView);
            favoriteCheckbox = view.findViewById(R.id.card_favoriteCheckbox);
        }
    }
}
