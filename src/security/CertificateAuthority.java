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
		this.setCert(cert);
		this.pubk=pubk;
	}
	public CertificateAuthority()
	{
		
	}
	
	public DerivateAuthority toDA(){
		return new DerivateAuthority(idName, pubk);
	}
	
	@Override
	public String toString()
	{
		String string = idName+"\n"+"Public Key :"+pubk.toString()+"\n";
		return string;
	}
	
	public PublicKey getPubkey(){
		return pubk;
	}
	
	public String getIdName(){
		return idName;
	}
	public Certificat getCert() {
		return cert;
	}
	public void setCert(Certificat cert) {
		this.cert = cert;
	}
	

}
