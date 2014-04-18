package com.asinenko.udpserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class UdpServerService extends Service{

	private String SERVER_IP = "192.168.77.149";
	private int SERVERPORT = 28333;

	boolean isConnected = false;

	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_SEND_MESSAGE = 3;

	private static final int UDP_SERVER_PORT = 2004;
	private static final int MAX_UDP_DATAGRAM_LEN = 1500;

	private final Messenger mMessenger = new Messenger(new IncomingHandler());
	private ArrayList<Messenger> mClients = new ArrayList<Messenger>();

	private Socket socket = null;
	private PrintWriter out = null;

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
					//Toast.makeText(getApplicationContext(), "Обновляем настройки", Toast.LENGTH_SHORT).show();
					Log.w("222", "333");
					UdpServerService.this.sendMessage(msg.getData().getString("message"));

					break;
				default:
					super.handleMessage(msg);
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public boolean sendMessage(String message){
		//if (out != null && !out.checkError()) {
			out.println(message);
			//out.flush();
			return true;
		//}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		new Thread(new ClientThread()).start();

//		try {
		//	Thread.sleep(10000);
		//	sendMessage("0001010101010");
			//out.println("poiopiopioppoiopi");
	//	} catch (InterruptedException e) {
			// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
		return START_NOT_STICKY;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mMessenger.getBinder();
	}

	class ClientThread implements Runnable {
		@Override
		public void run() {
			try {
				InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
				socket = new Socket(serverAddr, SERVERPORT);
				out = new PrintWriter( new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
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

	private class SendMessageToServer extends AsyncTask<Void, Void, Void> {
//		int count = 0;
//		int notsendcount = 0;

		TCPClient connection = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}

		protected Void doInBackground(Void... ids) {
			try {
				connection = new TCPClient();
				connection.run();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//if(connection.sendMessage("")){
				//}else{
				//}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(connection != null)
					connection.closeConnection();
			}
			return null;
		}
	}

//	public class DrawThread extends Thread {
//		public DrawThread(){
//		}
//		public void setRunning(boolean run) {
//		}
//		@Override
//		public void run() {
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
