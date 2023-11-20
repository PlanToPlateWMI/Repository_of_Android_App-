package pl.plantoplate.ui.main.calendar.recyclerViews.meal.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.ui.main.calendar.recyclerViews.meal.viewHolders.ConcreteMealViewHolder;

public class ConcreteMealAdapter extends RecyclerView.Adapter<ConcreteMealViewHolder>{

    private ArrayList<Meal> meals;

    public ConcreteMealAdapter() {
        this.meals = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConcreteMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_in_calendar, parent, false);
        return new ConcreteMealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConcreteMealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals == null ? 0 : meals.size();
    }
}