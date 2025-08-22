package com.example.university;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

    // دالة لتحديث البيانات
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

        holder.textView_name.setText(favorite.getName());
        holder.textView_country.setText(favorite.getCountry());
        holder.textView_province.setText(favorite.getWebPage());

        holder.button_web.setOnClickListener(v -> webClickListener.OnClicked(favorite.getWebPage()));

        // هنا دايماً النجمة هتكون مليانة
        holder.button_favorite.setImageResource(R.drawable.ic_star_filled);
        holder.button_favorite.setOnClickListener(v -> {
            // لما يدوس عليها، هنحذفها من المفضلة
            viewModel.deleteFavorite(favorite);
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }
}