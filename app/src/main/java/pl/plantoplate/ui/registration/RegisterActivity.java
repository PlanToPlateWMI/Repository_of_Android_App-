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
package pl.plantoplate.ui.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import pl.plantoplate.LoginActivity;
import pl.plantoplate.requests.joinGroup.sendRegisterData.SendRegisterDataCallback;
import pl.plantoplate.requests.joinGroup.sendRegisterData.UserRegisterData;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.databinding.RegisterActivityBinding;
import pl.plantoplate.tools.EmailValidator;
import pl.plantoplate.tools.SCryptStretcher;
import com.google.android.material.snackbar.Snackbar;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * An activity for user registration.
 */
public class RegisterActivity extends AppCompatActivity {

    private RegisterActivityBinding register_view;
    private EditText enter_name;
    private EditText enter_email;
    private EditText enter_password;
    private CheckBox apply_policy;
    private Button sign_in_button;
    private Button register_button;

    private SharedPreferences preferences;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                               shut down then this Bundle contains the data it most recently
     *                               supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the View Binding Library
        register_view = RegisterActivityBinding.inflate(getLayoutInflater());
        setContentView(register_view.getRoot());

        // Find the views in the layout
        enter_name = register_view.enterName;
        enter_email = register_view.enterEmail;
        enter_password = register_view.enterPassword;
        apply_policy = register_view.checkboxWyrazamZgode;
        register_button = register_view.buttonZalozKonto;
        sign_in_button = register_view.buttonZalogujSie;

        // get shared preferences
        preferences = getSharedPreferences("prefs", 0);

        // Set the click listener for the register button
        register_button.setOnClickListener(this::validateUserInfo);
        sign_in_button.setOnClickListener(v -> signInAccount());
    }

    /**
     * Get the user's information from the input fields.
     *
     * @return A UserInfo object containing the user's information.
     */
    public UserRegisterData getUserInfo(){
        String name = String.valueOf(enter_name.getText());
        String email = String.valueOf(enter_email.getText());
        String password = String.valueOf(enter_password.getText());
        return new UserRegisterData(name, email, password);
    }

    /**
     * Validate the user's information and show a Snackbar message if any errors are found.
     *
     * @param view The view that was clicked.
     */
    public void validateUserInfo(View view){
        UserRegisterData info = getUserInfo();

        if(info.getUsername() == null || info.getUsername().isEmpty()){
            Snackbar.make(view, "Wprowadż imię użytkownika!", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(!EmailValidator.isEmail(info.getEmail())){
            Snackbar.make(view, "Email jest niepoprawny!", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(info.getPassword() == null || info.getPassword().length() < 7){
            Snackbar.make(view, "Hasło musi być długie (co najmniej 7 znaków)", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (apply_policy.isChecked()){

            //stretch password to make it unreadable and secure
            info.setPassword(SCryptStretcher.stretch(info.getPassword(), info.getEmail()));
            sendUserData(info, view);
        }
        else{
            Snackbar.make(view, "Musisz wyrazić zgodę na przetwarzanie danych osobowych", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Sends the user data to the server and handles the response asynchronously.
     * @param data The user data to send.
     * @param view The View to display the response in (e.g. error using SnackBar).
     */
    public void sendUserData(UserRegisterData data, View view) {
        // Create a new retrofit call to send the user data to the server.
        Call<ResponseBody> myCall = RetrofitClient.getInstance().getApi().sendUserRegisterData(data);

        // Enqueue the call with a custom callback that handles the response.
        myCall.enqueue(new SendRegisterDataCallback(view, data));
    }

    /**
     * Starts the LoginActivity to allow the user to sign in.
     */
    public void signInAccount() {
        // Create an intent to start the RegisterActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}