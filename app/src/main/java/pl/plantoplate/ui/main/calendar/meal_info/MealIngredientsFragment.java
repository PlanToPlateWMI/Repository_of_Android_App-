package pl.plantoplate.ui.main.calendar.meal_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import pl.plantoplate.databinding.FragmentItemRecipeInsideSkladnikiBinding;
import pl.plantoplate.ui.main.calendar.meal_info.recycler_views.adapters.MealIngredientsAdapter;
import pl.plantoplate.ui.main.calendar.meal_info.view_models.MealInfoViewModel;

/**
 * This class is responsible for setting up the ingredients in the recycler view.
 */
public class MealIngredientsFragment extends Fragment {


    private MealIngredientsAdapter mealIngredientsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentItemRecipeInsideSkladnikiBinding fragmentItemRecipeInsideSkladnikiBinding =
                FragmentItemRecipeInsideSkladnikiBinding.inflate(inflater, container, false);

        setupRecyclerView(fragmentItemRecipeInsideSkladnikiBinding);
        setMealInfoViewModel();
        return fragmentItemRecipeInsideSkladnikiBinding.getRoot();
    }

    public void setupRecyclerView(FragmentItemRecipeInsideSkladnikiBinding fragmentItemRecipeInsideSkladnikiBinding){
        RecyclerView recyclerView = fragmentItemRecipeInsideSkladnikiBinding.recipeRecyclerViewSkladniki;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealIngredientsAdapter = new MealIngredientsAdapter(new ArrayList<>());
        recyclerView.setAdapter(mealIngredientsAdapter);
    }

    public void setMealInfoViewModel(){
        MealInfoViewModel mealInfoViewModel;
        mealInfoViewModel = new ViewModelProvider(requireParentFragment()).get(MealInfoViewModel.class);

        mealInfoViewModel.getMealInfo().observe(getViewLifecycleOwner(),
                recipeInfo -> mealIngredientsAdapter.setIngredientsList(recipeInfo.getIngredients()));
    }
}