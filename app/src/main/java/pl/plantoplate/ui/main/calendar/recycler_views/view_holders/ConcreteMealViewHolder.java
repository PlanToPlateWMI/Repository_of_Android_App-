package pl.plantoplate.ui.main.calendar.recycler_views.view_holders;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.databinding.ItemRecipeInCalendarBinding;
import pl.plantoplate.ui.main.calendar.recycler_views.listeners.SetupMealItem;

/**
 * ViewHolder class for displaying concrete meals in a RecyclerView.
 */
public class ConcreteMealViewHolder extends RecyclerView.ViewHolder {

    private final TextView vegeTextView;
    private final TextView mealName;
    private final TextView mealPreparationTime;
    private final ImageView mealImage;
    private final LinearLayout mealLayout;

    public ConcreteMealViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemRecipeInCalendarBinding binding = ItemRecipeInCalendarBinding.bind(itemView);
        vegeTextView = binding.wegeText;
        mealName = binding.textView2;
        mealPreparationTime = binding.textView5;
        mealImage = binding.iconUncheckKupione;
        mealLayout = binding.layoutRecipeItemInCalendar;
    }

    public void bind(Meal meal, SetupMealItem listener) {
        if (!meal.isPrepared()) {
            mealLayout.setOnClickListener(v -> listener.setupMealItemClick(meal.getId()));
        } else {
            mealLayout.setClickable(false);
            mealLayout.setEnabled(false);
            mealLayout.setAlpha(0.6f);
        }

        // Rest of your code for populating meal data remains the same
        vegeTextView.setVisibility(meal.isVege() ? View.VISIBLE : View.GONE);
        if (TextUtils.isEmpty(meal.getImage())) {
            Picasso.get()
                    .load(R.drawable.noimage)
                    .into(mealImage);
        } else {
            Picasso.get()
                    .load(meal.getImage())
                    .error(R.drawable.noimage)
                    .into(mealImage);
        }
        mealName.setText(meal.getRecipeTitle());
        mealPreparationTime.setText(meal.getTime() + " min");
    }
}