package sockets;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	
	private int ServerPort = 0;
	private String ServerName = null;
	private Socket clientSocket = null;
	public InputStream NativeIn = null;
	public ObjectInputStream ois = null;
	public OutputStream NativeOut = null;
	public ObjectOutputStream oos = null;
	
	public Client(int a, String name){
		ServerPort = a;
		ServerName = name;	
	}
	
	public void createSocket (){
		try {
			clientSocket = new Socket(ServerName,ServerPort);
		} catch (Exception e) {
			// Gestion des exceptions
		}
	}
	
	public void createStreams(){
		try {
			NativeOut = clientSocket.getOutputStream();
			oos = new ObjectOutputStream(NativeOut);
			NativeIn = clientSocket.getInputStream();
			ois = new ObjectInputStream(NativeIn);
		} catch (Exception e) {
			// Gestion des exceptions
		}
	}
	
	public void sendString (String s){
		try {
			oos.writeObject("Bonjour");
			oos.flush();
		} catch (Exception e) {
			// Gestion des exceptions
		}
	}
	
	public void receiveString(){
		try {
			String res = (String) ois.readObject();
			System.out.println(res);
		} catch (Exception e) {
			// Gestion des exceptions
		}
	}
	
	public void closeStreams(){
		try {
			ois.close();
			oos.close();
			NativeIn.close();
			NativeOut.close();
		} catch (IOException e) {
			// Gestion des exceptions
		}
	}
	
	public void closeConnection(){
		try {
			clientSocket.close();
		} catch (IOException e) {
			// Gestion des exceptions
		}
	}

}