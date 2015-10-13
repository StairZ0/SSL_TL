package main;
import java.security.Security;

import security.Equipement;


public class main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Equipement firstEquipement = new Equipement("TestEquipement", 0);
		

	}

}
