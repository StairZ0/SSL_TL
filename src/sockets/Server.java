package sockets;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

import security.Certificat;
import security.Equipement;
import java.util.ArrayList;

import security.DerivateAuthority;

public class Server {
	
	private Equipement equip = null;
	private ServerSocket serverSocket = null;
	private Socket NewServerSocket = null;
	public InputStream NativeIn = null;
	public ObjectInputStream ois = null;
	public OutputStream NativeOut = null;
	public ObjectOutputStream oos = null;
	
	public Server(Equipement eq){
		equip = eq;
	}
	
	public void createSocket(){
		try {
			serverSocket = new ServerSocket(equip.monPort());
		} catch (IOException e) {
			// Gestion des exceptions
		}
	}
	
	public void waitConnections(){
		try {
			NewServerSocket = serverSocket.accept();
		} catch (Exception e) {
			// Gestion des exceptions
		}
	}
	
	public void createStreams(){
		try {
		NativeIn = NewServerSocket.getInputStream();
		ois = new ObjectInputStream(NativeIn);
		NativeOut = NewServerSocket.getOutputStream();
		oos = new ObjectOutputStream(NativeOut);
		} catch (IOException e) {
		// Gestion des exceptions
		}
	}
	
	public String getString(){
		try {
			String res = (String) ois.readObject();
			return res;
		} catch (Exception e) {
			return "Exception";
		}
	}
	
	public ArrayList<DerivateAuthority> getAuthorities(){
		try {
			ArrayList<DerivateAuthority> res = (ArrayList<DerivateAuthority>) ois.readObject();
			return res;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void sendString(String s){
		try {
			oos.writeObject(s);
			oos.flush();
		} catch (Exception e) {
			// Gestion des exceptions
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
			NewServerSocket.close();
		} catch (IOException e) {
			// Gestion des exceptions
		}
	}
	public void stopServer(){
		try {
			serverSocket.close();
		} catch (IOException e) {
			// Gestion des exceptions
		}
	}
}
