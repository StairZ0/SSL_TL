package security;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;

import sockets.Client;
import sockets.Server;

public class Equipement {


	private PaireClesRSA maCle; // La paire de cle de l’equipement.
	private Certificat monCert; // Le certificat auto-signe.
	private String monNom; // Identite de l’equipement.
	private int monPort; // Le numéro de port d’ecoute.
	private ArrayList<CertificateAuthority> ca = new ArrayList<CertificateAuthority>();
	private ArrayList<DerivateAuthority> da = new ArrayList<DerivateAuthority>();

	public Equipement (String nom, int port) throws Exception {
		monNom=nom;
		monPort=port;
		maCle=new PaireClesRSA();
		monCert = new Certificat(nom, maCle, port);
		monCert.x509.verify(maCle.Publique());

	}


	public void affichage_da () 
	{

	}

	public void affichage_ca () 
	{

	}
	public void affichage() 
	{

	}






	public String monNom ()
	{
		return monNom;

	}
	public PublicKey maClePub() 
	{
		return maCle.Publique();

	}
	public Certificat monCertif() 
	{
		return monCert;
	}
	public int monPort() 
	{
		return monPort;
	}
	
	public Server createServer(){
		Server s = new Server(this);
		s.createSocket();
		s.waitConnections();
		s.createStreams();
		return s;
	}
	
	public Client createClient(int port){
		Client c = new Client(port, this.monNom);
		c.createSocket();
		c.createStreams();
		return c;
	}
	
	public void closeServer(Server s){
		s.closeStreams();
		s.closeConnection();
	}
	
	public void closeClient(Client c){
		c.closeStreams();
		c.closeConnection();
	}

}
