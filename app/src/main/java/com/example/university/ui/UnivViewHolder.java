package com.example.university.ui;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.university.R;

public class UnivViewHolder extends RecyclerView.ViewHolder {

    TextView textView_name, textView_country, textView_province;
    Button button_web;
    ImageButton button_favorite;
    ImageButton btn_share, btn_map;

    public UnivViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_name = itemView.findViewById(R.id.textView_name);
        textView_country = itemView.findViewById(R.id.textView_country);
        textView_province = itemView.findViewById(R.id.textView_province);
        button_web = itemView.findViewById(R.id.button_web);
        button_favorite = itemView.findViewById(R.id.button_favorite);
        btn_share = itemView.findViewById(R.id.btn_share);
        btn_map = itemView.findViewById(R.id.btn_map);
    }
}