package com.asinenko.udpserver;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class TCPClient {

	public static final int STATUS_DISCONNECTED = 0;// подключаемся
	public static final int STATUS_CONNECTED = 1;// подключено

	private PrintWriter out;
	DataInputStream dataInputStream; 
	private Socket socket;

	public static String SERVER = "192.168.77.149";
	public static int PORT = 28333;

	public TCPClient() {

	}

	public synchronized boolean sendMessage(String message){
		if (out != null && !out.checkError()) {
			byte buffer []= new byte[16];
			try {
				out.println(message);
				out.flush();
				dataInputStream.read(buffer);
				if(new String(buffer).startsWith("1")){
				//if(a.equals(new String(buffer))){
					return true;
				}
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public void closeConnection(){
		try {
			if(out != null)
				out.close();
			if(dataInputStream != null)
				dataInputStream.close();
			if(socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static boolean isReachable() {
		return  true;
//		try {
//			boolean res = InetAddress.getByName(SERVER).isReachable(1000);
//			return res;
//		}catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return true;
//		} 
	}

	public boolean isConnected(){
		if(socket != null && socket.isConnected()){
			boolean connected = socket.isConnected() && ! socket.isClosed();
			return connected;
//			try {
//				socket.getInputStream().read();
//			} catch (IOException e) {
//				return false;
//			}
			//return socket.isConnected();
			//boolean connected = socket.isConnected() && ! socket.isClosed();"
		}
		return false;
	}

	public void run() throws IOException{
		if(socket == null || !socket.isConnected()){
			InetAddress serverAddr = InetAddress.getByName(SERVER);

			socket = new Socket(serverAddr, PORT);
//			socket.setKeepAlive(true);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			dataInputStream = new DataInputStream(socket.getInputStream()); 
		}
	}
}
