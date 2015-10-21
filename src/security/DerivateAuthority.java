package security;

import java.io.Serializable;
import java.security.PublicKey;

public class DerivateAuthority implements Serializable{
	
	private String idName;
	private PublicKey pubk;
	
	
	public DerivateAuthority(String idName,PublicKey pubk)
	{
		this.idName=idName;
		this.pubk=pubk;
	}
	
	@Override
	public String toString()
	{
		String string = idName+"\n"+"Public Key :"+pubk.toString();
		return string;
		
	}
	
	public PublicKey getPubkey(){
		return pubk;
	}
	
	public String getIdName(){
		return idName;
	}
	

}
