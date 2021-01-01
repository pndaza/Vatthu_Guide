package mm.pndaza.vatthuguide;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class Favourite extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<FavData> item_list = new ArrayList<>();
    private FavDataAdapter mAdapter;

    private DBOpenHelper dbOpenHelper = null;
    private SQLiteDatabase sqLiteDatabase = null;
    private Cursor cursor = null;
    private Context context = null;

    Boolean menu_show = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite2);

        context = this;
        MDetect.init(context);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // change title to unicode
        String title = "ဝထၳဳလမ္းၫႊန္";
        if(MDetect.isUnicode())
            title = Rabbit.zg2uni(this.getTitle().toString());
        setTitle(title);


        initControls();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initControls();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initControls() {

        recyclerView = findViewById(R.id.recycler_view);

        item_list.clear();

        dbOpenHelper = DBOpenHelper.getInstance(context);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("select vatthuguide._id, item from vatthuguide inner join fav on fav.sid = vatthuguide._id", null);

        if (cursor.getCount() != 0 ){
            FavData.isCheckboxShow = false;
            while (cursor.moveToNext()) {
                item_list.add(new FavData(cursor.getInt(0),cursor.getString(1)));
            }
        }

        cursor.close();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FavDataAdapter(getApplicationContext(),item_list);
        recyclerView.setAdapter(mAdapter);


    }

    public void removeFromDB(final int _id){

        dbOpenHelper = DBOpenHelper.getInstance(context);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM fav WHERE sid = ?", new Object[] {_id});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_fav, menu);

        if (menu != null) {
            MenuItem sel = menu.findItem(R.id.select_all);
            MenuItem del = menu.findItem(R.id.del);
            MenuItem edit = menu.findItem(R.id.edit);
            if(menu_show == true) {
                edit.setIcon(R.drawable.cancel);
                sel.setVisible(true);
                del.setVisible(true);
            } else {
                edit.setIcon(R.drawable.edit);
                sel.setVisible(false);
                del.setVisible(false);
            } }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:

                if (menu_show == false) {
                    menu_show = true;
                    FavData.isCheckboxShow = true;
                } else {
                    item.setIcon(R.drawable.edit);
                    menu_show = false;
                    FavData.isCheckboxShow = false;
                }


                mAdapter.notifyDataSetChanged();;
                invalidateOptionsMenu();

                return true;

            case  R.id.select_all:

                for (FavData favData: item_list) {
                    favData.setSelected(true);
                }
                mAdapter.notifyDataSetChanged();

                return true;

            case R.id.del:

                ArrayList<FavData> temp = new ArrayList<>(item_list);

                for ( FavData data : temp ) {
                    if ( data.isSelected() == true) {
                        item_list.remove(data);

                        removeFromDB(data.getId());
                    }
                }

                mAdapter.notifyDataSetChanged();

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

}

