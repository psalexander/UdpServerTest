package com.asinenko.udpserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	TouchImageView touchImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		touchImageView = (TouchImageView) findViewById(R.id.imageView1);
		touchImageView.doBindService();
		startService(new Intent(this, UdpServerService.class));
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		touchImageView.doUnbindService();
	}


}
