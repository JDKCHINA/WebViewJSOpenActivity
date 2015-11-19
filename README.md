# WebViewJSOpenActivity
使用webview与js的交互，demo中包含js打电话，打开不同activity

使用webView 注意事项 (demo中没有完全写出)

##1.高度宽度 不要使用wrap_content 会影响JS <br/>
 <WebView
        android:id="@+id/webview"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
        
##2. 适配html配置<br/>
   //自适配高度和宽度
		WebSettings settings = mWebView.getSettings();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
##3. 加载flash视频，开启硬件加速 (最好 在代码中和mainfest中配置都配置)<br/>
    有些手机不开启加速 看不见画面<br/>
		try {
			//硬件加速  同时在mainfest中配置 当前activity  hardwareAccelerated true
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mWebView.getSettings().setJavaScriptEnabled(true);
	  mWebView.getSettings().setDomStorageEnabled(true);
##4. 退出停止视频声音<br/>
 @Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mWebView.onResume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mWebView.onPause(); // 暂停网页中正在播放的视频
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		try {
			mWebView.stopLoading();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

##5.退出停止声音 上面方法4不行的话，退出的时候 onStop中 加载一个空白的页面(如 www.baidu.com)<br/>

demo like this
![demo](https://github.com/lovemelovemydog/WebViewJSOpenActivity/blob/master/TestWebViewJS/Screenshot_2015-07-10-11-32-53.png "demo image")
