import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateParsingException;
import java.util.Calendar;
import java.util.Date;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.TBSCertificate;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.jce.provider.AnnotatedException;

public class CopyOfCertificat {
	
	static private BigInteger seqnum = BigInteger.ZERO;
	public X509CertificateObject x509;
	
	@SuppressWarnings({ "deprecation", "resource" })
	public CopyOfCertificat(String nom, PaireClesRSA cle,int validityDays) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, CertificateParsingException
	{
		SecureRandom rand = new SecureRandom();
		
		PublicKey pubKey = cle.Publique();
		PrivateKey privKey = cle.Privee();
		
		V3TBSCertificateGenerator certGen = new V3TBSCertificateGenerator();
		
		Calendar expiry = Calendar.getInstance();
		Date startDate = expiry.getTime();
		expiry.add(Calendar.DAY_OF_YEAR, validityDays);
		Date expiryDate = expiry.getTime();
		certGen.setStartDate(new Time(startDate));
		certGen.setEndDate(new Time(expiryDate));
		seqnum.add(BigInteger.ONE);
		
		certGen.setSerialNumber((ASN1Integer) new ASN1Integer(seqnum));
		X500Name x509Name = new X500Name("CN="+nom);
		certGen.setIssuer(x509Name);
		certGen.setSubject(x509Name);
		
		certGen.setSubjectPublicKeyInfo(new SubjectPublicKeyInfo(
				(ASN1Sequence) new ASN1InputStream(
						new ByteArrayInputStream(pubKey.getEncoded())
						).readObject()
				)
				);
		ASN1ObjectIdentifier sigOID = PKCSObjectIdentifiers.sha1WithRSAEncryption;
		AlgorithmIdentifier sigAlgId =
				new AlgorithmIdentifier(sigOID, new DERNull());
		certGen.setSignature(sigAlgId);
		TBSCertificate tbsCert = certGen.generateTBSCertificate();
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		ASN1OutputStream dOut = new ASN1OutputStream(bOut);
		dOut.writeObject(tbsCert);
		byte[] signature;
		Signature sig = Signature.getInstance(sigOID.getId());
		sig.initSign(privKey,rand);
		sig.update(bOut.toByteArray());
		signature = sig.sign();
		ASN1EncodableVector v = new ASN1EncodableVector();
		v.add(tbsCert);
		v.add(sigAlgId);
		v.add(new DERBitString(signature));
		
		Certificate cert = Certificate.getInstance(ASN1Sequence.getInstance(v));
		
		X509CertificateObject x509 = new X509CertificateObject(cert);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	

}
