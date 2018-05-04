package com.example.parcial1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class PhoneEditAdapter extends RecyclerView.Adapter<PhoneEditAdapter.PhoneViewHolder> {
    private List<String> phones;
    private Context context;
    private PhoneEditAdapter adapter;

    public PhoneEditAdapter(Context context, List<String> phones) {
        this.context = context;
        this.phones = phones;
        adapter = this;
    }

    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.phone_add_cardview, parent, false);

        return new PhoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhoneViewHolder holder, final int position) {
        final String phone = phones.get(position);
        holder.phoneEditText.setText(phone);
        holder.phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phones.remove(holder.getAdapterPosition());
                phones.add(holder.getAdapterPosition(), charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              phones.remove(holder.getAdapterPosition());
              adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class PhoneViewHolder extends RecyclerView.ViewHolder {
        private EditText phoneEditText;
        private Button deleteButton;

        public PhoneViewHolder(View view) {
            super(view);
            phoneEditText = view.findViewById(R.id.add_phoneEditText);
            deleteButton = view.findViewById(R.id.add_numberDeleteButton);
        }
    }
}
