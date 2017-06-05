package skyline.skyline1;

/**
 * Created by shanelgauthier on 16-11-20.
 */

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Recipe implements Serializable {
    private String name;
    private String description;
    private Uri iconId;
    private int preTime;
    private int cookTime;
    private Country country;
    private Category category;
    private ArrayList<Ingredient> ingredients;
    private static final long serialVersionUID = 1L;


    public Recipe(int cookTime, int preTime, String description, String name, Uri iconId, Country country, Category category, ArrayList<Ingredient> ingredients ) {
        this.cookTime = cookTime;
        this.preTime = preTime;
        this.iconId = iconId;
        this.description = description;
        this.name = name;
        this.country = country;
        this.category = category;
        this.ingredients=ingredients;
    }



    public Boolean hasIngredient(Ingredient ingredient){
        Boolean flag=false;
        for(int i=0 ; i<ingredients.size() ; i++){
            if(ingredients.get(i)==ingredient){
                flag=true;
                break;
            }
        }
        return flag;
    }



    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPreTime() {
        return preTime;
    }

    public void setPreTime(int preTime) {
        this.preTime = preTime;
    }

    public Uri getIconId() {
        return iconId;
    }

    public void setIconId(Uri iconId) {
        this.iconId = iconId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    private void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
    }

    private boolean removeIngredient(Ingredient ingredient){
         return ingredients.remove(ingredient);

    }

    public static Comparator<Recipe> COMPARE_BY_Recipe = new Comparator<Recipe>() {
        public int compare(Recipe one, Recipe other) {
            return one.getName().compareTo(other.getName());
        }
    };}



