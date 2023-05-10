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
package pl.plantoplate.requests.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.plantoplate.ui.main.ActivityMain;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A callback class for the API request to sign in the user.
 */
public class SignInCallback implements Callback<ResponseBody> {

    // View object to display the Snackbar
    private final View view;
    // SharedPreferences object to store the user's email
    private final SharedPreferences prefs;

    /**
     * Constructor to create a new SignInCallback object.
     * @param view The view object to display the Snackbar.
     */
    public SignInCallback(View view) {
        this.view = view;
        this.prefs = view.getContext().getSharedPreferences("prefs", 0);
    }

    /**
     * Handles the API response.
     * @param call The API call object.
     * @param response The API response object.
     */
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

        System.out.println(response.code());

        if (response.isSuccessful()) {

            // If the response body is null, display a Snackbar and return
            if (response.body() == null) {
                Snackbar.make(view, "Coś poszło nie tak!", Snackbar.LENGTH_LONG).show();
                return;
            }

            // If the response body is not null, parse the response body to JwtResponse object, save user token and role
            // to SharedPreferences and start the MainActivity
            try {
                JwtResponse jwt = new Gson().fromJson(response.body().string(), JwtResponse.class);

                // Save the user's token and role to SharedPreferences
                saveTokenAndRole(jwt.getToken(), jwt.getRole());

                // Start the MainActivity
                view.getContext().startActivity(new Intent(view.getContext(), ActivityMain.class));

            } catch (IOException e) {
                Snackbar.make(view, "Coś poszło nie tak!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            handleErrorResponse(response.code());
        }
    }

    /**
     * Handles the API call failure.
     * @param call The API call object.
     * @param t The throwable object.
     */
    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        Snackbar.make(view, "Błąd podczas wysyłania danych!", Snackbar.LENGTH_LONG).show();
    }

    /**
     * Handles the API server error responses.
     * @param code The error code.
     */
    private void handleErrorResponse(int code) {
        switch (code) {
            case 400:
                Snackbar.make(view, "Użytkownik o podanym adresie email nie istnieje!", Snackbar.LENGTH_LONG).show();
                break;
            case 403:
                Snackbar.make(view, "Niepoprawne hasło!", Snackbar.LENGTH_LONG).show();
                break;
            case 500:
                Snackbar.make(view, "Błąd serwera!", Snackbar.LENGTH_LONG).show();
                break;
            default:
                Snackbar.make(view, "Nieznana odpowiedź serwera!", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * Saves the user's token and role to SharedPreferences.
     * @param token The user's token.
     * @param role The user's role.
     */
    public void saveTokenAndRole(String token, String role) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.putString("role", role);
        editor.apply();
    }

}