package com.ucu.seguridad.controllers;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
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

    private void signDocument(File fileToSign) throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidKeyException, IOException, SignatureException, DocumentException, KeyStoreException {

        ClassLoader classLoader = getClass().getClassLoader();
//        File file = new File(classLoader.getResource("test.p12").getFile());
//        readPrivateKeyFromPKCS12(file, "jPdfSign");
        File file = new File(classLoader.getResource("test2.p12").getFile());
        readPrivateKeyFromPKCS12(file, "test");

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


}
