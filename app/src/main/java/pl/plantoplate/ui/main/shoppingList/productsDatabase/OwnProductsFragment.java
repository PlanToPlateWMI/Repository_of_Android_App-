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
package pl.plantoplate.ui.main.shoppingList.productsDatabase;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentWlasneBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.product.ProductRepository;
import pl.plantoplate.repository.remote.shoppingList.ShoppingListRepository;
import pl.plantoplate.repository.models.Product;
import pl.plantoplate.ui.main.shoppingList.ShoppingListFragment;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.product.ProductAdapter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.popups.AddToCartPopUp;

public class OwnProductsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private FragmentWlasneBinding fragmentWlasneBinding;

    private FloatingActionButton floatingActionButton_wlasne;
    private RecyclerView ownProductsRecyclerView;
    private TextView welcomeTextView;
    private SearchView searchView;

    private SharedPreferences prefs;
    private ProductRepository productRepository;

    private ArrayList<Product> groupProductsList;

    @Override
    public void onResume() {
        super.onResume();
        getProducts();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentWlasneBinding = FragmentWlasneBinding.inflate(inflater, container, false);

        // Setup views
        floatingActionButton_wlasne = fragmentWlasneBinding.floatingActionButtonWlasne;
        welcomeTextView = fragmentWlasneBinding.welcomeWlasne;
        searchView = requireActivity().findViewById(R.id.search);

        // Setup listeners
        floatingActionButton_wlasne.setOnClickListener(v -> replaceFragment(new AddYourOwnProductFragment()));
        searchView.setOnQueryTextListener(this);

        // Get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // Get product repository
        productRepository = new ProductRepository();

        setupRecyclerView();
        return fragmentWlasneBinding.getRoot();
    }

    private void getProducts(){
        String token = "Bearer " + prefs.getString("token", "");
        productRepository.getOwnProducts(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                groupProductsList = CategorySorter.sortProductsByName(products);

                updateRecyclerView();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void addProductToShoppingList(Product product) {
        String token = "Bearer " + prefs.getString("token", "");
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
        shoppingListRepository.addProductToShopList(token, product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), "Dodano produkt do listy zakupów", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        ArrayList<Product> filteredProducts = CategorySorter.filterProductsBySearch(groupProductsList, query);
        updateRecyclerView(filteredProducts);
        return false;
    }

    private void updateRecyclerView() {
        if (groupProductsList.isEmpty()) {
            welcomeTextView.setVisibility(View.VISIBLE);
        } else {
            welcomeTextView.setVisibility(View.GONE);
        }
        ProductAdapter productAdapter = (ProductAdapter) ownProductsRecyclerView.getAdapter();
        Objects.requireNonNull(productAdapter).setProductsList(this.groupProductsList);
    }

    private void updateRecyclerView(ArrayList<Product> filteredProducts) {
        ProductAdapter productAllAdapter = (ProductAdapter) ownProductsRecyclerView.getAdapter();
        Objects.requireNonNull(productAllAdapter).setProductsList(filteredProducts);
    }

    public void showAddProductPopup(Product product) {
        product.setAmount(1);
        AddToCartPopUp addToCartPopUp = new AddToCartPopUp(requireContext(), product);
        addToCartPopUp.acceptButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(addToCartPopUp.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                addToCartPopUp.quantity.setError("Podaj ilość");
                return;
            }
            if (quantityValue.endsWith(".")) {
                // Remove dot at the end
                quantityValue = quantityValue.substring(0, quantityValue.length() - 1);
            }
            float quantity = BigDecimal.valueOf(Float.parseFloat(quantityValue)).setScale(3, RoundingMode.HALF_UP).floatValue();
            product.setAmount(quantity);
            addProductToShoppingList(product);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ShoppingListFragment()).commit();
            addToCartPopUp.dismiss();
        });
        addToCartPopUp.show();
    }

    private void setupRecyclerView() {
        ownProductsRecyclerView = fragmentWlasneBinding.productsOwnRecyclerView;
        ownProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter productAllAdapter = new ProductAdapter(groupProductsList, R.layout.item_wlasny_produkt);
        productAllAdapter.setOnProductItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onAddToShoppingListButtonClick(View v, Product product) {
                product.setAmount(1.0f);
                addProductToShoppingList(product);
                replaceFragment(new ShoppingListFragment());
            }

            @Override
            public void onEditProductButtonClick(View v, Product product) {
                replaceFragment(new EditOwnProductFragment(product));
            }

            @Override
            public void onProductItemClick(View v, Product product) {
                showAddProductPopup(product);
            }
        });
        ownProductsRecyclerView.setAdapter(productAllAdapter);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
