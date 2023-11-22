package pl.plantoplate.ui.main.recipes.recipeInfo.popUps;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.databinding.NewPopUpNumberOfServingsPerRecipeBinding;
public class ChoosePortionsNumberPopUp extends DialogFragment {

    private MealPlan mealPlan;
    private TextView acceptButton;
    private TextView cancelButton;
    private TextInputEditText numberOfPortions;
    private View.OnClickListener listener;
    private ImageView plusButton;
    private ImageView minusButton;

    public ChoosePortionsNumberPopUp() {
    }

    public ChoosePortionsNumberPopUp(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpNumberOfServingsPerRecipeBinding binding = NewPopUpNumberOfServingsPerRecipeBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);

        setupViews(binding);
        setClickListeners();
        return dialog;
    }
    public void setupViews(NewPopUpNumberOfServingsPerRecipeBinding binding) {
        acceptButton = binding.zatwierdzenie;
        cancelButton = binding.close;
        numberOfPortions = binding.ilosc;
        plusButton = binding.plus;
        minusButton = binding.minus;

        numberOfPortions.setText("1");
    }

    public void setClickListeners() {
        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Liczba porcji nie została ustalona", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        acceptButton.setOnClickListener(v -> {
            if (numberOfPortions.getText().toString().length() == 0) {
                numberOfPortions.setError("Podaj liczbę porcji");
                return;
            }
            listener.onClick(v);
            int numberOfServingsInt = Integer.parseInt(Objects.requireNonNull(numberOfPortions.getText()).toString());
            mealPlan.setPortions(numberOfServingsInt);
            Toast.makeText(requireContext(), "Liczba porcji została ustalona", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        plusButton.setOnClickListener(v -> {
            if (numberOfPortions.getText().toString().length() == 0) {
                numberOfPortions.setError("Podaj liczbę porcji");
                return;
            }
            int numberOfServingsInt = Integer.parseInt(Objects.requireNonNull(numberOfPortions.getText()).toString());
            numberOfServingsInt++;
            numberOfPortions.setText(String.valueOf(numberOfServingsInt));
        });

        minusButton.setOnClickListener(v -> {
            if (numberOfPortions.getText().toString().length() == 0) {
                numberOfPortions.setError("Podaj liczbę porcji");
                return;
            }
            int numberOfServingsInt = Integer.parseInt(Objects.requireNonNull(numberOfPortions.getText()).toString());
            numberOfServingsInt--;
            numberOfPortions.setText(String.valueOf(numberOfServingsInt));
        });

        numberOfPortions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();

                if (!input.isEmpty()) {
                    try {
                        if (input.contains(".")) {
                            numberOfPortions.setText(input.replace(".", ""));
                        }

                        int portions = Integer.parseInt(s.toString());
                        if (portions < 1) {
                            numberOfPortions.setText("1");
                        }
                        if (portions > 10) {
                            numberOfPortions.setText("10");
                        }
                        numberOfPortions.setSelection(numberOfPortions.getText().length());
                    } catch (NumberFormatException ignored) {}
                }
            }
        });
    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }
}
