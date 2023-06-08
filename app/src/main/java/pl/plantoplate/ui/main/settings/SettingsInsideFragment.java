package pl.plantoplate.ui.main.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentSettingsInsideBinding;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.main.settings.developerContact.MailDevelops;
import pl.plantoplate.ui.main.settings.accountManagement.ChangeTheData;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GroupCodeTypeActivity;

/**
 * The fragment that is displayed when the user clicks the settings button.
 */
public class SettingsInsideFragment extends Fragment{

    private FragmentSettingsInsideBinding settings_view;

    private Button generate_group_code_button;
    private Button exit_account_button;
    private Button button_zarzadyanie_uyztkownikamu;
    private Button button_zmiana_danych;
    private Button button_about_us;

    private Switch switchButton;
    Window window;
    private SharedPreferences prefs;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout using the View Binding Library
        settings_view = FragmentSettingsInsideBinding.inflate(inflater, container, false);

        // Get the buttons
        generate_group_code_button = settings_view.buttonWygenerowanieKodu;
        exit_account_button = settings_view.buttonWyloguj;
        button_zarzadyanie_uyztkownikamu = settings_view.buttonZarzadyanieUyztkownikamu;
        button_zmiana_danych = settings_view.buttonZmianaDanych;
        button_about_us = settings_view.buttonAboutUs;

        switchButton=settings_view.switchButtonChangeColorTheme;

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the switch state change
                if (isChecked) {
                    // Switch is on
                    int nightMode = AppCompatDelegate.MODE_NIGHT_YES;
                    AppCompatDelegate.setDefaultNightMode(nightMode);
                } else {
                    // Switch is off
                    int nightMode = AppCompatDelegate.MODE_NIGHT_NO;
                    AppCompatDelegate.setDefaultNightMode(nightMode);
                }

            }
        });

        // Get the shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // Set the onClickListeners for the buttons
        String role = prefs.getString("role", "");

        if(role.equals("ROLE_ADMIN")) {
            //generate_group_code_button.setOnClickListener(this::chooseGroupCodeType);
            generate_group_code_button.setOnClickListener(v -> replaceFragment(new GroupCodeTypeActivity()));
        }else {
            generate_group_code_button.setBackgroundColor(getResources().getColor(R.color.gray));
            generate_group_code_button.setClickable(false);
        }
        if(role.equals("ROLE_ADMIN")) {
            //button_zarzadyanie_uyztkownikamu.setOnClickListener(this::zarzadywanieUzytkownikami);
        }else {
            button_zarzadyanie_uyztkownikamu.setBackgroundColor(getResources().getColor(R.color.gray));
            button_zarzadyanie_uyztkownikamu.setClickable(false);
        }

        exit_account_button.setOnClickListener(this::exitAccount);
        button_zmiana_danych.setOnClickListener(v -> replaceFragment(new ChangeTheData()));
        button_about_us.setOnClickListener(v -> replaceFragment(new MailDevelops()));

        return settings_view.getRoot();
    }

    /**
     * Starts the GroupCodeTypeActivity.
     * @param view The view object that was clicked.
     */
    public void chooseGroupCodeType(View view) {
        Intent intent = new Intent(this.getContext(), GroupCodeTypeActivity.class);
        startActivity(intent);
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