package com.example.university.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university.R;
import com.example.university.utils.WebClickListener;
import com.example.university.data.UniversityEntity;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<UnivViewHolder> {

    private final Context context;
    private List<UniversityEntity> favorites;
    private final WebClickListener webClickListener;
    private final UnivViewModel viewModel;

    public FavoritesAdapter(Context context, List<UniversityEntity> favorites, WebClickListener webClickListener, UnivViewModel viewModel) {
        this.context = context;
        this.favorites = favorites;
        this.webClickListener = webClickListener;
        this.viewModel = viewModel;
    }

    public void updateFavorites(List<UniversityEntity> newFavorites) {
        this.favorites = newFavorites;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UnivViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UnivViewHolder(LayoutInflater.from(context).inflate(R.layout.list_univ, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnivViewHolder holder, int position) {
        UniversityEntity favorite = favorites.get(position);

        String name = favorite.getName();
        String url = favorite.getWebPage();
        String country = favorite.getCountry();

        holder.textView_name.setText(name);
        holder.textView_country.setText(country);

        if (url != null && !url.isEmpty()) {
            holder.textView_province.setText(url);
        } else {
            holder.textView_province.setText("No website available");
        }

        holder.button_web.setOnClickListener(v -> {
            if (url != null && !url.isEmpty()) {
                webClickListener.OnClicked(url);
            }
        });

        holder.btn_share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this university: " + name + "\n" + url);
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, "Share via"));
        });

        holder.btn_map.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(name));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            } else {
                Intent browserMapIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(name)));
                context.startActivity(browserMapIntent);
            }
        });

        holder.button_favorite.setImageResource(R.drawable.ic_star_filled);
        holder.button_favorite.setOnClickListener(v -> {
            viewModel.deleteFavorite(favorite);
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }
}