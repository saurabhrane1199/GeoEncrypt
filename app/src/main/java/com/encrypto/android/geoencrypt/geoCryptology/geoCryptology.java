package com.encrypto.android.geoencrypt.geoCryptology;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static javax.crypto.SecretKeyFactory.getInstance;

public class geoCryptology {

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void encryptFile(Uri inFilePath, Context context) throws BadPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException {

        InputStream inputStream = context.getContentResolver().openInputStream(inFilePath);
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFile = new File(folder, "trail3.des");
        FileOutputStream outFile = new FileOutputStream(myFile);

        String password = "javapapers";
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory
                .getInstance("PBEWithMD5AndDES");
        SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

        byte[] salt = new byte[8];
        Random random = new Random();
        random.nextBytes(salt);

        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);
        outFile.write(salt);

        //file encryption
        byte[] input = new byte[64];
        int bytesRead;

        try {
            assert inputStream != null;
            while ((bytesRead = inputStream.read(input)) != -1) {
                System.out.println("We are trying");
                System.out.println((char)bytesRead);
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outFile.write(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] output = cipher.doFinal();

        if (output != null)
            try {
                outFile.write(output);
            } catch (IOException e) {
                e.printStackTrace();
            }


        //delete original file
        //deleteFile(plainFilePath);

        try {
            inputStream.close();
            outFile.flush();
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        resetValues(cipher);

    }

    public static void decryptFile(Uri file,Context context) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        InputStream fis = context.getContentResolver().openInputStream(file);
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myFile = new File(folder, "plainfile_decrypted.pdf");
        FileOutputStream fos = new FileOutputStream(myFile);

        String password = "javapapers";
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory
                .getInstance("PBEWithMD5AndDES");
        SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

        byte[] salt = new byte[8];
        assert fis != null;
        fis.read(salt);

        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);

        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);

        byte[] in = new byte[64];
        int read;
        while ((read = fis.read(in)) != -1) {
            byte[] output = cipher.update(in, 0, read);
            if (output != null)
                fos.write(output);
        }

        byte[] output = cipher.doFinal();
        if (output != null)
            fos.write(output);

        fis.close();
        fos.flush();
        fos.close();
        resetValues(cipher);

    }



















/*    static void decryptFile(byte[] hashedKey,Uri file,String algo) throws IOException {

        File filePath = new File( );
        File decryptedFilePath = new File(Environment.getExternalStorageDirectory() + "/Decrypted Files/");
        File decryptedFile = new File(decryptedFilePath,filePath.getName());

        //create path if does not exist
        if(!decryptedFilePath.exists())
            decryptedFilePath.mkdir();

        FileInputStream fis = new FileInputStream(filePath.getPath());
        FileOutputStream fos = new FileOutputStream(decryptedFile);

        SecretKeySpec secret = new SecretKeySpec(hashedKey, algo);
        //test bc
        String proC = null;
        // file decryption
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algo+"/CBC/PKCS5Padding","SC");
            proC = cipher.getProvider().toString();
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        byte[] in = new byte[64];
        int read;
        while ((read = fis.read(in)) != -1) {
            byte[] output = cipher.update(in, 0, read);
            if (output != null)
                fos.write(output);
        }

        byte[] output = new byte[0];
        try {
            output = cipher.doFinal();
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        if (output != null)
            fos.write(output);

        //delete original file
        deleteFile(filePath);

        fis.close();
        fos.flush();
        fos.close();
        resetValues(cipher);
    }*/


    private static void deleteFile(File filePath){
        filePath.delete();
    }



    private static void resetValues(Cipher cipher)
    {
        cipher = null;
    }
}
