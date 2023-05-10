package pl.plantoplate.ui.main;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentBazaProduktowBinding;
import pl.plantoplate.ui.main.shoplist.KupioneFragment;
import pl.plantoplate.ui.main.shoplist.TrzebaKupicFragment;

public class BazaProduktowFragment extends Fragment {

    private FragmentBazaProduktowBinding bazaProduktowBinding;
    private SearchView searchView;
    //this list will have items (products)
    private  List<ClipData.Item> itemList;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        searchView.findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterText(newText);
                return true;
            }
        });

        bazaProduktowBinding = FragmentBazaProduktowBinding.inflate(inflater, container, false);
        replaceFragment(new WszystkieFragment());

        bazaProduktowBinding.bottomNavigationView2.setOnItemSelectedListener(item ->{
            switch (item.getItemId()) {
                case R.id.wlasne:
                    replaceFragment(new WlasneFragment());
                    return true;
                case R.id.wszystkie:
                    replaceFragment(new WszystkieFragment());
                    return true;
            }
            return false;
        });

        return bazaProduktowBinding.getRoot();
    }

    private void filterText(String text) {
        List<ClipData.Item> filteredList = new ArrayList<>();
        for(ClipData.Item item : itemList){
        }
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.baza_def, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}