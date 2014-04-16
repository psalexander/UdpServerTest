package com.asinenko.udpserver;

import android.content.Context;
import android.graphics.PointF;
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
	private SparseArray<PointF> mActivePointers = new SparseArray<PointF>();;

	public TouchImageView(Context context) {
		super(context);
		this.context = context;
		gestureDetector = new GestureDetector(context, new MyGestureListener());
	}
	
	public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		gestureDetector = new GestureDetector(context, new MyGestureListener());
	}

	public TouchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		gestureDetector = new GestureDetector(context, new MyGestureListener());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
//		if(true){
//			if (gestureDetector.onTouchEvent(event))
//				return true;
//		}

		// get pointer index from the event object
		int pointerIndex = event.getActionIndex();
		// get pointer ID
		int pointerId = event.getPointerId(pointerIndex);
		// get masked (not specific to a pointer) action
		int maskedAction = event.getActionMasked();

		switch(maskedAction){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				Log.w("!", "ACTION_POINTER_DOWN id=" + pointerId);
				PointF f = new PointF();
				f.x = event.getX(pointerIndex);
				f.y = event.getY(pointerIndex);
				mActivePointers.put(pointerId, f);
				break;
			case MotionEvent.ACTION_MOVE:
				//Toast.makeText(context, "ACTION_MOVE", Toast.LENGTH_SHORT).show();
				//Log.w("!", "ACTION_MOVE");
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				Log.w("!", "ACTION_POINTER_UP id=" + pointerId);
			case MotionEvent.ACTION_CANCEL:
				mActivePointers.remove(pointerId);
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
			return super.onDown(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			Toast.makeText(context, "onFling", Toast.LENGTH_SHORT).show();
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			Toast.makeText(context, "onLongPress", Toast.LENGTH_SHORT).show();
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
