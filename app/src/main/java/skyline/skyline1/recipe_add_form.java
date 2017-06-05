package skyline.skyline1;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Toast;

import java.util.ArrayList;

public class recipe_add_form extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private static final int GALLERY_KITKAT_INTENT_CALLED = 3;
    private static final int SUCCESS_VALUE = 2;
    ListView ingredientList;
    private Uri selectedImageUri;
    ArrayList mSelected;
    ArrayList<Ingredient> in_list = new ArrayList<>();
    ArrayAdapter<Ingredient> ingredientAdapter;
    ArrayAdapter<Ingredient> dialogAdapter;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(skyline.skyline1.R.layout.recipe_add_form_act);
        Toolbar toolbar = (Toolbar) findViewById(skyline.skyline1.R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }




        ListView lv = (ListView) findViewById(skyline.skyline1.R.id.ingredientList);
        lv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;




            }
        });





        ingredientAdapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_multiple_choice, DishMaker.getCookHelper().getIngredients());

        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, DishMaker.getCookHelper().getCategories());

        Spinner categorySpinner;
        categorySpinner = (Spinner) findViewById(skyline.skyline1.R.id.add_recipe_category_spinner);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<Country> originAdapter = new ArrayAdapter<Country>(this, android.R.layout.simple_spinner_item, DishMaker.getCookHelper().getCountries());

        Spinner originSpinner;
        originSpinner = (Spinner) findViewById(skyline.skyline1.R.id.add_recipe_origin_spinner);
        originSpinner.setAdapter(originAdapter);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                if(in_list.isEmpty()){

                }else{



                    in_list.remove(in_list.get(position));




                    dialogAdapter.notifyDataSetChanged();
                    ListView lv = (ListView) findViewById(skyline.skyline1.R.id.ingredientList);
                    setListViewHeightBasedOnChildren(lv);}


            }
        });




        lv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    public void onClickOpenGallery(View v) {

        if(Build.VERSION.SDK_INT < 19) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);

        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                ImageView imagetest = (ImageView) findViewById(skyline.skyline1.R.id.imageView2);
                imagetest.setImageURI(selectedImageUri);

            } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
                selectedImageUri = data.getData();
                int takeFlags = data.getFlags();
                takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
                ImageView imagetest = (ImageView) findViewById(skyline.skyline1.R.id.imageView2);
                imagetest.setImageURI(selectedImageUri);


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

                        dialogAdapter = new ArrayAdapter<Ingredient>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, in_list);

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
            iCookTime = Integer.parseInt(sPrepTime);
        }


        EditText description = (EditText) findViewById(skyline.skyline1.R.id.add_recipe_edittext_steps);
        String steps = description.getText().toString();

        Spinner categories = (Spinner) findViewById(skyline.skyline1.R.id.add_recipe_category_spinner);
        Category category = (Category) categories.getSelectedItem();

        Spinner origins = (Spinner) findViewById(skyline.skyline1.R.id.add_recipe_origin_spinner);
        Country country = (Country) origins.getSelectedItem();

        if (sRecipeName.isEmpty()) {
            Toast.makeText(recipe_add_form.this, "Your recipe needs a name!", Toast.LENGTH_LONG).show();
        } else {
            sRecipeName = (sRecipeName.substring(0, 1).toUpperCase() + sRecipeName.substring(1).toLowerCase());
        }

        ArrayList<Ingredient> listIngredientToAdd = new ArrayList<>();
        ListView listIngredients = (ListView) findViewById(skyline.skyline1.R.id.ingredientList);
        ArrayAdapter<Ingredient> inAdapter = (ArrayAdapter<Ingredient>)listIngredients.getAdapter();
        if (inAdapter == null) {
            Toast.makeText(recipe_add_form.this, "Your recipe cannot have 0 ingredient, please add at least one.", Toast.LENGTH_LONG).show();
        } else {
            for (int i = 0; i < inAdapter.getCount(); i++) {
                listIngredientToAdd.add(inAdapter.getItem(i));
            }
            Recipe newRecipe = new Recipe(iCookTime, iPrepTime, steps, sRecipeName, selectedImageUri, country, category, listIngredientToAdd); //create a new recipe
            added = DishMaker.getCookHelper().addRecipe(newRecipe);
        }






        if (added){
            Toast.makeText(recipe_add_form.this, "Saved", Toast.LENGTH_LONG).show();
            finish();

        }else{
            Toast.makeText(recipe_add_form.this, "Recipe could not be added", Toast.LENGTH_LONG).show();
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

}
