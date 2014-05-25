package com.asinenko.udpserver;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ImageView;
import android.widget.Toast;

public class TouchImageView extends ImageView{

	private final GestureDetector gestureDetector;
	private Context context;
	private SparseArray<PointF> mActivePointers = new SparseArray<PointF>();
	private Map<Integer, Long> mActivePointersTime = new LinkedHashMap<Integer, Long>();
	private boolean mIsBound;
	private static float coef = 2.0f;
	private int currentPointerId = -1;
	private static int scrollStep = 120;
	int x = 0;
	int y = 0;

	private int firstPointerId = -1;
	private int lastPointerId = -1;
	private float scrollDistanceX = 0;
	private float scrollDistanceY = 0;

	public TouchImageView(Context context) {
		super(context);
		this.context = context;
		gestureDetector = new GestureDetector(context, new MyGestureListener());
		doBindService();
	}

	public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		gestureDetector = new GestureDetector(context, new MyGestureListener());
		doBindService();
	}

	public TouchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		gestureDetector = new GestureDetector(context, new MyGestureListener());
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

	private Messenger mService = null;

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

		x = (int) event.getX();
		y = (int) event.getY();

		switch(maskedAction){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				if(-1 == currentPointerId){
					currentPointerId = pointerId;
				}
				PointF f = new PointF();
				f.x = event.getX(pointerIndex);
				f.y = event.getY(pointerIndex);
				mActivePointers.put(pointerId, f);
				mActivePointersTime.put(pointerId, System.currentTimeMillis());
				if(mActivePointers.size() == 1){
					coef = 2.0f;
				}else if(mActivePointers.size() > 1){
					coef = 1.0f;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float deltaX = (event.getX(pointerIndex) - mActivePointers.get(pointerId).x) * coef;
				float deltaY = (event.getY(pointerIndex) - mActivePointers.get(pointerId).y) * coef;

				if(pointerId == currentPointerId && mActivePointers.size() < 3){
					sendMessageToService("M," + 
										String.valueOf(deltaX) + 
										"," + 
										String.valueOf(deltaY) + 
										";\n");
					mActivePointers.get(pointerId).x = event.getX(pointerIndex);
					mActivePointers.get(pointerId).y = event.getY(pointerIndex);
				}else
				if(mActivePointers.size() >= 3 ){
					scrollDistanceX += deltaX / coef;
					scrollDistanceY += deltaY / coef;
					if(Math.abs(scrollDistanceX) >= scrollStep){
						if(scrollDistanceX > 0){
							sendMessageToService("SU\n");
						}else{
							sendMessageToService("SD\n");
						}
						scrollDistanceX=0;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
				if(pointerId == currentPointerId){
					if(mActivePointers.size() == 0){
						currentPointerId = -1;
					}else if(mActivePointers.size() == 1){
						//for(int i=0; i < 10; i++){
							//if(null != mActivePointers.keyAt(i)){
								currentPointerId = mActivePointers.keyAt(0);
							//}
						//}
					}
				}
				mActivePointers.remove(pointerId);
				if(System.currentTimeMillis() - mActivePointersTime.get(pointerId) < 100){
					sendMessageToService("CLICK\n");
				}
				mActivePointersTime.remove(pointerId);
				if(mActivePointers.size() == 1){
					coef = 2.0f;
				}else if(mActivePointers.size() > 1){
					coef = 1.0f;
				}
				break;
		}
		return true;
	}

	private class MyGestureListener extends SimpleOnGestureListener{

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
			Toast.makeText(context, "onScroll", Toast.LENGTH_SHORT).show();
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			Toast.makeText(context, "onDoubleTap", Toast.LENGTH_SHORT).show();
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			Toast.makeText(context, "onDoubleTapEvent", Toast.LENGTH_SHORT).show();
			return super.onDoubleTapEvent(e);
		}

		@Override
		public boolean onDown(MotionEvent e) {
			//Toast.makeText(context, "onDown", Toast.LENGTH_SHORT).show();
			sendMessageToService("onDown");
			return super.onDown(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			Toast.makeText(context, "onFling", Toast.LENGTH_SHORT).show();
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			sendMessageToService("onLongPress");
			//Toast.makeText(context, "onLongPress", Toast.LENGTH_SHORT).show();
			super.onLongPress(e);
		}

		@Override
		public void onShowPress(MotionEvent e) {
			Toast.makeText(context, "onShowPress", Toast.LENGTH_SHORT).show();
			super.onShowPress(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Toast.makeText(context, "onSingleTapConfirmed", Toast.LENGTH_SHORT).show();
			return super.onSingleTapConfirmed(e);
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			Toast.makeText(context, "onSingleTapUp", Toast.LENGTH_SHORT).show();
			return super.onSingleTapUp(e);
		}
	}
}
