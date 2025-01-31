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
package pl.plantoplate.ui.main.recycler_views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.plantoplate.data.remote.models.product.ProductCategory;
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;
import pl.plantoplate.ui.main.recycler_views.view_holders.CategoryViewHolder;
import timber.log.Timber;

/**
 * This class is responsible for setting up the categories.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private final int productItemType;
    private final int categoryItemType;
    private SetupItemButtons listener;
    private List<ProductCategory> categories;

    public CategoryAdapter(List<ProductCategory> categories, int productItemType, int categoryItemType) {
        this.categories = categories;
        this.productItemType = productItemType;
        this.categoryItemType = categoryItemType;
    }

    public void setUpItemButtons(SetupItemButtons listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCategoriesList(List<ProductCategory> filterlist) {

        categories = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Timber.e("onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(this.categoryItemType, parent, false);
        return new CategoryViewHolder(itemView, this.productItemType);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position), listener);
    }

    @Override
    public int getItemCount() {
        if (categories == null) {
            return 0;
        }
        return categories.size();
    }
}