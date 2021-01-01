package mm.pndaza.vatthuguide;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;


public class Detail extends AppCompatActivity {

    private Context context = null;
    DBOpenHelper db = null;
    SQLiteDatabase sqLiteDatabase = null;
    Integer id;
    Boolean isAdded = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail);
        context = this;
        MDetect.init(context);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // change title to unicode
        String title = "ဝထၳဳလမ္းၫႊန္";
        if(MDetect.isUnicode())
            title = Rabbit.zg2uni(this.getTitle().toString());
        setTitle(title);



        Intent intent = getIntent();


        id = intent.getIntExtra("id", 0);
        String item = intent.getStringExtra("item");
        String content = intent.getStringExtra("content");
        String bref = intent.getStringExtra("bref");

        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str1= new SpannableString(item+"\n\n");
        str1.setSpan(new ForegroundColorSpan(Color.BLUE), 0, str1.length(), 0);
        str1.setSpan(new RelativeSizeSpan(1.4f), 0,str1.length(), 0);
        str1.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), 0 );
        builder.append(str1);

        content = content.replace("\\n", "\n\n");

        SpannableString str2= new SpannableString(content + "\n\n");
        str2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str2.length(), 0);
        str2.setSpan(new RelativeSizeSpan(1.3f), 0,str2.length(), 0);
        builder.append(str2);

        SpannableString str3= new SpannableString(bref + "\n\n\n\n");
        str3.setSpan(new ForegroundColorSpan(Color.GRAY), 0, str3.length(), 0);
        str3.setSpan(new RelativeSizeSpan(1.1f), 0,str3.length(), 0);
        str3.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),0,str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(str3);


        TextView textView = findViewById(R.id.tv_detail);
        textView.setTextIsSelectable(true);
        textView.setText( builder, TextView.BufferType.SPANNABLE);


        FloatingActionButton fab = findViewById(R.id.fab);


        db = DBOpenHelper.getInstance(context);
        sqLiteDatabase = db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT sid FROM fav WHERE sid = %d",id),null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            isAdded = false;
            fab.setImageResource(R.drawable.favourite);

        } else  {
            isAdded = true ;
            fab.setImageResource(R.drawable.favourited);

        }
        cursor.close();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isAdded == false){
                    sqLiteDatabase.execSQL("INSERT INTO fav (sid) VALUES (?)",new Object[] {id});

                    ((FloatingActionButton)view).setImageResource(R.drawable.favourited);

                    String info = "မွတ္သားလိုက္ပါၿပီ";
                    if (MDetect.isUnicode())
                        info = Rabbit.zg2uni(info);

                    Snackbar.make(view, info, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    isAdded = true;

                }
                else {
                    sqLiteDatabase.execSQL("DELETE FROM fav WHERE sid = ?", new Object[] {id});

                    ((FloatingActionButton)view).setImageResource(R.drawable.favourite);

                    String info = "မွတ္သားထားသည္မွ ဖ်က္လိုက္ပါၿပီ";
                    if (MDetect.isUnicode())
                        info = Rabbit.zg2uni(info);

                    Snackbar.make(view, info, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    isAdded = false;
                }

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}