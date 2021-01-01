package mm.pndaza.vatthuguide;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class About extends AppCompatActivity {

    private  Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MDetect.init(this);

        // change title to unicode
        if(MDetect.isUnicode())
            setTitle(Rabbit.zg2uni(this.getTitle().toString()));




        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setAllowFileAccess( false );
        webView.getSettings().setAppCacheEnabled( false );
        webView.getSettings().setJavaScriptEnabled( false );
        webView.loadUrl("file:///android_asset/about/about.html");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private String readFromAsset(String filename) throws IOException {


        BufferedReader reader =reader = new BufferedReader(
                new InputStreamReader(getApplicationContext().getAssets().open(filename), "UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        String line = reader.readLine();
        while ( line != null) {
            stringBuilder.append(line);
            line = reader.readLine();
        }

        reader.close();
        return stringBuilder.toString();

    }

}
