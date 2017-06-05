package skyline.skyline1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class act_add_Ingredient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add__ingredient_act);

    }
    public void onClickSaveIngredient(View v){
        boolean added;
        EditText edit =  (EditText) findViewById(R.id.addIngredientName);
        String ingredient = edit.getText().toString();
        int[] capsLocation = new int[ingredient.length()];
        capsLocation[0] = 1;
        char[] ingredientChar = ingredient.toCharArray();
        for (int i = 0; i<ingredientChar.length; i++){
            char c = ingredientChar[i];
            if(c == ' '){
                ingredientChar[i] = '-';
                capsLocation[i+1] = 1;
            }
        }

        ingredient = String.valueOf(ingredientChar).toLowerCase();
        String ingredients = new String();
        for (int y = 0; y< capsLocation.length; y++){
            if (capsLocation[y]==1){
                ingredients = ingredients + ingredient.substring(y,y+1).toUpperCase();
            }else{
                ingredients = ingredients + ingredient.substring(y,y+1).toLowerCase();
            }
        }
        Ingredient newIngredient= new Ingredient(ingredients);

        added = DishMaker.getCookHelper().addIngredient(newIngredient);

        if (added == true){
            Toast.makeText(act_add_Ingredient.this, "Saved", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(act_add_Ingredient.this, "Ingredient already exists.", Toast.LENGTH_LONG).show();
        }
    }
}
