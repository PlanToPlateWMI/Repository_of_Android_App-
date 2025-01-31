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
package pl.plantoplate.ui.main.popups;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;

/**
 * Class responsible for displaying pop up window for modifying product
 */
public class ModifyProductPopUp extends Dialog {

    private final TextView productName;
    private final ImageView plusButton;
    private final ImageView minusButton;
    private final TextView closeButton;
    public TextInputEditText quantity;
    private final TextView productUnitTextView;
    private TextView acceptButton;
    private final Button radioButtonMin;
    private final Button radioButtonMiddle;
    private final Button radioButtonMax;
    private final HashMap<String,List<Float>> map = new HashMap<>();

    /**
     * @param context - context of the activity
     * @param product - product to be modified
     */
    @SuppressLint("SetTextI18n")
    public ModifyProductPopUp(@NonNull Context context, Product product) {
        super(context);
        setCancelable(true);
        setContentView(R.layout.new_pop_up_change_in_product_quantity);

        productName = findViewById(R.id.text_head);
        plusButton = findViewById(R.id.plus);
        minusButton = findViewById(R.id.minus);
        closeButton = findViewById(R.id.close);
        quantity = findViewById(R.id.ilosc);
        productUnitTextView = findViewById(R.id.unit);
        acceptButton = findViewById(R.id.zatwierdzenie);
        radioButtonMin = findViewById(R.id.min);
        radioButtonMiddle = findViewById(R.id.middle);
        radioButtonMax = findViewById(R.id.max);

        map.put("szt", List.of(0.5f,2.0f,5.0f));
        map.put("l", List.of(0.25f,0.5f,5.0f));
        map.put("ml", List.of(10.0f,50.0f,200.0f));
        map.put("kg", List.of(0.25f,0.5f,5.0f));
        map.put("gr", List.of(10.0f,50.0f,200.0f));

        String title = productName.getText().toString() + " " + product.getName();
        productName.setText(title);

        String unitTitle = productUnitTextView.getText().toString() + " " + product.getUnit().toLowerCase();
        productUnitTextView.setText(unitTitle);

        radioButtonMin.setText("+" + Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(0));
        radioButtonMiddle.setText("+" + Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(1));
        radioButtonMax.setText("+" + Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(2));

        if(product.getAmount() == 0.0){
            quantity.setText("");
        }
        else{
            quantity.setText(String.valueOf(product.getAmount()));
        }

        quantity.requestFocus();
        quantity.setTag(product.getAmount());

        // set up input type
        setOnlyFloatInput();

        // set views listeners
        closeButton.setOnClickListener(v -> dismiss());
        plusButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "0.0";
            }
            float quantity = Float.parseFloat(quantityValue);
            quantity++;
            this.quantity.setText(String.valueOf(quantity));
        });
        minusButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "0.0";
            }
            float quantity = Float.parseFloat(quantityValue);
            if (quantity > 1) {
                quantity--;
                this.quantity.setText(String.valueOf(quantity));
            }
        });
        radioButtonMin.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "0.0";
            }
            float quantity = Float.parseFloat(quantityValue);
            quantity += Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(0);
            this.quantity.setText(String.valueOf(quantity));
        });
        radioButtonMiddle.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "0.0";
            }
            float quantity = Float.parseFloat(quantityValue);
            quantity += Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(1);
            this.quantity.setText(String.valueOf(quantity));
        });
        radioButtonMax.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(this.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                quantityValue = "0.0";
            }
            float quantity = Float.parseFloat(quantityValue);
            quantity += Objects.requireNonNull(map.get(product.getUnit().toLowerCase())).get(2);
            this.quantity.setText(String.valueOf(quantity));
        });

        setOnShowListener(dialog -> {
            Window window = getWindow();
            if (window != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(window.getDecorView(), InputMethodManager.SHOW_IMPLICIT);
            }
        });

        setOnDismissListener(dialog -> {
            Window window = getWindow();
            if (window != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(window.getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
    }

    /**
     * Set up input type for quantity EditText
     */
    public void setOnlyFloatInput() {
        quantity.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                try {
                    float inputFloat = Float.parseFloat(input);
                    input = inputFloat < 9999.99 ? input : "9999.99";
                } catch (NumberFormatException e) {
                    if (!input.equals("")){
                        input = quantity.getTag() != null ? quantity.getTag().toString() : "";
                    }
                }
                if (!input.equals(Objects.requireNonNull(quantity.getText()).toString())) {
                    quantity.setText(input);
                    quantity.setSelection(input.length());
                }

                quantity.setTag(input);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // unused
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // unused
            }
        });
    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        acceptButton.setOnClickListener(listener);
    }
}
