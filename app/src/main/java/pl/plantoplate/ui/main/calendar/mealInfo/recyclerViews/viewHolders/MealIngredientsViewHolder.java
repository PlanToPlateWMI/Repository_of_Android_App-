package pl.plantoplate.ui.main.calendar.mealInfo.recyclerViews.viewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.plantoplate.data.remote.models.recipe.Ingredient;
import pl.plantoplate.databinding.ItemSkladnikBinding;
import pl.plantoplate.databinding.ItemSkladnikForCalendarBinding;

public class MealIngredientsViewHolder extends RecyclerView.ViewHolder {
    private TextView ingredientName;
    private TextView ingredientAmount;

    public MealIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemSkladnikForCalendarBinding itemSkladnikForCalendarBinding = ItemSkladnikForCalendarBinding.bind(itemView);
        ingredientName = itemSkladnikForCalendarBinding.textViewSkladnik;
        ingredientAmount = itemSkladnikForCalendarBinding.textViewIloscSkladnika;

    }

//    public void setOnCheckedChangeListener(CheckBox.OnCheckedChangeListener listener) {
//        checkBox.setOnCheckedChangeListener(listener);
//    }

    public void bind(Ingredient ingredient, boolean isSelected) {
        //checkBox.setChecked(isSelected);
        float amount = BigDecimal.valueOf(Float.parseFloat(String.valueOf(ingredient.getQuantity())))
                .setScale(3, RoundingMode.HALF_UP)
                .floatValue();
        ingredientName.setText(ingredient.getIngredientName());
        ingredientAmount.setText(amount + " " + ingredient.getUnit());
    }
}