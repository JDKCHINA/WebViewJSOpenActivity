package com.example.js;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.js.util.OpenActivityUtil;

import java.io.File;

public class MyActivity extends Activity {
    String TAG = "MyActivity";
    WebView mWebView = null;
    MyActivity context = this;

    ProgressDialog progressDialog = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");


        mWebView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSavePassword(false);//不保存网页密码
        webSettings.setSaveFormData(false);//不保存表单form
        webSettings.setJavaScriptEnabled(true);//允许JavaScript
        webSettings.setSupportZoom(false);//禁止放大
        webSettings.setPluginState(WebSettings.PluginState.ON);//允许插件
//        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");//设置编码

        //下面是设置缓存  可不写
        /*mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);	//设置 缓存模式
        // 开启 DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        mWebView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath();
        Log.v(TAG, "cacheDirPath=" + cacheDirPath);
        //设置数据库缓存路径
        mWebView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);*/


        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());

        //mPage.close()
        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "mPage");
        //上面已经使用了mPage
//        mWebView.addJavascriptInterface(new OpenActivityJavaScriptInterface(), "mPage");//can not use mPage in this line,addJavascriptInterface 第二个参数 不能使用已经使用过的name

        //OpenActivity
        mWebView.addJavascriptInterface(new OpenActivityJavaScriptInterface(), "openObject");

        mWebView.loadUrl("file:///android_asset/test.html");
    }

    class DemoJavaScriptInterface {

        DemoJavaScriptInterface() {

        }

        /**
         * 方法名必须和js的方法名一直
         */
        public void clickOnAndroid() {
            Toast.makeText(context, "clickOnAndroid", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setAction("demoAct");
            intent.putExtra("title", "隐士启动 ok！");
            startActivity(intent);
        }

        public void close() {
            Toast.makeText(context, "close", Toast.LENGTH_SHORT).show();
            context.finish();
        }
    }

    /**
     * openActivity
     */
    class OpenActivityJavaScriptInterface {

        public OpenActivityJavaScriptInterface() {
        }

        /**
         * 方法名必须和js的方法名一直
         */
        public void open(String s) {
            Toast.makeText(context, "open", Toast.LENGTH_SHORT).show();
            OpenActivityUtil.open(context, s);
        }
    }


    /**
     * MyWebChromeClient
     */
    class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            //debug js
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //show progressDialog
            if (progressDialog != null && !progressDialog.isShowing() && newProgress < 20) {
                //newProgress < 20 有些手机的newProgress 直接从10开始
                progressDialog.show();
            }

            if (progressDialog != null && progressDialog.isShowing() && newProgress >= 95) {
                progressDialog.hide();
            }

            super.onProgressChanged(view, newProgress);
        }
    }

    /**
     * MyWebViewClient
     */
    class MyWebViewClient extends WebViewClient {
        /**
         * return true 可截断网页链接
         *  href 等
         * @param view
         * @param url
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //也可截断 message://action?key=value&title=hello   从而隐士调用activity
            if (url != null && url.trim().startsWith("tel:")) {
                String phoneStr = url.replace("tel:", "");
                try {
                    //直接打电话
//                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneStr)));

                    //拨号盘界面
                    String tel = phoneStr.replace("-", "");
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        clearWebViewCache();
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        //清理Webview缓存数据库  开启database后 需要清理掉数据可以调用
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");

            //WebView 缓存文件
            File appCacheDir = new File(getFilesDir().getAbsolutePath());
            Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

            File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
            Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

            //删除webview 缓存目录
            if (webviewCacheDir.exists()) {
                deleteFile(webviewCacheDir.getAbsolutePath());
            }
            //删除webview 缓存 缓存目录
            if (appCacheDir.exists()) {
                deleteFile(appCacheDir.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
