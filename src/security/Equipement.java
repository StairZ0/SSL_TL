package security;
import java.security.PublicKey;
import java.security.Security;

public class Equipement {


	private PaireClesRSA maCle; // La paire de cle de l’equipement.
	private Certificat monCert; // Le certificat auto-signe.
	private String monNom; // Identite de l’equipement.
	private int monPort; // Le numéro de port d’ecoute.

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

}
