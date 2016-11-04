

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Provider;
import java.security.Security;
import java.security.KeyStore.CallbackHandlerProtection;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Properties;

import javax.security.auth.callback.CallbackHandler;

import sun.security.pkcs11.SunPKCS11;

public class PKCS11Util {
	
	public static KeyStore loadKeystore(String dll, CallbackHandler passwordCallbackHandler) {
		Properties config = new Properties();
		config.put("library", dll);
		config.put("name", dll);
		return loadKeystore(config, passwordCallbackHandler);
	}

	public static KeyStore loadKeystore(Properties config, CallbackHandler passwordCallbackHandler) {
		try {
			Provider pkcs11Prov = createPkcs11Provider(config);
			CallbackHandlerProtection pwCallbackProt = new CallbackHandlerProtection(passwordCallbackHandler);
			KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", pkcs11Prov, pwCallbackProt);
			return builder.getKeyStore();
		} catch (KeyStoreException e) {
			throw new RuntimeException("Failed to load PKCS#11 Keystore",e);
		}
	}

	private static Provider createPkcs11Provider(Properties config) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			config.store(baos, null);
			SunPKCS11 result = new SunPKCS11(new ByteArrayInputStream(baos.toByteArray()));
			if (result.getService("KeyStore", "PKCS11") == null) {
				throw new RuntimeException("No PKCS#11 Service available. Probably Security Token (Smartcard) not inserted");
			}
			
			// Register the Provider 
			if (Security.getProvider(result.getName()) != null) {
				Security.removeProvider(result.getName());
			}
			Security.addProvider(result);			
			return result;
		} catch (IOException e) {
			throw new RuntimeException("Failed to install SUN PKCS#11 Provider",e);
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		// Test for the DataKey 330 Smartcard 
		// (dskck201.dll is installed with CIP Utilities from DataKey)
		
		KeyStore ks = loadKeystore("dkck201.dll", new SwingPasswordCallbackHandler());
		
		for(Enumeration<String> aliases = ks.aliases(); aliases.hasMoreElements(); ) {
			String alias = aliases.nextElement();
			System.out.println(alias);
			
			// print certifcate
			Certificate cert = ks.getCertificate(alias);
			if (cert != null) {
				System.out.print(" Certificate found. type="+cert.getType());
				if (cert instanceof X509Certificate) {
					X509Certificate x509 = (X509Certificate)cert;
					System.out.print(" SubjectDN="+x509.getSubjectDN());
				}
				System.out.println();
			}
			
			// private key is accessed without password 
			Key pk = ks.getKey(alias, null);
			if (pk != null) {
				System.out.println(" Private key found. algorithm="+pk.getAlgorithm());
			}
			
			System.out.println();
		}
	}
	
}
