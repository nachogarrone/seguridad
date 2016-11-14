package com.ucu.seguridad;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import javax.security.auth.callback.*;

import sun.security.pkcs11.SunPKCS11;

public class PKCS11Util {
	
	/*public static KeyStore loadKeystore(CallbackHandler passwordCallbackHandler) {
		Properties config = new Properties();
		/*config.put("library", dll);
		config.put("name", "gclib.dll");
		System.out.println("Config library: "+config.getProperty("library").toString());
		System.out.println("Config name: "+config.getProperty("name").toString());
		return loadKeystore(passwordCallbackHandler);
	}*/

	public static KeyStore loadKeystore(CallbackHandler passwordCallbackHandler) throws IOException {
		try {
			
			Provider pkcs11Prov = createPkcs11Provider();
			CallbackHandlerProtection pwCallbackProt = new CallbackHandlerProtection(passwordCallbackHandler);
			KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", pkcs11Prov, pwCallbackProt);
			return builder.getKeyStore();
		} catch (KeyStoreException e) {
			throw new RuntimeException("Failed to load PKCS#11 Keystore",e);
		}
	}

	private static Provider createPkcs11Provider() throws IOException {
		/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
		config.store(baos, null);
		System.out.println("Config library: "+config.getProperty("library").toString());
		System.out.println("Config name: "+config.getProperty("name").toString());
		//System.out.println("Config library: "+config.getProperty("baos").toString());*/
		SunPKCS11 result = new sun.security.pkcs11.SunPKCS11("C:\\Users\\alfonso\\workspace32\\Seguridad\\ConfigAux.cfg");
		if (result.getService("KeyStore", "PKCS11") == null) {
			throw new RuntimeException("No PKCS#11 Service available. Probably Security Token (Smartcard) not inserted");
		}
		System.out.println("sali del new SunPKCS11");
		// Register the Provider 
		if (Security.getProvider(result.getName()) != null) {
			Security.removeProvider(result.getName());
		}
		Security.addProvider(result);			
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
		// Test for the DataKey 330 Smartcard 
		// (dskck201.dll is installed with CIP Utilities from DataKey)
		SwingPasswordCallbackHandler swing = new SwingPasswordCallbackHandler();
		KeyStore ks = loadKeystore(swing );
		
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
