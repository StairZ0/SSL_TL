package security;

import java.io.Serializable;
import java.security.PublicKey;



public class CertificateAuthority {
	
	private String idName;
	private Certificat cert;
	private PublicKey pubk;
	
	public CertificateAuthority(String idName,Certificat cert,PublicKey pubk)
	{
		this.idName=idName;
		this.cert=cert;
		this.pubk=pubk;
	}
	public CertificateAuthority()
	{
		
	}
	
	@Override
	public String toString()
	{
		String string = idName+"\n"+"Public Key :"+pubk.toString()+"\n"+cert.x509.toString();
		return string;
		
	}
	
	public PublicKey getPubkey(){
		return pubk;
	}
	
	public String getIdName(){
		return idName;
	}
	

}
