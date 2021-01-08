package com.maoqis.test.androidnew.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maoqis.test.androidnew.R;
import com.maoqis.test.androidnew.ui.fragment.WebFragment;

import static com.maoqis.test.androidnew.ui.item.WeekItemAdapterItem.KEY_URL;

public class WebActivity extends AppCompatActivity {
    private static final String TAG = "WebActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Fragment fragmentById = getFragmentManager().findFragmentById(R.id.fragment);
        if (fragmentById != null && fragmentById instanceof WebFragment) {
            WebFragment webFragment = (WebFragment) fragmentById;
            String url = getIntent().getStringExtra(KEY_URL);
            Log.d(TAG, "onCreate: url=" + url);
            webFragment.getWebView().loadUrl(url);
            webFragment.getWebView().setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String url = getIntent().getStringExtra(KEY_URL);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.share:
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");

                textIntent.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(Intent.createChooser(textIntent, "分享"));

                break;
            case R.id.web:

                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
