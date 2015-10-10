import java.io.StringWriter;
import org.bouncycastle.openssl.*;
import java.io.StringReader;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
public class PEMUtils {


	public static String encodePEM(Certificat c) throws Exception {
	    StringWriter sw = new StringWriter();
	    PEMWriter pw = new PEMWriter(sw);
	    pw.writeObject(c.x509); 
	    pw.flush();
	    pw.close();
	    return sw.toString();
	}
	

	public static Certificat decodePEM(String pemcert) throws Exception {
	    StringReader sr = new StringReader(pemcert);
	    PEMParser parser = new PEMParser(sr);
	    X509CertificateHolder holder = (X509CertificateHolder) parser.readObject();
	    parser.close();

	    JcaX509CertificateConverter conv = new JcaX509CertificateConverter();
	    return new Certificat(conv.getCertificate(holder)); 
	}
}
