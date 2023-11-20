package pl.plantoplate.data.remote.models.meal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Meal {

    @JsonProperty("mealId")
    private int id;
    private String recipeTitle;
    private int time;
    private MealType mealType;
    private String image;

    public Meal(){
    }

    public Meal(int id, String recipeTitle, int time, MealType mealType, String image) {
        this.id = id;
        this.recipeTitle = recipeTitle;
        this.time = time;
        this.mealType = mealType;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
