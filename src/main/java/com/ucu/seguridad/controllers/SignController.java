package com.ucu.seguridad.controllers;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.ucu.seguridad.SwingPasswordCallbackHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.NoSuchElementException;

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

/**
 * Created by nachogarrone on 10/29/16.
 */
@Component
public class SignController {
    private static PrivateKey privateKey;
    private static Certificate[] certificateChain;
    public Button btn_load;
    public Button btn_sign;
    public Label label_path;
    private File file_selected;

    protected static void readPrivateKeyFromPKCS12(File pkcs12File, String pkcs12Password)
            throws KeyStoreException {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("pkcs12");
            ks.load(new FileInputStream(pkcs12File), pkcs12Password.toCharArray());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("An unknown error accoured while reading the PKCS#12 file:");
            e.printStackTrace();
            System.exit(-1);
        } catch (CertificateException e) {
            System.err.println("An unknown error accoured while reading the PKCS#12 file:");
            e.printStackTrace();
            System.exit(-1);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to open the PKCS#12 keystore file \"" + pkcs12File.getName() + "\":");
            System.err.println("The file does not exists or missing read permission.");
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("An unknown error accoured while reading the PKCS#12 file:");
            e.printStackTrace();
            System.exit(-1);
        }
        String alias = "";
        try {
            alias = (String) ks.aliases().nextElement();
            privateKey = (PrivateKey) ks.getKey(alias, pkcs12Password.toCharArray());
        } catch (NoSuchElementException e) {
            System.err.println("An unknown error accoured while retrieving the private key:");
            System.err.println("The selected PKCS#12 file does not contain any private keys.");
            e.printStackTrace();
            System.exit(-1);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("An unknown error accoured while retrieving the private key:");
            e.printStackTrace();
            System.exit(-1);
        } catch (UnrecoverableKeyException e) {
            System.err.println("An unknown error accoured while retrieving the private key:");
            e.printStackTrace();
            System.exit(-1);
        }
        certificateChain = ks.getCertificateChain(alias);
    }

    @FXML
    public void initialize() {
        btn_load.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PDF", "pdf"));
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                label_path.setText(selectedFile.getAbsolutePath());
                file_selected = selectedFile;
            } else {
                label_path.setText("");
                file_selected = null;
            }
        });

        btn_sign.setOnAction(event -> {
            if (file_selected != null) {
                try {
                    signDocument(file_selected);
                } catch (Exception e) {
                    System.out.println("Error signing document: " + e.getMessage());
                }
            }
        });
    }

    private void signDocument(File fileToSign) throws Exception {

        //ClassLoader classLoader = getClass().getClassLoader();
        //File file = new File(classLoader.getResource("ConfigAux.cfg").getFile());
//        readPrivateKeyFromPKCS12(file, "jPdfSign");
        //File file = new File(classLoader.getResource("test2.p12").getFile());
        //readPrivateKeyFromPKCS12(file, "test"); En el main obtniene privatekey y certificado
        try {
			readPrivateKey();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
		}
        PdfReader reader = new PdfReader(fileToSign.getAbsolutePath());
        FileOutputStream fout = new FileOutputStream("Prueba-firmada.pdf");

        PdfStamper stamper = PdfStamper.createSignature(reader, fout, '\0');
        PdfSignatureAppearance sap = stamper.getSignatureAppearance();
        sap.setCrypto(privateKey, certificateChain, null, PdfSignatureAppearance.WINCER_SIGNED);
        sap.setReason("Soy tu padre");
        sap.setLocation("Uruguay");
        sap.setContact("Mi numero de contacto");
        sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
        stamper.close();
    }
    
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
		SunPKCS11 result = new sun.security.pkcs11.SunPKCS11("C:\\Users\\alfonso\\git\\seguridad\\src\\main\\resources\\ConfigAux.cfg");
		//SunPKCS11 result = new sun.security.pkcs11.SunPKCS11(ConfigFile);
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
    public static void readPrivateKey() throws Exception {
		// Test for the DataKey 330 Smartcard 
		// (dskck201.dll is installed with CIP Utilities from DataKey)
		SwingPasswordCallbackHandler swing = new SwingPasswordCallbackHandler();
		
		KeyStore ks = loadKeystore(swing);
		
		for(Enumeration<String> aliases = ks.aliases(); aliases.hasMoreElements(); ) {
			String alias = aliases.nextElement();
			System.out.println(alias);
			
			// print certifcate
			Certificate cert = ks.getCertificate(alias);
			certificateChain = ks.getCertificateChain(alias);
			if (cert != null) {
				System.out.print(" Certificate found. type="+cert.getType());
				if (cert instanceof X509Certificate) {
					X509Certificate x509 = (X509Certificate)cert;
					System.out.print(" SubjectDN="+x509.getSubjectDN());
					
				}
				System.out.println();
			}
			
			// private key is accessed without password 
			privateKey = (PrivateKey)ks.getKey(alias, null);
			if (privateKey != null) {
				System.out.println(" Private key found. algorithm="+privateKey.getAlgorithm());
			}
			
			System.out.println();
		}
	}


}
