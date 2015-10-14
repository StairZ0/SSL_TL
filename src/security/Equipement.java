package security;
import ihm.EquipmentPanel;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.Main;
import sockets.Client;
import sockets.Server;
import utils.PEMUtils;

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
	
	public Client createClient(){
		Client c = new Client(monPort, this.monNom);
		c.createSocket();
		c.createStreams();
		return c;
	}
	public void insertAsServer(Server s)
	{
		String idNameDistantEq = s.getString();
		EquipmentPanel.console.clear();
		EquipmentPanel.console.append("Insertion request received from equipment "+idNameDistantEq+"\n");
		s.sendString(monNom);
		
		Object[] options = {"Yes",
		                    "No"
		                    };
		int n = JOptionPane.showOptionDialog(Main.frame,
		    "Would you like to insert equipment "
		    + idNameDistantEq,
		    "Insertion Check",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[1]);
		if(n==1)
		return;
		
		EquipmentPanel.console.append("Insertion checked by user, begin to trade certificates \n");
		try {
			s.sendString(PEMUtils.encodePEM(monCert));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String distantCert = s.getString();
		PublicKey distantPubKey = null;
		try {
			distantPubKey = PEMUtils.decodePEM(distantCert).x509.getPublicKey();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EquipmentPanel.console.append("Received Public Key :"+distantPubKey.toString()+"\n");
		Certificat certPubKDistant = null;
		try {
			certPubKDistant = new Certificat(monNom, distantPubKey, maCle.Privee(), 100);
		} catch (CertificateEncodingException | InvalidKeyException
				| IllegalStateException | NoSuchProviderException
				| NoSuchAlgorithmException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			s.sendString(PEMUtils.encodePEM(certPubKDistant));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EquipmentPanel.console.append("Public key was certified and sent \n");
		String CA = s.getString();
		Certificat certCa = null;
		try {
			certCa = PEMUtils.decodePEM(CA);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EquipmentPanel.console.append("Certificate received : \n"+certCa.toString());
		CertificateAuthority receivedCA = new CertificateAuthority(idNameDistantEq, certCa, distantPubKey);
		ca.add(receivedCA);
		
		
		
		
	}
	public void insertAsClient(Client c)
	{
		c.sendString(monNom);
		String idNameDistantEq = c.receiveString();
		EquipmentPanel.console.append("Insertion request sent to equipment "+idNameDistantEq+"\n");
		Object[] options = {"Yes",
                "No"
                };
		int n = JOptionPane.showOptionDialog(Main.frame,
				"Would you like to insert equipment "
						+ idNameDistantEq,
						"Insertion Check",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[1]);
		if(n==1)
		return;
		EquipmentPanel.console.append("Insertion checked by user, begin to trade certificates \n");
		try {
			c.sendString(PEMUtils.encodePEM(monCert));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String distantCert = c.receiveString();
		PublicKey distantPubKey = null;
		try {
			distantPubKey = PEMUtils.decodePEM(distantCert).x509.getPublicKey();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EquipmentPanel.console.append("Received Public Key :"+distantPubKey.toString()+"\n");
		Certificat certPubKDistant = null;
		try {
			certPubKDistant = new Certificat(monNom, distantPubKey, maCle.Privee(), 100);
		} catch (CertificateEncodingException | InvalidKeyException
				| IllegalStateException | NoSuchProviderException
				| NoSuchAlgorithmException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			c.sendString(PEMUtils.encodePEM(certPubKDistant));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EquipmentPanel.console.append("Public key was certified and sent \n");
		String CA = c.receiveString();
		Certificat certCa = null;
		try {
			certCa = PEMUtils.decodePEM(CA);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EquipmentPanel.console.append("Certificate received : \n"+certCa.toString());
		CertificateAuthority receivedCA = new CertificateAuthority(idNameDistantEq, certCa, distantPubKey);
		ca.add(receivedCA);
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
