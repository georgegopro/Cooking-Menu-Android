package skyline.skyline1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Change_recipe extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private static final int SUCCESS_VALUE = 2;
    ListView ingredientList;
    private Uri selectedImageUri;
    ArrayList mSelected;
    private ArrayList<Ingredient> in_list;
    private ArrayAdapter<Ingredient> ingredientAdapter;

    private String name;
    private String prepTime;
    private String cookTime;
    private String origin;
    private String category;
    private String description;
    private String iconId;
    private String ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(skyline.skyline1.R.layout.edit_recipe_act);
        Toolbar toolbar = (Toolbar) findViewById(skyline.skyline1.R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        Intent intent = getIntent();

        prepTime = intent.getStringExtra("prepTime");
        name = intent.getStringExtra("name");
        cookTime = intent.getStringExtra("cookTime");
        category = intent.getStringExtra("category");
        origin = intent.getStringExtra("origin");
        description = intent.getStringExtra("description");
        iconId = intent.getStringExtra("picture");
        ingredients = intent.getStringExtra("ingredients");
        selectedImageUri = Uri.parse(iconId);




        EditText preparation = (EditText)findViewById(skyline.skyline1.R.id.recipe_add_preptime);
        preparation.setText(prepTime, TextView.BufferType.EDITABLE);


        EditText recipeName = (EditText)findViewById(skyline.skyline1.R.id.recipe_add_name);
        recipeName.setText(name, TextView.BufferType.EDITABLE);


        EditText cook = (EditText)findViewById(skyline.skyline1.R.id.recipe_add_cooktime);
        cook.setText(cookTime, TextView.BufferType.EDITABLE);




        ArrayAdapter<Country> originAdapter = new ArrayAdapter<Country>(this, android.R.layout.simple_spinner_item, DishMaker.getCookHelper().getCountries());
        ArrayList<Country> listCountry = DishMaker.getCookHelper().getCountries();
        Spinner originSpinner;
        originSpinner = (Spinner) findViewById(skyline.skyline1.R.id.add_recipe_origin_spinner);
        originSpinner.setAdapter(originAdapter);
        originSpinner.setSelection(listCountry.indexOf(origin));





        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, DishMaker.getCookHelper().getCategories());
        ArrayList<Category> listCat = DishMaker.getCookHelper().getCategories();
        Spinner categorySpinner;
        categorySpinner = (Spinner) findViewById(skyline.skyline1.R.id.add_recipe_category_spinner);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(listCat.indexOf(category));


        EditText text = (EditText)findViewById(skyline.skyline1.R.id.add_recipe_edittext_steps);
        text.setText(description, TextView.BufferType.EDITABLE);


        ImageView img= (ImageView) findViewById(skyline.skyline1.R.id.imageView2);
        img.setImageURI(Uri.parse(iconId));

        in_list = verifyIngredients();


        ingredientAdapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_multiple_choice, in_list);

        ingredientList = (ListView) findViewById(skyline.skyline1.R.id.ingredientList);
        ingredientList.setAdapter(ingredientAdapter);
        setListViewHeightBasedOnChildren(ingredientList);




        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                if(in_list.isEmpty()){

                }else{



                    in_list.remove(in_list.get(position));




                    ingredientAdapter.notifyDataSetChanged();
                    ListView lv = (ListView) findViewById(skyline.skyline1.R.id.ingredientList);
                    setListViewHeightBasedOnChildren(lv);}


            }
        });




        ingredientList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    public void onClickOpenGallery(View v) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                ImageView imagetest = (ImageView) findViewById(skyline.skyline1.R.id.imageView2);
                imagetest.setImageURI(selectedImageUri);
            } else if (requestCode == SUCCESS_VALUE) {
                ArrayList<Ingredient> listOfIngredients = new ArrayList<Ingredient>();
                ArrayAdapter<Ingredient> ingredientsAdapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_multiple_choice, listOfIngredients);
                Bundle b = getIntent().getParcelableExtra("test");
                listOfIngredients.add((Ingredient)data.getParcelableExtra("test"));
                ListView inList = (ListView) findViewById(skyline.skyline1.R.id.ingredientList);
                inList.setAdapter(ingredientsAdapter);


            }
        }
    }

    public String getPath(Uri uri) {

        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }

        return uri.getPath();
    }

    public void onClickOpenIngredientDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        mSelected = new ArrayList();
        final ArrayList<Ingredient> list = (ArrayList<Ingredient>) DishMaker.getCookHelper().getIngredients();
        ArrayList<String> stringIngredients = new ArrayList<>();

        Ingredient item;

        for(int i=0;i<list.size();i++)
        {
            item = list.get(i);
            stringIngredients.add(item.getName());

        }
        final CharSequence[] fol_list = stringIngredients.toArray(new CharSequence[stringIngredients.size()]);




        builder.setTitle("Select Ingredient")

                .setMultiChoiceItems(fol_list, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {

                                    mSelected.add(which);
                                } else if (mSelected.contains(which)) {

                                    mSelected.remove(Integer.valueOf(which));
                                }
                            }
                        })

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        for (int i = 0; i < mSelected.size(); i++){
                            in_list.add(list.get((int)mSelected.get(i)));
                        }

                        ArrayAdapter<Ingredient> dialogAdapter = new ArrayAdapter<Ingredient>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, in_list);

                        ListView ingredientList = (ListView) findViewById(skyline.skyline1.R.id.ingredientList);
                        ingredientList.setAdapter(dialogAdapter);
                        setListViewHeightBasedOnChildren(ingredientList);



                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }


    public void onClickSaveRecipe(View v) {
        boolean added = false;
        Recipe recipe = null;
        boolean found = false;
        if (true) {
            ArrayList<Recipe> recipeList = DishMaker.getCookHelper().getRecipes();
            for (int i = 0; i < DishMaker.getCookHelper().getRecipes().size(); i++) {
                if (name.toLowerCase().equals(recipeList.get(i).getName().toLowerCase())) {
                    recipe = recipeList.get(i);
                    found = true;
                    break;
                }
            }
            if (found) {


                EditText recipeName = (EditText) findViewById(skyline.skyline1.R.id.recipe_add_name);
                String sRecipeName = recipeName.getText().toString();


                EditText prepTime = (EditText) findViewById(skyline.skyline1.R.id.recipe_add_preptime);
                String sPrepTime = prepTime.getText().toString();
                int iPrepTime;
                if (sPrepTime.isEmpty()) {
                    iPrepTime = 0;
                } else {
                    iPrepTime = Integer.parseInt(sPrepTime);
                }

                EditText cookTime = (EditText) findViewById(skyline.skyline1.R.id.recipe_add_cooktime);
                String sCookTime = cookTime.getText().toString();
                int iCookTime;
                if (sCookTime.isEmpty()) {
                    iCookTime = 0;
                } else {
                    iCookTime = Integer.parseInt(sCookTime);
                }


                EditText description = (EditText) findViewById(skyline.skyline1.R.id.add_recipe_edittext_steps);
                String steps = description.getText().toString();

                Spinner categories = (Spinner) findViewById(skyline.skyline1.R.id.add_recipe_category_spinner);
                Category category = (Category) categories.getSelectedItem();

                Spinner origins = (Spinner) findViewById(skyline.skyline1.R.id.add_recipe_origin_spinner);
                Country country = (Country) origins.getSelectedItem();

                ArrayList<Ingredient> listIngredientToAdd = new ArrayList<>();
                ListView listIngredients = (ListView) findViewById(skyline.skyline1.R.id.ingredientList);
                ArrayAdapter<Ingredient> inAdapter = (ArrayAdapter<Ingredient>)ingredientList.getAdapter();
                if (in_list.isEmpty()) {
                    Toast.makeText(Change_recipe.this, "Your recipe cannot have 0 ingredient, please add at least one.", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < inAdapter.getCount(); i++) {
                        listIngredientToAdd.add(in_list.get(i));

                    }
                    if (sRecipeName.isEmpty()) {
                        Toast.makeText(Change_recipe.this, "Your recipe needs a name!", Toast.LENGTH_LONG).show();
                    } else {
                        sRecipeName = (sRecipeName.substring(0, 1).toUpperCase() + sRecipeName.substring(1).toLowerCase());
                        DishMaker.getCookHelper().removeRecipe(recipe);
                        Recipe newRecipe = new Recipe(iCookTime, iPrepTime, steps, sRecipeName, selectedImageUri, country, category, listIngredientToAdd);//Create the new recipe
                        added = DishMaker.getCookHelper().addRecipe(newRecipe);
                    }

                }


                if (added) {
                    Toast.makeText(Change_recipe.this, "Recipe SuccessFully Modified.", Toast.LENGTH_LONG).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", recipeName.getText().toString());
                    resultIntent.putExtra("prepTime", prepTime.getText().toString());
                    resultIntent.putExtra("cookTime", cookTime.getText().toString());
                    resultIntent.putExtra("country", country.getName());
                    resultIntent.putExtra("category", category.getName());
                    resultIntent.putExtra("picture", selectedImageUri.toString());
                    resultIntent.putExtra("description", steps);

                    String ingredients = null ;
                    for (int y =0; y<in_list.size(); y++) {
                        if (y == 0) {
                            ingredients = in_list.get(y).getName();
                        } else {
                            ingredients = ingredients + "\n" + in_list.get(y).getName();
                        }
                    }
                    resultIntent.putExtra("ingredients", ingredients);

                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                }
            }
        } else {
            Toast.makeText(Change_recipe.this, "Modification Failed.", Toast.LENGTH_LONG).show();
        }
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, Toolbar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public ArrayList<Ingredient> verifyIngredients(){
        ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
        String received = ingredients.toString();
        String[] ingredientStringList = received.trim().split("\n");
        for (int i = 0; i < ingredientStringList.length; i++){
            if (DishMaker.getCookHelper().findIngredient(ingredientStringList[i])!=null){
                ingredientList.add(DishMaker.getCookHelper().findIngredient(ingredientStringList[i]));
            }else {
                Toast.makeText(Change_recipe.this, "There is an error.", Toast.LENGTH_SHORT);
            }
        }
        return ingredientList;
    }



}

