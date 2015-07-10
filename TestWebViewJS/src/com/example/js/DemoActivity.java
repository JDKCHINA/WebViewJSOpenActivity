package com.example.js;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by fangzhu on 2015/7/10.
 */
public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo);

        TextView textView = (TextView)findViewById(R.id.textView);

        String title = getIntent().getStringExtra("title");
        if (title != null) {
            if (textView != null)
                textView.setText(title);
        }
    }
}
