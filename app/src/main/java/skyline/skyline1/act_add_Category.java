package skyline.skyline1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class act_add_Category extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add__category_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    public void onClickSaveCategory(View v){
        boolean added;
        EditText edit =  (EditText) findViewById(R.id.inputNewCategory);
        String category = edit.getText().toString();

        int[] capsLocation = new int[category.length()];
        capsLocation[0] = 1;
        char[] ingredientChar = category.toCharArray();
        for (int i = 0; i<ingredientChar.length; i++){
            char c = ingredientChar[i];
            if(c == ' '){
                capsLocation[i+1] = 1;
            }
        }

        category = String.valueOf(ingredientChar).toLowerCase();
        String categoryString = new String();
        for (int y = 0; y< capsLocation.length; y++){
            if (capsLocation[y]==1){
                categoryString = categoryString + category.substring(y,y+1).toUpperCase();
            }else{
                categoryString = categoryString + category.substring(y,y+1).toLowerCase();
            }
        }

        Category newCategory= new Category(categoryString);

        added = DishMaker.getCookHelper().addCategory(newCategory);

        if (added == true){
            Toast.makeText(act_add_Category.this, "Saved", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(act_add_Category.this, "Category already exists.", Toast.LENGTH_LONG).show();
        }
    }

}
