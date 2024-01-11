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
package pl.plantoplate.ui.main.recycler_views.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;

public class BoughtProductsViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView unit;
    private final ImageView checkShoppingListButton;
    private final ImageView deleteProductButton;

    public BoughtProductsViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nazwaProduktu_kupione);
        unit = itemView.findViewById(R.id.jednostkiMiary_kupione);
        checkShoppingListButton = itemView.findViewById(R.id.iconUncheck_kupione);
        deleteProductButton = itemView.findViewById(R.id.iconDelete_kupione);
    }

    public void bind(Product product, SetupItemButtons listener) {
        name.setText(product.getName());
        float amount = BigDecimal.valueOf(Float.parseFloat(String.valueOf(product.getAmount())))
                .setScale(3, RoundingMode.HALF_UP)
                .floatValue();
        String unitText = amount + " " + product.getUnit().toLowerCase();
        unit.setText(unitText);
        listener.setupCheckShoppingListButtonClick(checkShoppingListButton, product);
        listener.setupDeleteProductButtonClick(deleteProductButton, product);
    }
}