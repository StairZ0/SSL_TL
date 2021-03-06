package security;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.TBSCertificate;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.x509.X509V1CertificateGenerator;

public class Certificat implements Serializable{

	static private BigInteger seqnum = BigInteger.ZERO;
	public X509Certificate x509;
	private String nom;

	@SuppressWarnings({ "deprecation", "resource" })
	public Certificat(String nom, PaireClesRSA cle,int validityDays) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, CertificateParsingException, CertificateEncodingException, IllegalStateException, NoSuchProviderException
	{
		this.setNom(nom);
		PublicKey pubkey = cle.Publique();
		PrivateKey privkey = cle.Privee();
		
		X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();

		Calendar expiry = Calendar.getInstance();
		Date startDate = expiry.getTime();
		expiry.add(Calendar.DAY_OF_YEAR, 10);
		Date expiryDate = expiry.getTime();
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
	
		seqnum=seqnum.add(BigInteger.ONE);
		certGen.setSerialNumber(seqnum);
		
		X500Principal cnName = new X500Principal("CN="+nom);
		certGen.setSubjectDN(cnName);
		certGen.setIssuerDN(cnName);
	
		certGen.setSignatureAlgorithm("sha1WithRSA");
	
		certGen.setPublicKey(pubkey);
		
		this.x509= certGen.generate(privkey, "BC");
		

	}
	
	public Certificat(String nom, PublicKey clePublique, PrivateKey clePrivee, int validityDays) throws CertificateEncodingException, InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException{
		
		this.setNom(nom);
		PublicKey pubkey = clePublique;
		PrivateKey privkey = clePrivee;
		
		X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
		
		Calendar expiry = Calendar.getInstance();
		Date startDate = expiry.getTime();
		expiry.add(Calendar.DAY_OF_YEAR, 10);
		Date expiryDate = expiry.getTime();
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
		
		seqnum=seqnum.add(BigInteger.ONE);
		certGen.setSerialNumber(seqnum);
		
		X500Principal cnName = new X500Principal("CN="+nom);
		certGen.setSubjectDN(cnName);
		certGen.setIssuerDN(cnName);
	
		certGen.setSignatureAlgorithm("sha1WithRSA");
	
		certGen.setPublicKey(pubkey);
		
		this.x509= certGen.generate(privkey, "BC");
	}

	

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public boolean verify(PublicKey pk)
	{
		try {
			x509.verify(pk);
		} catch (InvalidKeyException | CertificateException
				| NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}

	


}
