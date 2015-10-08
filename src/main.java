import java.security.Security;


public class main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Equipement firstEquipement = new Equipement("TestEquipement", 0);
		System.out.println(firstEquipement.monCertif().x509.toString());

	}

}
