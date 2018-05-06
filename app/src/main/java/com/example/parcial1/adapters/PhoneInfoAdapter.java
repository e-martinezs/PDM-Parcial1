package com.example.parcial1.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.parcial1.R;
import com.example.parcial1.activities.CallActivity;

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

        //Abre la actividad para realizar la llamada
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), CallActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra("PHONE", phones.get(holder.getAdapterPosition()));
                context.startActivity(intent);
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
