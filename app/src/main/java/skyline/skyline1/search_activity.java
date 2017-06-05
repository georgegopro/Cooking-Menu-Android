package skyline.skyline1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static skyline.skyline1.R.layout.food_ingredient;

public class search_activity extends AppCompatActivity {
    private ArrayAdapter<Recipe> adapter;
    private ListView list;
    private ArrayList<String> searchBools;
    private ArrayList<Ingredient> searchIngredients;
    private ArrayList<String> numbreOr;
    private ArrayList<Recipe> recherche;
    private ArrayList<Integer> recipesOr;
    private Integer numberOfOrs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(skyline.skyline1.R.layout.search_activity_act);
        Toolbar toolbar = (Toolbar) findViewById(skyline.skyline1.R.id.toolbar);
        setSupportActionBar(toolbar);





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, DishMaker.getCookHelper().getCategories());

        Spinner categorySpinner;
        categorySpinner = (Spinner) findViewById(skyline.skyline1.R.id.category_search);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<Country> originAdapter = new ArrayAdapter<Country>(this, android.R.layout.simple_spinner_item, DishMaker.getCookHelper().getCountries());

        Spinner originSpinner;
        originSpinner = (Spinner) findViewById(skyline.skyline1.R.id.origin_search);
        originSpinner.setAdapter(originAdapter);




        Button button = (Button) findViewById(skyline.skyline1.R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                EditText search = (EditText) findViewById(skyline.skyline1.R.id.text_ingredients);
                String ingredients = search.getText().toString();


                Spinner categories = (Spinner) findViewById(skyline.skyline1.R.id.category_search);
                Category category = (Category) categories.getSelectedItem();

                Spinner origins = (Spinner) findViewById(skyline.skyline1.R.id.origin_search);
                Country country = (Country) origins.getSelectedItem();
                boolean flag = true;

                try{
                    if(!ingredients.matches("")){
                        flag =readIngredients(ingredients);

                    }else{
                        searchBools=null;
                        searchIngredients=null;
                    }
                    if(!flag){
                        Toast.makeText(search_activity.this, "A component of the string was misspelled or did not exist" , Toast.LENGTH_LONG).show();
                    }

                    recherche = DishMaker.getCookHelper().search(category, country, searchIngredients, searchBools); //calls the search
                    ArrayList<Ingredient>  orIngredients= getOrIngredients(searchBools, searchIngredients); //lists or ingredients
                    if(orIngredients!=null){
                        numberOfOrs=orIngredients.size();
                    }else{
                        numberOfOrs=0;
                    }

                    recipesOr = orInRecipes(recherche,orIngredients );
                    sortSearchResult(recherche, recipesOr);

                }catch (IOException e){
                    Toast.makeText( search_activity.this, "String was not valid for search. Refer to help page for details." , Toast.LENGTH_LONG).show();
                    return;

                }
               if (flag){
                   populateListView();
               }
                registerClickCallBack();





            }});}



    private void registerClickCallBack(){

        ListView list = (ListView) findViewById(skyline.skyline1.R.id.select_ListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View viewClick, int position, long id){
                Recipe clickedRecipe = recherche.get(position);
                Intent i = new Intent(search_activity.this, RecipeView.class);
                i.putExtra("prepTime", clickedRecipe.getPreTime()+" minutes");
                i.putExtra("name", clickedRecipe.getName()+"");
                i.putExtra("cookTime", clickedRecipe.getCookTime()+" minutes");
                i.putExtra("category", clickedRecipe.getCategory().getName()+"");
                i.putExtra("origin", clickedRecipe.getCountry().getName()+"");
                i.putExtra("description", clickedRecipe.getDescription());
                i.putExtra("picture", clickedRecipe.getIconId()+"");




                startActivity(i);


            }
        });

    }



    public Boolean readIngredients(String received) throws IOException{
        String[] splitString = received.trim().split(" ");
        ArrayList<String> ingredients;

        if(splitString.length%2!=0){
            throw new IOException("String not convertible to search");
        }

        for(int i = 0 ; i<splitString.length;i++){
            splitString[i]=splitString[i].toLowerCase();
        }
        searchBools= new ArrayList<String>();
        ingredients= new ArrayList<String>();
        for(int j=0;j<splitString.length/2;j++){
            if(splitString[2*j].toUpperCase().equals("AND") ||
                    splitString[2*j].toUpperCase().equals("NOT") ||
                    splitString[2*j].toUpperCase().equals("OR")){
                searchBools.add(j,splitString[2*j].toUpperCase());
            }else{
                searchBools=null;
                searchIngredients=null;
                return false;
            }
            ingredients.add(j,splitString[2*j+1]);
        }
        searchIngredients=new ArrayList<Ingredient>();
        for(int h=0;h<ingredients.size();h++){
            if(DishMaker.getCookHelper().findIngredient(ingredients.get(h))!=null){//checks if ingredient exists
                searchIngredients.add(DishMaker.getCookHelper().findIngredient(ingredients.get(h)));
            }else{
                searchBools=null;
                searchIngredients=null;
                return false;
            }
        }
        return true;
    }




    private void populateListView() {


        adapter = new search_activity.MyListAdapter();
        list = (ListView) findViewById(skyline.skyline1.R.id.select_ListView);

        list.setAdapter(adapter);

    }

    private class  MyListAdapter extends ArrayAdapter<Recipe> {
                public MyListAdapter() {
            super(search_activity.this, food_ingredient, recherche);
       }

        @Override
        public @NonNull
        View getView(int position, View convertView, @NonNull ViewGroup parent) {View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(food_ingredient, parent, false);
            }

            //find the ingredient

            Recipe currentRecipe = recherche.get(position);
            Integer currentString = recipesOr.get(position);



            TextView nameText = (TextView) itemView.findViewById(skyline.skyline1.R.id.item_textName);
            nameText.setText(currentRecipe.getName());


            TextView name = (TextView) itemView.findViewById(skyline.skyline1.R.id.txt_OrNumber);
            name.setText(currentString+"/"+ numberOfOrs);
            return itemView;


        }
    }


    public ArrayList<Ingredient> getOrIngredients(ArrayList<String> bools, ArrayList<Ingredient> ings){
        ArrayList<Ingredient> orIngredients= new ArrayList<>();
        if(bools==null || ings ==null){
            return null;
        }
        if(bools.size()>0) {
            for (int i = 0; i < bools.size(); i++) {
                if (bools.get(i).toUpperCase().equals("OR")) {
                    orIngredients.add(ings.get(i));
                }
            }
            return orIngredients;
        }else{
            return null;
        }
    }

    public ArrayList<Integer> orInRecipes(ArrayList<Recipe> recipes, ArrayList<Ingredient> ingredients){
        ArrayList<Integer> matchingOrs = new ArrayList<>(recipes.size());
        int counter;
        if(ingredients!=null){
            for(int i=0;i<recipes.size();i++){
                counter=0;
                for(int j=0;j<ingredients.size();j++){
                    if(recipes.get(i).hasIngredient(ingredients.get(j))){
                        counter++;
                    }
                }
                matchingOrs.add(counter);
            }
        }else{
            for(int i=0;i<recipes.size();i++){
                matchingOrs.add(0);
            }
        }

        return matchingOrs;
    }



    public void sortSearchResult(ArrayList<Recipe> recipes, ArrayList<Integer> orsOfRecipes){
        ArrayList<Recipe> newRecipes= new ArrayList<>(recipes.size());
        ArrayList<Integer> newOrsOfRecipes = new ArrayList<>(orsOfRecipes.size());
        int counter;

        if(orsOfRecipes.size()<=0){
            return;
        }else{
            counter = numberOfOrs;
        }

        while(counter>=0){
            for(int i=0;i<orsOfRecipes.size();i++){
                if(orsOfRecipes.get(i)==counter){
                    newRecipes.add(recipes.get(i));
                    newOrsOfRecipes.add(orsOfRecipes.get(i));
                }
            }
            counter--;
        }
        recherche=newRecipes;
        recipesOr=newOrsOfRecipes;
    }


}





