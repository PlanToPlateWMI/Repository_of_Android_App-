package pl.plantoplate.ui.main.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentSettingsInsideBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.models.UserInfo;
import pl.plantoplate.repository.remote.user.UserRepository;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.main.settings.changePermissions.ChangePermissionsFragment;
import pl.plantoplate.ui.main.settings.developerContact.MailDevelops;
import pl.plantoplate.ui.main.settings.accountManagement.ChangeTheData;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GroupCodeTypeActivity;
import pl.plantoplate.ui.main.settings.viewModels.SettingsViewModel;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.ui.main.storage.StorageViewModel;

/**
 * The fragment that is displayed when the user clicks the settings button.
 */
public class SettingsInsideFragment extends Fragment{

    private FragmentSettingsInsideBinding settings_view;
    private SettingsViewModel settingsViewModel;

    private TextView username;
    private Button generate_group_code_button;
    private Button exit_account_button;
    private Button button_zarzadzanie_uyztkownikamu;
    private Button button_zmiana_danych;
    private Button button_about_us;
    private Switch switchButton;
    private UserRepository userRepository;

    private SharedPreferences prefs;
    private int userCount;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout using the View Binding Library
        settings_view = FragmentSettingsInsideBinding.inflate(inflater, container, false);

        // Get views
        generate_group_code_button = settings_view.buttonWygenerowanieKodu;
        exit_account_button = settings_view.buttonWyloguj;
        button_zarzadzanie_uyztkownikamu = settings_view.buttonZarzadyanieUyztkownikamu;
        button_zmiana_danych = settings_view.buttonZmianaDanych;
        button_about_us = settings_view.buttonAboutUs;
        username = settings_view.imie;
        switchButton=settings_view.switchButtonChangeColorTheme;
        userRepository = new UserRepository();

        // Get the shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        switchButton=settings_view.switchButtonChangeColorTheme;

        SharedPreferences.Editor editor = prefs.edit();

        String key = "theme";

        // depends on SharedPreferences key set checked of Switch button
        switch (prefs.getString(key, "")) {
            case "dark":
                switchButton.setChecked(true);
                break;

            case "light":
                switchButton.setChecked(false);
                break;
        }


        // switch listener - change theme and change key of SharedPreferences
        switchButton.setOnClickListener(view -> {
            if (switchButton.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // set dark mode
                editor.putString("theme", "dark");
                editor.apply();

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // set light mode
                editor.putString("theme", "light");
                editor.apply();

            }
        });

        // Set the onClickListeners for the buttons
        String role = prefs.getString("role", "");
        String token = "Bearer " + prefs.getString("token", "");

        userRepository.getUsersInfo(token, new ResponseCallback<ArrayList<UserInfo>>() {

            @Override
            public void onSuccess(ArrayList<UserInfo> response) {
                userCount = response.size();
                if(role.equals("ROLE_ADMIN") && userCount > 1) {
                    button_zarzadzanie_uyztkownikamu.setOnClickListener(v -> replaceFragment(new ChangePermissionsFragment()));
                }else {
                    button_zarzadzanie_uyztkownikamu.setBackgroundColor(getResources().getColor(R.color.gray));
                    button_zarzadzanie_uyztkownikamu.setClickable(false);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                checkUsers(role);
            }

            @Override
            public void onFailure(String failureMessage) {
                System.out.println(failureMessage);
                checkUsers(role);
            }
        });

        if(role.equals("ROLE_ADMIN")) {
            //generate_group_code_button.setOnClickListener(this::chooseGroupCodeType);
            generate_group_code_button.setOnClickListener(v -> replaceFragment(new GroupCodeTypeActivity()));
        }else {
            generate_group_code_button.setBackgroundColor(getResources().getColor(R.color.gray));
            generate_group_code_button.setClickable(false);
        }

        exit_account_button.setOnClickListener(this::exitAccount);
        button_zmiana_danych.setOnClickListener(v -> replaceFragment(new ChangeTheData()));
        button_about_us.setOnClickListener(v -> replaceFragment(new MailDevelops()));

        setUpViewModel();

        return settings_view.getRoot();
    }

    public void checkUsers(String role) {
        if(role.equals("ROLE_ADMIN")) {
            button_zarzadzanie_uyztkownikamu.setOnClickListener(v -> replaceFragment(new ChangePermissionsFragment()));
        }else {
            button_zarzadzanie_uyztkownikamu.setBackgroundColor(getResources().getColor(R.color.gray));
            button_zarzadzanie_uyztkownikamu.setClickable(false);
        }
    }

    public void setUpViewModel() {

        // get storage view model
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        // get storage title
        settingsViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
            Spannable spans = new SpannableString("Imię: " + userInfo.getUsername() + "\n" + "Email: " + userInfo.getEmail());
            username.setText(spans);
        });

        // get success message
        settingsViewModel.getSuccess().observe(getViewLifecycleOwner(), successMessage -> {
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), successMessage, Toast.LENGTH_SHORT).show());
            }
        });

        // get error message
        settingsViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * Logs the user out of the app.
     * @param view The view object that was clicked.
     */
    public void exitAccount(View view) {

        //delete the user's data from the shared preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("name");
        editor.remove("email");
        editor.remove("password");
        editor.remove("role");
        editor.remove("token");
        editor.apply();

        //go back to the login screen
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        // save the app state
        saveAppState(ApplicationState.LOGIN);
    }

    /**
     * Saves the app state to the shared preferences.
     * @param applicationState The app state to save.
     */
    public void saveAppState(ApplicationState applicationState) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }

    /**
     * Replaces the current fragment with the specified fragment.
     * @param fragment The fragment to replace the current fragment with.
     */
    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_default, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
