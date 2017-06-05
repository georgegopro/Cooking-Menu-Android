package skyline.skyline1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeView extends AppCompatActivity {
    private String name;
    private String prepTime;
    private String cookTime;
    private String origin;
    private String category;
    private String description;
    private String iconId;
    private String ingredients;
    private static final int RECIPE_EDIT = 2002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(skyline.skyline1.R.layout.view_act);
        Toolbar toolbar = (Toolbar) findViewById(skyline.skyline1.R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(skyline.skyline1.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            Intent i = new Intent(RecipeView.this, Change_recipe.class);
            i.putExtra("prepTime", prepTime);
            i.putExtra("name", name);
            i.putExtra("cookTime", cookTime);
            i.putExtra("category", category);
            i.putExtra("origin", origin);
            i.putExtra("description", description);
            i.putExtra("picture", iconId);

            i.putExtra("ingredients", ingredients);


            startActivityForResult(i, RECIPE_EDIT);

                    }
        });
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        prepTime = intent.getStringExtra("prepTime");
        name = intent.getStringExtra("name");
        cookTime =intent.getStringExtra("cookTime");
        category = intent.getStringExtra("category");
        origin = intent.getStringExtra("origin");
        description = intent.getStringExtra("description");
        iconId = intent.getStringExtra("picture");
        if(iconId.isEmpty()) {
            iconId = "android.resource://skyline.skyline1/drawable/defimage";
        }
        ingredients = intent.getStringExtra("ingredients");


        TextView preparation = (TextView)findViewById(skyline.skyline1.R.id.recipe_view_value_preptime);
        preparation.setText(prepTime + " minutes");


        TextView recipeName = (TextView)findViewById(skyline.skyline1.R.id.recipe_view_title);
        recipeName.setText(name);


        TextView cook = (TextView)findViewById(skyline.skyline1.R.id.recipe_view_value_cooktime);
        cook.setText(cookTime + " minutes");



        TextView type = (TextView)findViewById(skyline.skyline1.R.id.recipe_view_value_origin);
        type.setText(origin);



        TextView categorie = (TextView)findViewById(skyline.skyline1.R.id.recipe_view_value_category);
        categorie.setText(category);


        TextView text = (TextView)findViewById(skyline.skyline1.R.id.textView10);
        text.setText(description);


        ImageView img= (ImageView) findViewById(skyline.skyline1.R.id.defaultRecipeImage);
        img.setImageURI(Uri.parse(iconId));

        TextView ingredientList = (TextView) findViewById(skyline.skyline1.R.id.recipe_value_text_ingredients);
        ingredientList.setText(ingredients);



        // ------------------------------------------

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(skyline.skyline1.R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(skyline.skyline1.R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(name);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RECIPE_EDIT) {
                prepTime = data.getStringExtra("prepTime");
                name = data.getStringExtra("name");
                cookTime =data.getStringExtra("cookTime");
                category = data.getStringExtra("category");
                origin = data.getStringExtra("origin");
                description = data.getStringExtra("description");
                iconId = data.getStringExtra("picture");
                ingredients = data.getStringExtra("ingredients");


                TextView preparation = (TextView)findViewById(skyline.skyline1.R.id.recipe_view_value_preptime);
                preparation.setText(prepTime + " minutes");


                TextView recipeName = (TextView)findViewById(skyline.skyline1.R.id.recipe_view_title);
                recipeName.setText(name);


                TextView cook = (TextView)findViewById(skyline.skyline1.R.id.recipe_view_value_cooktime);
                cook.setText(cookTime + " minutes");



                TextView type = (TextView)findViewById(skyline.skyline1.R.id.recipe_view_value_origin);
                type.setText(origin);



                TextView categorie = (TextView)findViewById(skyline.skyline1.R.id.recipe_view_value_category);
                categorie.setText(category);


                TextView text = (TextView)findViewById(skyline.skyline1.R.id.textView10);
                text.setText(description);


                ImageView img= (ImageView) findViewById(skyline.skyline1.R.id.defaultRecipeImage);
                img.setImageURI(Uri.parse(iconId));

                TextView ingredientList = (TextView) findViewById(skyline.skyline1.R.id.recipe_value_text_ingredients);
                ingredientList.setText(ingredients);

            }
        }
    }
}
