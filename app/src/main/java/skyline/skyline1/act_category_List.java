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

import java.util.ArrayList;
import java.util.List;

public class act_category_List extends AppCompatActivity {

    private List<Category> myCategories= new ArrayList<Category>();
    private ListView list;
    private ArrayAdapter<Category> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(skyline.skyline1.R.layout.category__list_act);
        Toolbar toolbar = (Toolbar) findViewById(skyline.skyline1.R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(skyline.skyline1.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(act_category_List.this, act_add_Category.class);
                startActivity(a);


            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }


    }



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


                                    Category category = DishMaker.getCookHelper().getCategories().get(position);

                                    boolean isNotInRecipe = DishMaker.getCookHelper().removeCategory(category);
                                    if (!isNotInRecipe) {
                                        String message = " You can not remove this category because it is currently used in a recipe";
                                        Toast.makeText(act_category_List.this, message, Toast.LENGTH_LONG).show();

                                    }  adapter.notifyDataSetChanged();

                                }
                            }});
        list.setOnTouchListener(touchListener);
    }


    private void populateListView() {

        adapter = new MyListAdapter();
        list = (ListView) findViewById(skyline.skyline1.R.id.categoriesListView);
        list.setAdapter(adapter);

    }


        private class MyListAdapter extends ArrayAdapter<Category> {

            public MyListAdapter() {
                super(act_category_List.this, skyline.skyline1.R.layout.food_ingredient, DishMaker.getCookHelper().getCategories());
            }

            @Override
            public @NonNull
            View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View itemView = convertView;
                if (itemView == null) {
                    itemView = getLayoutInflater().inflate(skyline.skyline1.R.layout.food_ingredient, parent, false);
                }


                Category currentCategory = DishMaker.getCookHelper().getCategories().get(position);



                TextView nameText = (TextView) itemView.findViewById(skyline.skyline1.R.id.item_textName);
                nameText.setText(currentCategory.getName());
                return itemView;


            }

    }

}