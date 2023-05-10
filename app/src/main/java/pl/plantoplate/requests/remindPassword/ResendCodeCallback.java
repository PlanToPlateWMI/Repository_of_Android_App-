package pl.plantoplate.requests.remindPassword;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.getConfirmCode.ConfirmCodeResponse;
import pl.plantoplate.ui.login.remindPassword.EnterCodeActivity;
import pl.plantoplate.ui.registration.EmailConfirmActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A callback class for the API request to retrieve a confirmation code for password remind process (if was problems with getting code
 * for the first time).
 */
public class ResendCodeCallback implements Callback<ResponseBody> {

    // View object to display the Snackbar
    private final View view;
    // The user's email
    private final String email;

    /**
     * Constructor to create a new ConfirmCodeCallback object.
     * @param view The view object to display the Snackbar.
     * @param email The user's email.
     */
    public ResendCodeCallback(View view, String email) {
        this.view = view;
        this.email = email;
    }

    /**
     * Handles the API response.
     * @param call The API call object.
     * @param response The API response object.
     */
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

        if (response.isSuccessful()) {

            // If the response body is null, display a Snackbar and return
            if (response.body() == null) {
                Snackbar.make(view, "Coś poszło nie tak!", Snackbar.LENGTH_LONG).show();
                return;
            }

            // If the response body is not null, parse the response body to CodeResponse and restart the EnterCodeActivity.
            try {
                ConfirmCodeResponse code = new Gson().fromJson(response.body().string(), ConfirmCodeResponse.class);
                Intent intent = new Intent(view.getContext(), EnterCodeActivity.class);
                intent.putExtra("code", code.getCode());
                view.getContext().startActivity(intent);

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
        Snackbar.make(view, "Błąd, sprawdź swoje połączenie internetowe!", Snackbar.LENGTH_LONG).show();
    }

    /**
     * Handles the API server error responses.
     * @param code The error code.
     */
    private void handleErrorResponse(int code) {
        switch (code) {
            case 409:
                Snackbar.make(view, "Użytkownik o podanym adresie email nie istnieje!", Snackbar.LENGTH_LONG).show();
                break;
            case 500:
                Snackbar.make(view, "Błąd serwera!", Snackbar.LENGTH_LONG).show();
                break;
            default:
                Snackbar.make(view, "Nieznana odpowiedź serwera!", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

}
