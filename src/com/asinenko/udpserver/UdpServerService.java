package com.asinenko.udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UdpServerService extends Service{

	private static final int UDP_SERVER_PORT = 2004;
	private static final int MAX_UDP_DATAGRAM_LEN = 1500;
	private RunServerInThread runServer;

	@Override
	public void onCreate() {
		super.onCreate();

		runServer = new RunServerInThread();
		runServer.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		runServer.stop();
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
		return null;
	}

	private void runUdpServer() {
		String message;
		byte[] lmessage = new byte[MAX_UDP_DATAGRAM_LEN];
		DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(UDP_SERVER_PORT);
			Log.w("+++", "Receive...");
			socket.receive(packet);
			Log.w("+++", "Conected.");
			message = new String(lmessage, 0, packet.getLength());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}

	private class RunServerInThread extends Thread{
		private boolean keepRunning = true;
		private String lastmessage = "";
		private boolean isConnected = false;

		@Override
		public void run() {
			String message;
			byte[] lmessage = new byte[MAX_UDP_DATAGRAM_LEN];
			DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);
			DatagramSocket socket = null;
			while(keepRunning){
				try {
					socket = new DatagramSocket(UDP_SERVER_PORT);
					socket.receive(packet);
					//message = new String(lmessage, 0, packet.getLength());
					isConnected = true;
//					textMessage.setText(message);
				} catch (SocketException e) {
					isConnected = false;
					e.printStackTrace();
				} catch (IOException e) {
					isConnected = false;
					e.printStackTrace();
				} finally {
					if (socket != null) {
						socket.close();
					}
					isConnected = false;
				}
			}
		}

		public void sendMessage(String message){
			Log.w("!!!!", "Message is " + message);
		}

		public boolean isConnected(){
			return isConnected;
		}
	}
}
