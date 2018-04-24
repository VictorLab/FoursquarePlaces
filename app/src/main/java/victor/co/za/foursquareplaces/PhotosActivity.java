package victor.co.za.foursquareplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;

import adapter.PhotoAdapter;

public class PhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photos);

        getSupportActionBar().setTitle(getString(R.string.photos));

        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUIComponent();
    }

    private void setUIComponent(){

        Intent i = getIntent();

        ArrayList<String> photos = i.getStringArrayListExtra("PhotoUrl");

        GridView gridView = findViewById(R.id.gridView);

        PhotoAdapter pa = new PhotoAdapter(getApplicationContext(),R.layout.photo_item,photos);

        gridView.setAdapter(pa);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
