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

public class Country_List extends AppCompatActivity {
    private List<Country> myCountry = new ArrayList<Country>();
    private ArrayAdapter<Country> adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(skyline.skyline1.R.layout.origin__list_list);
        Toolbar toolbar = (Toolbar) findViewById(skyline.skyline1.R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(skyline.skyline1.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(Country_List.this, act_add_Country.class);
                startActivity(a);


            }
        });
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        }}


    protected void onResume() {
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


                                    Country country = DishMaker.getCookHelper().getCountries().get(position);
                                    boolean isNotInRecipe = DishMaker.getCookHelper().removeOrigin(country);
                                    if(!isNotInRecipe){
                                        String message =" You can not remove this country because it is currently used in a recipe";

                                        Toast.makeText(Country_List.this, message , Toast.LENGTH_LONG).show();
                                    }

                                    adapter.notifyDataSetChanged();

                                }

                            }
                        });
        list.setOnTouchListener(touchListener);
    }



    private void populateListView() {

        adapter = new Country_List.MyListAdapter();
        list = (ListView) findViewById(skyline.skyline1.R.id.originsListView);
        list.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<Country> {

        public MyListAdapter() {
            super(Country_List.this, skyline.skyline1.R.layout.food_ingredient, DishMaker.getCookHelper().getCountries());
        }

        @Override
        public @NonNull
        View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(skyline.skyline1.R.layout.food_ingredient, parent, false);
            }



            Country currentCountry = DishMaker.getCookHelper().getCountries().get(position);



            TextView nameText = (TextView) itemView.findViewById(skyline.skyline1.R.id.item_textName);
            nameText.setText(currentCountry.getName());
            return itemView;


        }

    }


}

