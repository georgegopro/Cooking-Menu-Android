package skyline.skyline1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static skyline.skyline1.R.layout.food_ingredient;

public class Ingredient_List extends AppCompatActivity {
    private ListView list;
    private ArrayAdapter<Ingredient> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(skyline.skyline1.R.layout.ingredient__list_act);
        Toolbar toolbar = (Toolbar) findViewById(skyline.skyline1.R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(skyline.skyline1.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(Ingredient_List.this, act_add_Ingredient.class);
                startActivity(a);


            }
        });
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }}

    public void onResume() {
        super.onResume();

        populateListView();



        Swiped touchListener =
                new Swiped(
                        list,
                        new Swiped.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {


                                    Ingredient ingredient = DishMaker.getCookHelper().getIngredients().get(position);
                                    boolean isNotInRecipe = DishMaker.getCookHelper().removeIngredient(ingredient);
                                    if(!isNotInRecipe){
                                        String message =" You can not remove this ingredient because it is currently used in a recipe";

                                        Toast.makeText( Ingredient_List.this, message , Toast.LENGTH_LONG).show();
                                    }

                                    adapter.notifyDataSetChanged();

                                }

                            }
                        });
        list.setOnTouchListener(touchListener);
    }


    private void populateListView() {

        adapter= new MyListAdapter();
        list = (ListView) findViewById(skyline.skyline1.R.id.ingredientsListView);
        list.setAdapter(adapter);

    }


    private class  MyListAdapter extends ArrayAdapter<Ingredient> {

        public MyListAdapter() {
            super(Ingredient_List.this, food_ingredient, DishMaker.getCookHelper().getIngredients());
        }

        @Override
        public @NonNull
        View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(food_ingredient, parent, false);
            }



            Ingredient currentIngredient = DishMaker.getCookHelper().getIngredients().get(position);



            TextView nameText = (TextView) itemView.findViewById(skyline.skyline1.R.id.item_textName);
            nameText.setText(currentIngredient.getName());
            return itemView;


        }

    }




}


