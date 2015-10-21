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
		
		String flag = s.getString();
		
		if(!flag.equals("insert")){
			return;
		}
		
		s.sendString("ok");
		
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
		s.sendPublicKey(maCle.Publique());
//		try {
//			s.sendString(PEMUtils.encodePEM(monCert));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
		//String distantCert = s.getString();
		PublicKey distantPubKey = s.receivePublicKey();
		
//		try {
//			distantPubKey = PEMUtils.decodePEM(distantCert).x509.getPublicKey();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
//		try {
//			s.sendString(PEMUtils.encodePEM(certPubKDistant));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		s.sendCertificate(certPubKDistant);
		EquipmentPanel.console.append("Public key was certified and sent \n");
		//String CA = s.getString();
		Certificat certCa = s.receiveCertificate();
//		try {
//			certCa = PEMUtils.decodePEM(CA);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		EquipmentPanel.console.append("Certificate received : \n"+certCa.toString());
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
		c.sendPublicKey(maCle.Publique());
//		try {
//			s.sendString(PEMUtils.encodePEM(monCert));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
		//String distantCert = s.getString();
		PublicKey distantPubKey = c.receivePublicKey();
		
//		try {
//			distantPubKey = PEMUtils.decodePEM(distantCert).x509.getPublicKey();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
//		try {
//			s.sendString(PEMUtils.encodePEM(certPubKDistant));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		c.sendCertificate(certPubKDistant);
		EquipmentPanel.console.append("Public key was certified and sent \n");
		//String CA = s.getString();
		Certificat certCa = c.receiveCertificate();
//		try {
//			certCa = PEMUtils.decodePEM(CA);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		EquipmentPanel.console.append("Certificate received : \n"+certCa.toString());
		CertificateAuthority receivedCA = new CertificateAuthority(idNameDistantEq, certCa, distantPubKey);
		ca.add(receivedCA);
	}
	
	public void synchronizeAsServer(Server s) throws Exception{
		
		String flag = s.getString();
		
		while( flag != "synchronize"){
			flag = s.getString();
		}
		
		s.sendString("ok");
		
		//	Exchanging datas with the client
		
		String idNameDistantEq = s.getString();
		s.sendString(monNom);
		
		PublicKey cPubkey = s.receivePublicKey();
		
		// Need to format the CA to create the authorities list
		
		ArrayList<DerivateAuthority> formatCA = new ArrayList<DerivateAuthority>();
		
		for(CertificateAuthority caEl:ca){
			formatCA.add( caEl.toDA() );
		}
		
		ArrayList<DerivateAuthority> authorities = new ArrayList<DerivateAuthority>();
		
		authorities.addAll(formatCA);
		authorities.addAll(da);
		
		// Start the algorithm
		
		int indexDA = -1;
		boolean inCA = false;
		boolean inDA = false;
		
		// Test if the machine is in the own ca or da
		
		for(int i = 0;i<ca.size();i++){
			if(ca.get(i).getPubkey().equals(cPubkey) && ca.get(i).getIdName().equals(idNameDistantEq)){
				inCA = true;
			}
		}
		
		for(int i = 0;i<da.size();i++){
			if(da.contains(new DerivateAuthority( idNameDistantEq, cPubkey))){
				indexDA = i;
				inDA = true;
			}
		}
		
		// If it is in DA but not CA we certificate the key if if neither, we try to find a common link
		
		if(!inCA){
			
			if(inDA){
			
				CertificateAuthority newCert = new CertificateAuthority(idNameDistantEq, new Certificat(idNameDistantEq, cPubkey, maCle.Privee(), 365 ), cPubkey);
				ca.add(newCert);
				da.remove(indexDA);
				
			}else{ 
				ArrayList<DerivateAuthority> cCA = s.getAuthorities();
				
				// test if there is a common Authority and test the certif
				
			}
			
		}
		
		// Need to send the CA if not trusted by the remote correspondant
		
		// When trusted : Send the ca and da
		
		ArrayList<DerivateAuthority> cAuthorities = s.getAuthorities();
		
		s.sendAuthorities(authorities);
		
		// Then we synchronize our datas with the union of CA and DA from both
		
		boolean isIn = false;
		
		for(int i = 0;i<cAuthorities.size();i++){
			
			isIn = false;
			
			for(int j = 0;j<authorities.size();j++){
				
				if(cAuthorities.get(i).getIdName().equals(authorities.get(j).getIdName())
					&& cAuthorities.get(i).getPubkey().equals(authorities.get(i).getPubkey())){
					isIn = true;
				}
			}
			
			if(!isIn){
				da.add(new DerivateAuthority(cAuthorities.get(i).getIdName(), cAuthorities.get(i).getPubkey()));
			}
			
		}		
		
	}
	
	public void synchronizeAsClient(Client c) throws Exception{
		
		c.sendString("insert");
		
		String flag = c.receiveString();
		
		if(flag != "ok"){
			return;
		}
		
		c.sendString(monNom);
		String idNameDistantEq = c.receiveString();
		try {
			c.sendString(PEMUtils.encodePEM(monCert));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String cert = c.receiveString();
		
		PublicKey cPubkey = null;
		
		try{
			cPubkey = PEMUtils.decodePEM(cert).x509.getPublicKey();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Need to format the CA to create the authorities list
	
			ArrayList<DerivateAuthority> formatCA = new ArrayList<DerivateAuthority>();
			
			for(CertificateAuthority caEl:ca){
				formatCA.add( caEl.toDA() );
			}
			
			ArrayList<DerivateAuthority> authorities = new ArrayList<DerivateAuthority>();
			
			authorities.addAll(formatCA);
			authorities.addAll(da);
			
			// Start the algorithm
			
			int indexDA = -1;
			boolean inCA = false;
			boolean inDA = false;
			
			// Test if the machine is in the own ca or da
			
			for(int i = 0;i<ca.size();i++){
				if(ca.get(i).getPubkey().equals(cPubkey) && ca.get(i).getIdName().equals(idNameDistantEq)){
					inCA = true;
				}
			}
			
			for(int i = 0;i<da.size();i++){
				if(da.contains(new DerivateAuthority( idNameDistantEq, cPubkey))){
					indexDA = i;
					inDA = true;
				}
			}
			
			// If it is in DA but not CA we certificate the key if if neither, we try to find a common link
			
			if(!inCA){
				
				if(inDA){
				
					CertificateAuthority newCert = new CertificateAuthority(idNameDistantEq, new Certificat(idNameDistantEq, cPubkey, maCle.Privee(), 365 ), cPubkey);
					ca.add(newCert);
					da.remove(indexDA);
					
				}else{ 
					ArrayList<DerivateAuthority> cCA = c.receiveAuthorities();
					
					// test if there is a common Authority and test the certif
					
				}
				
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
				}
				
				if(!isIn){
					da.add(new DerivateAuthority(cAuthorities.get(i).getIdName(), cAuthorities.get(i).getPubkey()));
				}
				
			}		
			
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
