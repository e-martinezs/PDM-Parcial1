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
import android.widget.TextView;

import java.util.List;

public class PhoneInfoAdapter extends RecyclerView.Adapter<PhoneInfoAdapter.PhoneViewHolder> {
    private List<String> phones;
    private Context context;
    private PhoneInfoAdapter adapter;

    public PhoneInfoAdapter(Context context, List<String> phones) {
        this.context = context;
        this.phones = phones;
        adapter = this;
    }

    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.phone_info_cardview, parent, false);

        return new PhoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhoneViewHolder holder, final int position) {
        final String phone = phones.get(position);
        holder.phoneTextView.setText(phone);

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class PhoneViewHolder extends RecyclerView.ViewHolder {
        private TextView phoneTextView;
        private Button callButton;

        public PhoneViewHolder(View view) {
            super(view);
            phoneTextView = view.findViewById(R.id.info_phoneTextView);
            callButton = view.findViewById(R.id.info_callButton);
        }
    }
}
