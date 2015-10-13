package security;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;


public class PaireClesRSA {
	private KeyPair keys;
	
	PaireClesRSA() throws NoSuchAlgorithmException
	{
		SecureRandom rand = new SecureRandom();
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(512,rand);
		keys = kpg.generateKeyPair();
	}
	public PublicKey Publique()
	{
		return keys.getPublic();
		
	}
	public PrivateKey Privee()
	{
		return keys.getPrivate();
		
	}

}
