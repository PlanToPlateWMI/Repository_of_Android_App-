package pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl;

import android.app.Dialog;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import pl.plantoplate.R;

/*
    This class is responsible for showing pop-ups after clicking on the "Add to shopping list" button
 */
public class PopUpControlShoppingStart {

    /*
        This method shows pop-up with question about synchronization
     */
    public void showPopUpSynchronization(Context context) {

        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_question_synhronization_on);

        CheckBox checkBox = dialog.findViewById(R.id.checkBox);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {

            //wlaczanie synchronizacji
            if(checkBox.isChecked()){
                Toast.makeText(context, "Synchronizacja została włączona", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Synchronizacja nie została włączona", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(context, "Produkty zostały dodane do listy zakupów", Toast.LENGTH_SHORT).show();

            showPopUpQuestionAlsoAddToCalendar(context);
            dialog.dismiss();

        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(context, "Produkty nie zostały dodane do listy zakupów", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    /*
        This method shows pop-up with question about adding to calendar
     */
    public void showPopUpQuestionAlsoAddToCalendar(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_question_add_to_calendar);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {
            Toast.makeText(context, "Trwa planowanie...", Toast.LENGTH_SHORT).show();
            showPopUpPlanning(context);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(context, "Przepis nie został dodany do kalendarza", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    /*
        This method shows pop-up with information about planning
     */
    public void showPopUpPlanning(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_planing_calendar);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        //only if ONE OF calendar item is clicked
        acceptButton.setOnClickListener(v -> {
            Toast.makeText(context, "Przepis został dodany do kalendarza", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(context, "Przepis nie został dodany do kalendarza", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }


}
