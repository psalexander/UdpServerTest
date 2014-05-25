package com.asinenko.udpserver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class LeftClickImageView extends ImageView{

	private boolean isDown = false;
	private static int downColor = Color.GREEN;
	private static int upColor = Color.GRAY;
	int currentPointerId = -1;
	private Context context;

	private boolean mIsBound;
	private Messenger mService = null;

	public LeftClickImageView(Context context) {
		super(context);
		this.context = context;
		setBackgroundColor(upColor);
		doBindService();
	}

	public LeftClickImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		setBackgroundColor(upColor);
		doBindService();
	}

	public LeftClickImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setBackgroundColor(upColor);
		doBindService();
	}

	public void doBindService() {
		if(!mIsBound){
			this.context.bindService(new Intent(this.context, UdpServerService.class), mConnection, Context.BIND_AUTO_CREATE);
			mIsBound = true;
		}
	}

	public void doUnbindService() {
		if (mIsBound) {
			this.context.unbindService(mConnection);
			mIsBound = false;
		}
	}

	public void sendMessageToService(String message) {
		if (!mIsBound)
			return;
		if (mService != null) {
			try {
				Bundle b = new Bundle();
				b.putString("message", message);
				Message msg = Message.obtain(null, UdpServerService.MSG_SEND_MESSAGE);
				msg.setData(b);
				mService.send(msg);
			} catch (RemoteException e) {

			}
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event){
		int pointerIndex = event.getActionIndex();
		int pointerId = event.getPointerId(pointerIndex);
		int maskedAction = event.getActionMasked();

		switch(maskedAction){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				if(!isDown && currentPointerId == -1){
					sendMessageToService("LD\n");
					currentPointerId = pointerId;
					isDown = true;
					setBackgroundColor(downColor);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				
			case MotionEvent.ACTION_CANCEL:
				if(isDown && pointerId == currentPointerId){
					sendMessageToService("LU\n");
					currentPointerId = -1;
					isDown = false;
					setBackgroundColor(upColor);
				} 
				break;
		}
		return true;
	}
}
