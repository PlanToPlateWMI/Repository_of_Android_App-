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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import okhttp3.ResponseBody;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import retrofit2.Call;

import pl.plantoplate.databinding.EmailConfirmationBinding;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.getConfirmCode.ConfirmCodeCallback;

public class EmailConfirmActivity extends AppCompatActivity implements ApplicationStateController {

    private EmailConfirmationBinding email_confirm_view;

    private SharedPreferences prefs;

    private TextView email_info;
    private EditText enter_code;
    private Button confirm_button;
    private TextView resend_code_button;

    private String correct_code;

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the View Binding Library
        email_confirm_view = EmailConfirmationBinding.inflate(getLayoutInflater(), null, false);
        setContentView(email_confirm_view.getRoot());

        // get shared preferences
        prefs = getSharedPreferences("prefs", 0);

        // define ui elements
        email_info = email_confirm_view.skorzystajZLinku;
        enter_code = email_confirm_view.wprowadzKod.getEditText();
        confirm_button = email_confirm_view.buttonZatwierdzenieLink;
        resend_code_button = email_confirm_view.wyLijPono;

        // Set the email info text
        String email = prefs.getString("email", "");
        email_info.setText(email_info.getText() + "\n" + email);

        // add listener to the confirm button
        confirm_button.setOnClickListener(this::checkCode);

        // add listener to the resend button
        resend_code_button.setOnClickListener(this::getNewConfirmCode);

    }

    public void checkCode(View view) {
        String entered_code = Objects.requireNonNull(email_confirm_view.wprowadzKod.getEditText()).getText().toString();
        String correct_code = prefs.getString("code", "");
        if (correct_code.equals(entered_code)){
            // delete the code from the shared preferences
            prefs.edit().remove("code").apply();

            // start the group select activity
            Intent intent = new Intent(this, GroupSelectActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            saveAppState(ApplicationState.GROUP_CHOOSE);
        }
        else {
            Snackbar.make(view, "Wprowadzony kod jest niepoprawny", Snackbar.LENGTH_LONG).show();
        }
    }

    public void getNewConfirmCode(View view) {
        // Create a new Email object with the email from the shared preferences.
        String email = prefs.getString("email", "");

        // clear entered code
        enter_code.setText("");

        // Create a new retrofit call to send the user data to the server.
        Call<ResponseBody> myCall = RetrofitClient.getInstance().getApi().getConfirmCode(email);

        // Enqueue the call with a custom callback that handles the response.
        myCall.enqueue(new ConfirmCodeCallback(view));

        // make snackbar that informs the user that the code has been sent
        Snackbar.make(view, "Wysłano nowy kod", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void saveAppState(ApplicationState applicationState) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }
}
