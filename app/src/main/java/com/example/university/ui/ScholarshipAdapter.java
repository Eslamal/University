package com.example.university.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.university.R;
import com.example.university.data.Scholarship;
import java.util.List;

public class ScholarshipAdapter extends RecyclerView.Adapter<ScholarshipAdapter.ViewHolder> {

    Context context;
    List<Scholarship> list;

    public ScholarshipAdapter(Context context, List<Scholarship> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_scholarship, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Scholarship item = list.get(position);

        String name = item.getName() != null ? item.getName() : "Scholarship";
        holder.tvName.setText(name);
        holder.tvCountry.setText(item.getCountry());
        holder.tvDesc.setText(item.getDescription());

        // 💡 حماية أول حرف من الكراش
        if (!name.isEmpty()) {
            holder.tvLetter.setText(String.valueOf(name.charAt(0)));
        } else {
            holder.tvLetter.setText("S");
        }

        // 💡 حماية الألوان (الطريقة الأحدث والأكثر أماناً)
        try {
            holder.layoutHeader.setBackgroundColor(androidx.core.content.ContextCompat.getColor(context, item.getColorRes()));
        } catch (Exception e) {
            holder.layoutHeader.setBackgroundColor(android.graphics.Color.GRAY);
        }

        holder.btnApply.setOnClickListener(v -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                context.startActivity(browserIntent);
            } catch (Exception e) {
                // منع الكراش لو الرابط بايظ
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCountry, tvDesc, tvLetter;
        Button btnApply;
        LinearLayout layoutHeader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCountry = itemView.findViewById(R.id.tv_country);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvLetter = itemView.findViewById(R.id.tv_letter);
            btnApply = itemView.findViewById(R.id.btn_apply);
            layoutHeader = itemView.findViewById(R.id.layout_header);
        }
    }
}