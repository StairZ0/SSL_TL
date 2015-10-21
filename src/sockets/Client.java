package sockets;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.PublicKey;

import security.Certificat;

import java.util.ArrayList;
import security.DerivateAuthority;



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
			clientSocket = new Socket("127.0.0.1",ServerPort);
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
			oos.writeObject(s);
			oos.flush();
		} catch (Exception e) {
			// Gestion des exceptions
		}
	}
	
	public void sendAuthorities(ArrayList<DerivateAuthority> da){
		try {
			oos.writeObject(da);
			oos.flush();
		} catch (Exception e) {
			// Gestion des exceptions
		}
	}
	
	public ArrayList<DerivateAuthority> receiveAuthorities(){
		try {
			ArrayList<DerivateAuthority> res = (ArrayList<DerivateAuthority>) ois.readObject();
			return res;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String receiveString(){
		try {
			String res = (String) ois.readObject();
			return res;
		} catch (Exception e) {
			return "Exception";
		}
	}
	public void sendCertificate(Certificat o)
	{
		try{
			oos.writeObject(o);
			oos.flush();
		} catch (Exception e) {
			
		}
	}
	public Certificat receiveCertificate()
	{
		try {
			Certificat res = (Certificat) ois.readObject();
			return res;
		} catch (Exception e) {
			return null;
		}
	}
	public void sendPublicKey(PublicKey o)
	{
		try{
			oos.writeObject(o);
			oos.flush();
		} catch (Exception e) {
			
		}
	}
	public PublicKey receivePublicKey()
	{
		try {
			PublicKey res = (PublicKey) ois.readObject();
			return res;
		} catch (Exception e) {
			return null;
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