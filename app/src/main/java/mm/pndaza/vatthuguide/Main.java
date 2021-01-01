package mm.pndaza.vatthuguide;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context = null;
    private DBOpenHelper db = null;
    private SQLiteDatabase sqLiteDatabase = null;
    private MySimpleCursorAdapter adapter = null;


    ListView listview;
    private static final int LAYOUT = R.layout.list_item;
    private static  final String[] FROM = { "item" };
    private static  final String[] FROM_UNI = { "item_uni" };
    private static final int[] TO = { R.id.tv_item };
    private static Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        db = DBOpenHelper.getInstance(context);
        sqLiteDatabase = db.getReadableDatabase();
        MDetect.init(context);

        // Set Title
        String title = "ဝထၳဳလမ္းၫႊန္";
        if(MDetect.isUnicode())
            title = Rabbit.zg2uni(this.getTitle().toString());
        setTitle(title);


        listview = findViewById(R.id.listview);
        listview.setFastScrollEnabled(true);
        initList();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView appname = headerView.findViewById(R.id.tv_header);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem nav_fav =  menu.findItem(R.id.nav_fav);

        String fav = "မွတ္သားခ်က္မ်ား";
        if (MDetect.isUnicode())
            fav = Rabbit.zg2uni(fav);
        nav_fav.setTitle(fav);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);



        MenuItem search_item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        String hint = "ရွာရန္";
        if (MDetect.isUnicode())
           hint = Rabbit.zg2uni(hint);

        searchView.setQueryHint(hint);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                String temp = s;


                    if( MDetect.isUnicode()) {
                        cursor = sqLiteDatabase.rawQuery("select _id, item_uni from vatthuguide where item_uni like \"%" + temp + "%\"",null);
                        MySimpleCursorAdapter myadapter = new MySimpleCursorAdapter(getApplicationContext(), LAYOUT, cursor, FROM_UNI, TO, 0);
                        listview.setAdapter(myadapter);
                    } else {
                        cursor = sqLiteDatabase.rawQuery("select _id, item from vatthuguide where item like \"%" + temp + "%\"",null);
                        MySimpleCursorAdapter myadapter = new MySimpleCursorAdapter(getApplicationContext(), LAYOUT, cursor, FROM, TO, 0);
                        listview.setAdapter(myadapter);
                    }



                return false;
            }
        });

        return true;
    }




    private  void initList() {



        if (MDetect.isUnicode()) {
            cursor = sqLiteDatabase.rawQuery("select _id, item_uni from vatthuguide", null);
            adapter = new MySimpleCursorAdapter(getApplicationContext(), LAYOUT, cursor, FROM_UNI, TO, 0);
        }
        else {
            cursor = sqLiteDatabase.rawQuery("select _id, item from vatthuguide", null);
            adapter = new MySimpleCursorAdapter(getApplicationContext(), LAYOUT, cursor, FROM, TO, 0);
        }
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listview, View view, int position, long arg3) {

                //db = DBOpenHelper.getInstance(context);
                //db.getReadableDatabase();
                Cursor c = sqLiteDatabase.rawQuery("select * from vatthuguide where _id = " + ((Cursor)listview.getItemAtPosition(position)).getString(0), null);
                c.moveToFirst();

                Integer id = c.getInt(0);
                String item = c.getString(1);
                String content = c.getString(3);
                String bref = c.getString(4);
                c.close();

                Intent result = new Intent(Main.this.getApplicationContext(),Detail.class);
                result.putExtra("id",id);
                if (MDetect.isUnicode()) {
                    result.putExtra("item", Rabbit.zg2uni(item));
                    result.putExtra("content", Rabbit.zg2uni(content));
                    result.putExtra("bref", Rabbit.zg2uni(bref));
                } else {
                    result.putExtra("item", item);
                    result.putExtra("content", content);
                    result.putExtra("bref", bref);
                }
                Main.this.startActivity(result);

            }
        });


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            Intent intent = new Intent( Main.this,Favourite.class);
            Main.this.startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(Main.this,About.class);
            Main.this.startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
