package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

public class Crypto {

    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("No command given.");
            return;
        }

        try{
            switch (args[0]){
                case "checksha":
                    if(args.length - 1 == 1){
                        checksha(args[1]);
                    }else{
                        System.out.println("Wrong amount of command arguments given, " +
                                "expected 1, got: " + (args.length-1));
                    }
                    break;

                case "encrypt":
                    if(args.length - 1 == 2){
                        crypt(args[1], args[2], true);
                    }else{
                        System.out.println("Wrong amount of command arguments given, " +
                                "expected 2, got: " + (args.length-1));
                    }
                    break;

                case "decrypt":
                    if(args.length - 1 == 2){
                        crypt(args[1], args[2], false);
                    }else{
                        System.out.println("Wrong amount of command arguments given, " +
                                "expected 2, got: " + (args.length-1));
                    }
                    break;

                default:
                    System.out.println("Unknown command given.");
                    break;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Error reading file.");
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException |
                 NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("Error decrypting/encrypting given file with the given key and initialization vector.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Illegal arguments given.");
        }

    }

    private static void checksha(String fileName) throws IOException {
        try(BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(Path.of(fileName)))) {

            System.out.println("Please provide expected sha-256 digest for " + fileName + ":");
            System.out.print("> ");

            Scanner sc = new Scanner(System.in);
            String expectedDigest = sc.nextLine().trim();


            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            byte[] inputData = inputStream.readNBytes(16);
            while(inputData.length > 0){
                messageDigest.update(inputData);

                inputData = inputStream.readNBytes(16);
            }

            String digest = Util.byteToHex(messageDigest.digest());

            if(digest.equals(expectedDigest)){
                System.out.println("Digesting completed. Digest of " + fileName + " matches expected digest.");
            } else {
                System.out.println("Digesting completed. Digest of " + fileName + " does not match the expected digest." +
                        " Digest was: " + digest);
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static void crypt(String inputFileName, String outputFileName, boolean encrypt) throws IOException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        try(BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(Path.of(inputFileName)));
                BufferedOutputStream outputStream =new BufferedOutputStream(Files.newOutputStream(Path.of(outputFileName)))) {

            System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
            System.out.print("> ");

            Scanner sc = new Scanner(System.in);
            String keyText = sc.nextLine().trim();

            if(keyText.length() != 32){
                throw new IllegalArgumentException("The given password does not have 32 hex-digits.");
            }

            System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
            System.out.print("> ");

            String ivText = sc.nextLine().trim();

            if(ivText.length() != 32){
                throw new IllegalArgumentException("The given initialization vector does not have 32 hex-digits.");
            }

            SecretKeySpec keySpec = new SecretKeySpec(Util.hexToByte(keyText), "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hexToByte(ivText));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);

            byte[] inputData = inputStream.readNBytes(16);
            while(inputData.length > 0){
                outputStream.write(cipher.update(inputData));

                inputData = inputStream.readNBytes(16);
            }

            outputStream.write(cipher.doFinal());
        }

    }


}
