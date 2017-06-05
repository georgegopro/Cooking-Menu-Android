package skyline.skyline1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class act_add_Country extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add__origin_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    public void onClickSaveOrigin(View v){
        boolean added;
        EditText edit =  (EditText) findViewById(R.id.inputNewOrigin);
        String origin = edit.getText().toString();

        int[] capsLocation = new int[origin.length()];
        capsLocation[0] = 1;
        char[] originChar = origin.toCharArray();
        for (int i = 0; i<originChar.length; i++){
            char c = originChar[i];
            if(c == ' '){
                capsLocation[i+1] = 1;
            }
        }

        origin = String.valueOf(originChar).toLowerCase();
        String originString = new String();
        for (int y = 0; y< capsLocation.length; y++){
            if (capsLocation[y]==1){
                originString = originString + origin.substring(y,y+1).toUpperCase();
            }else{
                originString = originString + origin.substring(y,y+1).toLowerCase();
            }
        }
        Country newCountry = new Country(originString);

        added = DishMaker.getCookHelper().addOrigin(newCountry);

        if (added){
            Toast.makeText(act_add_Country.this, "Saved", Toast.LENGTH_LONG).show();
            finish();

        }else{
            Toast.makeText(act_add_Country.this, "Country already exists.", Toast.LENGTH_LONG).show();
        }
        }
    }
