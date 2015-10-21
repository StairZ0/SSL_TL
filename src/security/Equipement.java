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


	private PaireClesRSA maCle; // La paire de cle de lÃ¢â‚¬â„¢equipement.
	private Certificat monCert; // Le certificat auto-signe.
	private String monNom; // Identite de lÃ¢â‚¬â„¢equipement.
	private int monPort; // Le numÃƒÂ©ro de port dÃ¢â‚¬â„¢ecoute.
	private ArrayList<CertificateAuthority> ca = new ArrayList<CertificateAuthority>();
	private ArrayList<DerivateAuthority> da = new ArrayList<DerivateAuthority>();
	private String socketLogs = "";
	private String details ="";

	public Equipement (String nom, int port) throws Exception {
		monNom=nom;
		monPort=port;
		maCle=new PaireClesRSA();
		monCert = new Certificat(nom, maCle, port);
		monCert.x509.verify(maCle.Publique());
		
		
		
		details=monNom+"\n";
		details+=monPort+"\n\n";
		details+="Certificat : \n"+monCert.x509.toString();
	}


	public void affichage_da () 
	{	
		EquipmentPanel.console.clear();
		String DerivateAuthorities = "";
		
		for(DerivateAuthority d : da)
		{
			DerivateAuthorities+=d.toString()+"\n";
		}
		EquipmentPanel.console.setText(DerivateAuthorities);
	}

	public void affichage_ca () 
	{
		EquipmentPanel.console.clear();
		String CertificateAuthorities = "";
		for(CertificateAuthority d : ca)
		{
			CertificateAuthorities+=d.toString()+"\n";
		}
		EquipmentPanel.console.setText(CertificateAuthorities);
		
	}
	public void affichage() 
	{
		EquipmentPanel.console.clear();
		EquipmentPanel.console.setText(details);
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
		
		String flag = s.getString();
		
		if(!flag.equals("insert")){
			return;
		}
		
		s.sendString("ok");
		
		String idNameDistantEq = s.getString();
		EquipmentPanel.console.clear();
		EquipmentPanel.console.append(socketLogs);
		String log = "Insertion request received from equipment "+idNameDistantEq+"\n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
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
		
		log = "Insertion checked by user, begin to trade certificates \n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		s.sendPublicKey(maCle.Publique());

		PublicKey distantPubKey = s.receivePublicKey();
		

		log="Received Public Key :"+distantPubKey.toString()+"\n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		Certificat certPubKDistant = null;
		try {
			certPubKDistant = new Certificat(monNom, distantPubKey, maCle.Privee(), 100);
		} catch (CertificateEncodingException | InvalidKeyException
				| IllegalStateException | NoSuchProviderException
				| NoSuchAlgorithmException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		s.sendCertificate(certPubKDistant);
		log = "Public key was certified and sent \n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		
		Certificat certCa = s.receiveCertificate();

		log = "Certificate received : \n"+certCa.toString()+"\n\n\n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		CertificateAuthority receivedCA = new CertificateAuthority(idNameDistantEq, certCa, distantPubKey);
		ca.add(receivedCA);
		
		
		
		
	}
	public void insertAsClient(Client c)
	{
		
		c.sendString("insert");
		
		String flag = c.receiveString();
		
		if(!flag.equals("ok")){
			return;
		}
		
		c.sendString(monNom);
		String idNameDistantEq = c.receiveString();
		String log = "Insertion request sent to equipment "+idNameDistantEq+"\n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
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
		{
			log = "Insertion cancelled by user \n";
			EquipmentPanel.console.append(log);
			socketLogs+=log;
			return;
		}
		
		log = "Insertion checked by user, begin to trade certificates \n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		c.sendPublicKey(maCle.Publique());
		PublicKey distantPubKey = c.receivePublicKey();

		log = "Received Public Key :"+distantPubKey.toString()+"\n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		Certificat certPubKDistant = null;
		try {
			certPubKDistant = new Certificat(monNom, distantPubKey, maCle.Privee(), 100);
		} catch (CertificateEncodingException | InvalidKeyException
				| IllegalStateException | NoSuchProviderException
				| NoSuchAlgorithmException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c.sendCertificate(certPubKDistant);
		log = "Public key was certified and sent \n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		Certificat certCa = c.receiveCertificate();

		log = "Certificate received : \n"+certCa.toString()+"\n\n\n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		CertificateAuthority receivedCA = new CertificateAuthority(idNameDistantEq, certCa, distantPubKey);
		ca.add(receivedCA);
	}
	
	public void synchronizeAsServer(Server s) throws Exception{
		
		String flag = s.getString();
		
		if(!flag.equals("synchronize")){
			return;
		}
		s.sendString("ok");
		
		//	Exchanging name and Pubkey with the client
		
		s.sendString(monNom);
		String idNameDistantEq = s.getString();
		
		
		s.sendPublicKey(maCle.Publique());
		PublicKey cPubkey = s.receivePublicKey();
		
		
		String log = "Synchronization with " + idNameDistantEq + "\n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		
		// Need to format the CA to create the authorities list
		
		ArrayList<DerivateAuthority> formatCA = new ArrayList<DerivateAuthority>();
		ArrayList<DerivateAuthority> authorities = new ArrayList<DerivateAuthority>();

		for(CertificateAuthority caEl:ca){
			formatCA.add( caEl.toDA() );
		}
		
		authorities.addAll(formatCA);
		authorities.addAll(da);
		
		// Start the algorithm
		
		int indexDA = -1;
		int indexCA = -1;
		boolean inCA = false;
		boolean inDA = false;
		
		for(int i = 0;i<ca.size();i++){
			if(ca.get(i).getPubkey().equals(cPubkey) && ca.get(i).getIdName().equals(idNameDistantEq)){
				inCA = true;
				indexCA = i;
				log = "Match in CA\n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
			}
		}
		
		for(int i = 0;i<da.size();i++){
			if(da.get(i).getPubkey().equals(cPubkey) && da.get(i).getIdName().equals(idNameDistantEq)){
				indexDA = i;
				inDA = true;
				log = "Match in DA\n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
			}
		}
		
		// If it is in DA but not CA we certificate the key if if neither, we try to find a common link
		
		if(!inCA){
			
			if(inDA)
			{	
				log = "Distant client is trusted \n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				CertificateAuthority newCert = new CertificateAuthority(idNameDistantEq, new Certificat(idNameDistantEq, cPubkey, maCle.Privee(), 365 ), cPubkey);
				ca.add(newCert);
				da.remove(indexDA);
				s.sendString("ok");
				String flag2 = s.getString();
				
				if(!flag2.equals("ok"))
				{
					log = "Searching for common authorities \n";
					EquipmentPanel.console.append(log);
					socketLogs+=log;
					ArrayList<DerivateAuthority> cAuthorities = s.getAuthorities();
					CertificateAuthority commonAuthority = null;
					for(DerivateAuthority da : cAuthorities)
					{
						for(CertificateAuthority c1 : this.ca)
						{
							if(da.getIdName().equals(c1.getIdName()))
							{
								commonAuthority=c1;
								break;
							}
						}
						if(commonAuthority!=null)
						{
							log = "Common Authority found "+commonAuthority.getIdName()+"\n";
							EquipmentPanel.console.append(log);
							socketLogs+=log;
							break;
						}
						
						
					}
					if(commonAuthority==null)
					{
						log = "No common authority was found, sync cancelled \n";
						EquipmentPanel.console.append(log);
						socketLogs+=log;
						return;
					}
					
					
					s.sendCertificate(commonAuthority.getCert());
				}
				
				
			}
			else
			{
				log = "Distant Client isn't trusted\n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				s.sendString("verify");
				s.getString();
				log = "Sending Authorities \n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				s.sendAuthorities(formatCA);
				Certificat cert = s.receiveCertificate();
				log = "Certificate received\n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				int a = 0;
				for(CertificateAuthority c1 : this.ca)
				{
					if(cert.getNom().equals(c1.getIdName()))
					{
						a=1;
						if(!cert.verify(c1.getPubkey()))
						{
							
							log = "Invalid Certificate ,sync cancelled\n";
							EquipmentPanel.console.append(log);
							socketLogs+=log;
							return;
						}
						
						
					}
				}
				if(a==0)
				{
					log = "Wrong Certificate, sync cancelled \n";
					EquipmentPanel.console.append(log);
					socketLogs+=log;
					return;
				}
				log = "Certificate verified, sync started \n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				
			}
			
							
		}
		else
		{
			log = "Distant client is trusted, but auth needed \n";
			EquipmentPanel.console.append(log);
			socketLogs+=log;
			PaireClesRSA keyP = new PaireClesRSA();
			s.sendPublicKey(keyP.Publique());
			PublicKey cRandKey = s.receivePublicKey();
			Certificat randKeyCert = new Certificat(idNameDistantEq, cRandKey, this.maCle.Privee(), 1);
			s.sendCertificate(randKeyCert);
			Certificat cert = s.receiveCertificate();
			if(!cert.verify(this.ca.get(indexCA).getPubkey()))
			{
				log = "Auth unsuccessful, sync cancelled";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				return;
			}
			log = "Auth successful , sync started \n";
			EquipmentPanel.console.append(log);
			socketLogs+=log;
		}
		
		// Need to send the CA if not trusted by the remote correspondant
		
		// When trusted : Send the ca and da
		
		s.sendAuthorities(authorities);
		ArrayList<DerivateAuthority> cAuthorities = s.getAuthorities();
		
		
		
		// Then we synchronize our datas with the union of CA and DA from both
		
		boolean isIn = false;
		
		for(int i = 0;i<cAuthorities.size();i++){
			
			isIn = false;
			
			for(int j = 0;j<authorities.size();j++){
				
				if(cAuthorities.get(i).getIdName().equals(authorities.get(j).getIdName())
					&& cAuthorities.get(i).getPubkey().equals(authorities.get(i).getPubkey())){
					isIn = true;
				}
				if(cAuthorities.get(i).getIdName().equals(this.monNom))
				{
					isIn = true;
				}
			}
			
			if(!isIn){
				da.add(new DerivateAuthority(cAuthorities.get(i).getIdName(), cAuthorities.get(i).getPubkey()));
			}
			
		}	
		log = "Sync over\n\n\n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		
	}
	
	public void synchronizeAsClient(Client c) throws Exception{
		
		c.sendString("synchronize");
		String flag = c.receiveString();
		if(!flag.equals("ok")){
			return;
		}
		
		// Exchanging name and Pubkey with the server
		
		c.sendString(monNom);
		String idNameDistantEq = c.receiveString();
		
		
		c.sendPublicKey(maCle.Publique());
		PublicKey cPubkey = c.receivePublicKey();
		
		
		String log = "Synchronization with " + idNameDistantEq + "\n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		
		// Need to format the CA to create the authorities list
		
		ArrayList<DerivateAuthority> formatCA = new ArrayList<DerivateAuthority>();
		ArrayList<DerivateAuthority> authorities = new ArrayList<DerivateAuthority>();

		for(CertificateAuthority caEl:ca){
			formatCA.add( caEl.toDA() );
		}
		
		authorities.addAll(formatCA);
		authorities.addAll(da);
		
		// Start the algorithm
		
		int indexDA = -1;
		int indexCA = -1;
		boolean inCA = false;
		boolean inDA = false;
		
		for(int i = 0;i<ca.size();i++){
			if(ca.get(i).getPubkey().equals(cPubkey) && ca.get(i).getIdName().equals(idNameDistantEq)){
				inCA = true;
				indexCA = i;
				log = "Match in CA\n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
			}
		}
		
		for(int i = 0;i<da.size();i++){
			if(da.get(i).getPubkey().equals(cPubkey) && da.get(i).getIdName().equals(idNameDistantEq)){
				indexDA = i;
				inDA = true;
				log = "Match in DA\n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
			}
		}
		
		// If it is in DA but not CA we certificate the key if if neither, we try to find a common link
		
		if(!inCA){
			
			if(inDA)
			{	
				log = "Distant server is trusted \n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				CertificateAuthority newCert = new CertificateAuthority(idNameDistantEq, new Certificat(idNameDistantEq, cPubkey, maCle.Privee(), 365 ), cPubkey);
				ca.add(newCert);
				da.remove(indexDA);
				c.sendString("ok");
				String flag2 = c.receiveString();
				
				if(!flag2.equals("ok"))
				{
					log = "Searching for common authorities \n";
					EquipmentPanel.console.append(log);
					socketLogs+=log;
					ArrayList<DerivateAuthority> cAuthorities = c.receiveAuthorities();
					CertificateAuthority commonAuthority = null;
					for(DerivateAuthority da : cAuthorities)
					{
						for(CertificateAuthority c1 : this.ca)
						{
							if(da.getIdName().equals(c1.getIdName()))
							{
								commonAuthority=c1;
								break;
							}
						}
						if(commonAuthority!=null)
						{
							log = "Common Authority found "+commonAuthority.getIdName()+"\n";
							EquipmentPanel.console.append(log);
							socketLogs+=log;
							break;
						}
						
						
					}
					if(commonAuthority==null)
					{
						log = "No common authority was found, sync cancelled \n";
						EquipmentPanel.console.append(log);
						socketLogs+=log;
						return;
					}
					
					
					c.sendCertificate(commonAuthority.getCert());
				}
				
				
			}
			else
			{
				log = "Distant Client isn't trusted\n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				c.sendString("verify");
				c.receiveString();
				log = "Sending Authorities \n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				c.sendAuthorities(formatCA);
				Certificat cert = c.receiveCertificate();
				log = "Certificate received\n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				int a = 0;
				for(CertificateAuthority c1 : this.ca)
				{
					if(cert.getNom().equals(c1.getIdName()))
					{
						a=1;
						if(!cert.verify(c1.getPubkey()))
						{
							
							log = "Invalid Certificate ,sync cancelled\n";
							EquipmentPanel.console.append(log);
							socketLogs+=log;
							return;
						}
						
						
					}
				}
				if(a==0)
				{
					log = "Wrong Certificate, sync cancelled \n";
					EquipmentPanel.console.append(log);
					socketLogs+=log;
					return;
				}
				log = "Certificate verified, sync started \n";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				
			}
			
							
		}
		else
		{
			log = "Distant client is trusted, but auth needed \n";
			EquipmentPanel.console.append(log);
			socketLogs+=log;
			PaireClesRSA keyP = new PaireClesRSA();
			c.sendPublicKey(keyP.Publique());
			PublicKey cRandKey = c.receivePublicKey();
			Certificat randKeyCert = new Certificat(idNameDistantEq, cRandKey, this.maCle.Privee(), 1);
			c.sendCertificate(randKeyCert);
			Certificat cert = c.receiveCertificate();
			if(!cert.verify(this.ca.get(indexCA).getPubkey()))
			{
				log = "Auth unsuccessful, sync cancelled";
				EquipmentPanel.console.append(log);
				socketLogs+=log;
				return;
			}
			log = "Auth successful , sync started \n";
			EquipmentPanel.console.append(log);
			socketLogs+=log;
		}
		
		// Need to send the CA if not trusted by the remote correspondant
		
		// When trusted : Send the ca and da
		
		c.sendAuthorities(authorities);
		ArrayList<DerivateAuthority> cAuthorities = c.receiveAuthorities();
		
	
		
		// Then we synchronize our datas with the union of CA and DA from both
		
		boolean isIn = false;
		
		for(int i = 0;i<cAuthorities.size();i++){
			
			isIn = false;
			
			for(int j = 0;j<authorities.size();j++){
				
				if(cAuthorities.get(i).getIdName().equals(authorities.get(j).getIdName())
					&& cAuthorities.get(i).getPubkey().equals(authorities.get(i).getPubkey())){
					isIn = true;
				}
				if(cAuthorities.get(i).getIdName().equals(this.monNom))
				{
					isIn = true;
				}
			}
			
			if(!isIn){
				da.add(new DerivateAuthority(cAuthorities.get(i).getIdName(), cAuthorities.get(i).getPubkey()));
			}
			
		}	
		log = "Sync over\n\n\n";
		EquipmentPanel.console.append(log);
		socketLogs+=log;
		
	}

	
	
	public void closeServer(Server s){
		s.closeStreams();
		s.closeConnection();
		s.stopServer();
	}
	
	public void closeClient(Client c){
		c.closeStreams();
		c.closeConnection();
	}

}
