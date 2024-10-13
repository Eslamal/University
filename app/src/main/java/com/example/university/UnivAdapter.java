package com.example.university;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UnivAdapter extends RecyclerView.Adapter<UnivViewHolder> {
    Context context;
    List<APIResponse> list;
    WebClickListener listener;

    public UnivAdapter(Context context, List<APIResponse> list, WebClickListener listener) {
        this.context = context;
        this.list = list != null ? list : List.of(); // Initialize list if null
        this.listener = listener;
    }

    @NonNull
    @Override
    public UnivViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UnivViewHolder(LayoutInflater.from(context).inflate(R.layout.list_univ, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnivViewHolder holder, int position) {
        APIResponse university = list.get(position);

        holder.textView_name.setText(university.getName());
        holder.textView_country.setText(university.getCountry() + " - " + university.getAlphaTwoCode());

        // Check if web_pages is not null or empty before setting the text
        if (university.getWebPages() != null && !university.getWebPages().isEmpty()) {
            holder.textView_province.setText(university.getWebPages().get(0));
        } else {
            holder.textView_province.setText("No web page available"); // Handle empty or null web pages
        }

        holder.button_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (university.getWebPages() != null && !university.getWebPages().isEmpty()) {
                    listener.OnClicked(university.getWebPages().get(0));
                } else {
                    // Optionally handle the case when no web page is available
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
