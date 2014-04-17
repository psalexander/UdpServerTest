package com.asinenko.udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class UdpServerService extends Service{

	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_SEND_MESSAGE = 3;

	private static final int UDP_SERVER_PORT = 2004;
	private static final int MAX_UDP_DATAGRAM_LEN = 1500;
//	private RunServerInThread runServer;
	TCPClient connection = null;
	private final Messenger mMessenger = new Messenger(new IncomingHandler());
	private ArrayList<Messenger> mClients = new ArrayList<Messenger>();

	private Messenger mService = null;

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};


	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_REGISTER_CLIENT:
					mClients.add(msg.replyTo);
					break;
				case MSG_UNREGISTER_CLIENT:
					mClients.remove(msg.replyTo);
					break;
				case MSG_SEND_MESSAGE:
					Toast.makeText(getApplicationContext(), "Обновляем настройки", Toast.LENGTH_SHORT).show();
					break;
				default:
					super.handleMessage(msg);
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		connection = new TCPClient();
		try {
			connection.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//runServer = new RunServerInThread();
		//runServer.start();
	}

	private void sendMessage(String message){
		connection.sendMessage(message);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		runServer.stop();
		if(connection != null)
			connection.closeConnection();

	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mMessenger.getBinder();
	}
	



//	private void runUdpServer() {
//		String message;
//		byte[] lmessage = new byte[MAX_UDP_DATAGRAM_LEN];
//		DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);
//		DatagramSocket socket = null;
//		try {
//			socket = new DatagramSocket(UDP_SERVER_PORT);
//			Log.w("+++", "Receive...");
//			//socket.
//			socket.receive(packet);
//			Log.w("+++", "Conected.");
//			message = new String(lmessage, 0, packet.getLength());
//		} catch (SocketException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (socket != null) {
//				socket.close();
//			}
//		}
//	}

//	private class RunServerInThread extends Thread{
//		private boolean keepRunning = true;
//		private String lastmessage = "";
//		private boolean isConnected = false;
//
//		@Override
//		public void run() {
//			String message;
//			byte[] lmessage = new byte[MAX_UDP_DATAGRAM_LEN];
//			DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);
//			DatagramSocket socket = null;
//			while(keepRunning){
//				try {
//					socket = new DatagramSocket(UDP_SERVER_PORT);
//					socket.receive(packet);
//					//message = new String(lmessage, 0, packet.getLength());
//					isConnected = true;
////					textMessage.setText(message);
//				} catch (SocketException e) {
//					isConnected = false;
//					e.printStackTrace();
//				} catch (IOException e) {
//					isConnected = false;
//					e.printStackTrace();
//				} finally {
//					if (socket != null) {
//						socket.close();
//					}
//					isConnected = false;
//				}
//			}
//		}
//
//		public void sendMessage(String message){
//			Log.w("!!!!", "Message is " + message);
//		}
//
//		public boolean isConnected(){
//			return isConnected;
//		}
//	}
	
//	private class SendMessageToServer extends AsyncTask<Void, Void, Void> {
////		int count = 0;
////		int notsendcount = 0;
//
//		TCPClient connection = null;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//		}
//
//		protected Void doInBackground(Void... ids) {
//			try {
//				connection = new TCPClient();
//				connection.run();
//				if(connection.sendMessage("")){
//
//				}else{
//
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}finally{
//				if(connection != null)
//					connection.closeConnection();
//			}
//			return null;
//		}
//	}
	
//	public class DrawThread extends Thread {
//	
//		public DrawThread(){
//			
//		}
//
//		public void setRunning(boolean run) {
//
//		}
//
//
//		@Override
//		public void run() {
//
//			while (runFlag) {
//				if(isDraw){
//					long now = System.currentTimeMillis();
//					long elapsedTime = now - prevTime;
//					if (elapsedTime > 80){
//						prevTime = now;
//					}
//					canvas = null;
//					try {
//						canvas = surfaceHolder.lockCanvas(null);
//						synchronized (surfaceHolder) {
//							myThreadSurfaceView.onDraw(canvas);
//						}
//					} finally {
//						if (canvas != null) {
//							surfaceHolder.unlockCanvasAndPost(canvas);
//						}
//					}
//				}
//			}
//		}
//	}
}
