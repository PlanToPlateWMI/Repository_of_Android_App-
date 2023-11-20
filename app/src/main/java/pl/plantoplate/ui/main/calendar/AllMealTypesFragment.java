/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.plantoplate.ui.main.calendar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.time.LocalDate;
import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import pl.plantoplate.data.remote.repository.MealRepository;
import pl.plantoplate.databinding.FragmentCalendarInsideAllBinding;
import pl.plantoplate.utils.CategorySorter;
import pl.plantoplate.ui.main.calendar.events.DateSelectedEvent;
import pl.plantoplate.ui.main.calendar.recyclerViews.meal.adapters.MealTypesAdapter;
import timber.log.Timber;

public class AllMealTypesFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private SharedPreferences prefs;
    private MealTypesAdapter mealTypesAdapter;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDateSelected(DateSelectedEvent event){
        mealTypesAdapter.setMealTypes(new ArrayList<>());
        getAllMeals(event.getDate());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCalendarInsideAllBinding fragmentCalendarInsideBldBinding =
                FragmentCalendarInsideAllBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("prefs", 0);
        compositeDisposable = new CompositeDisposable();

        setupRecyclerView(fragmentCalendarInsideBldBinding);
        return fragmentCalendarInsideBldBinding.getRoot();
    }

    public void setupRecyclerView(FragmentCalendarInsideAllBinding fragmentCalendarInsideBldBinding) {
        RecyclerView mealTypesRecyclerView = fragmentCalendarInsideBldBinding.productsOwnRecyclerView;
        mealTypesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealTypesAdapter = new MealTypesAdapter();
        mealTypesRecyclerView.setAdapter(mealTypesAdapter);
    }

    public void getAllMeals(LocalDate date){
        MealRepository mealRepository = new MealRepository();
        compositeDisposable.add(
                mealRepository.getMealsByDate("Bearer " + prefs.getString("token", ""), date)
                        .subscribe(meals -> mealTypesAdapter.setMealTypes(CategorySorter.groupMealsByType(meals)),
                                   Timber::e)
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}