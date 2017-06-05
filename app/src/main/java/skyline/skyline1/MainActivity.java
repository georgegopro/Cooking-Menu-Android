package skyline.skyline1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ArrayAdapter<Recipe> adapter;
    private ListView list;
    private static Context mContext;






    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(skyline.skyline1.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(skyline.skyline1.R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(skyline.skyline1.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainActivity.this, recipe_add_form.class);
                startActivity(a);}
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(skyline.skyline1.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, skyline.skyline1.R.string.navigation_drawer_open, skyline.skyline1.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(skyline.skyline1.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initSingletons();





        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();




    }

    protected void onResume(){

        super.onResume();
        populateListView();
        registerClickCallBack();


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


                                    Recipe recipe = DishMaker.getCookHelper().getRecipes().get(position);
                                    DishMaker.getCookHelper().removeRecipe(recipe);

                                    adapter.notifyDataSetChanged();

                                }

                            }
                        });
        list.setOnTouchListener(touchListener);



        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();




    }

    private void registerClickCallBack(){

        ListView list = (ListView) findViewById(skyline.skyline1.R.id.recipesListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View viewClick, int position, long id){
                Recipe clickedRecipe = DishMaker.getCookHelper().getRecipes().get(position);
                Intent i = new Intent(MainActivity.this, RecipeView.class);
                i.putExtra("prepTime", clickedRecipe.getPreTime()+"");
                i.putExtra("name", clickedRecipe.getName()+"");
                i.putExtra("cookTime", clickedRecipe.getCookTime()+"");
                i.putExtra("category", clickedRecipe.getCategory().getName()+"");
                i.putExtra("origin", clickedRecipe.getCountry().getName()+"");
                i.putExtra("description", clickedRecipe.getDescription());
                if(clickedRecipe.getIconId() == null) {
                    i.putExtra("picture", "android.resource://skyline.skyline1/drawable/defimage");
                } else {
                    i.putExtra("picture", clickedRecipe.getIconId()+"");
                }
                i.putExtra("Position", position+"");

                ArrayList<Ingredient> ingredientList = clickedRecipe.getIngredients();
                String ingredients = null ;
                for (int y =0; y<ingredientList.size(); y++) {
                    if (y == 0) {
                        ingredients = ingredientList.get(y).getName();
                    } else {
                        ingredients = ingredients + "\n" + ingredientList.get(y).getName();
                    }
                }
                i.putExtra("ingredients", ingredients);


                startActivity(i);


            }
        });

    }


    protected void initSingletons()
    {


        DishMaker.getCookHelper();
    }



    private void populateListView() {

         adapter = new MyListAdapter();
        list = (ListView) findViewById(skyline.skyline1.R.id.recipesListView);
        list.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(skyline.skyline1.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();


        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class MyListAdapter extends ArrayAdapter<Recipe> {

        public MyListAdapter() {
            super(MainActivity.this, skyline.skyline1.R.layout.food_view, DishMaker.getCookHelper().getRecipes());
        }

        @Override
        public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(skyline.skyline1.R.layout.food_view, parent, false);
            }



            Recipe currentRecipe = DishMaker.getCookHelper().getRecipes().get(position);



            ImageView imageView = (ImageView) itemView.findViewById(skyline.skyline1.R.id.item_icon);
            if(currentRecipe.getIconId() == null) {
                imageView.setImageURI(Uri.parse("android.resource://tophaters.cookhelper/drawable/defimage"));
            } else {
                imageView.setImageURI(currentRecipe.getIconId());
            }


            TextView nameText = (TextView) itemView.findViewById(skyline.skyline1.R.id.item_txtName);
            nameText.setText(currentRecipe.getName());
            return itemView;


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(skyline.skyline1.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == skyline.skyline1.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == skyline.skyline1.R.id.nav_help) {
            Intent a = new Intent(MainActivity.this, help_page_act.class);
            startActivity(a);
        } else if (id == skyline.skyline1.R.id.nav_search) {

            Intent a = new Intent(MainActivity.this, search_activity.class);
            startActivity(a);

        } else if (id == skyline.skyline1.R.id.nav_viewCategories) {
            Intent a = new Intent(MainActivity.this, act_category_List.class);
            startActivity(a);
        } else if (id == skyline.skyline1.R.id.nav_viewIngredients) {
            Intent a = new Intent(MainActivity.this, Ingredient_List.class);
            startActivity(a);
        } else if (id == skyline.skyline1.R.id.nav_viewOrigins) {
            Intent a = new Intent(MainActivity.this, Country_List.class);
            startActivity(a);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(skyline.skyline1.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
