/**
 * 
 */
package encryption;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



/**
 * @author rajiv
 *
 */
public class EncryptData {
/**
 * 
 * @param data The string to be encrypted.
 * @param filePath The path where the encrypted data will be stored.
 * @param key The key that will be used for the encryption. 
 * @param initializationVector The initialization vector that will be used.
 * @return boolean Returns whether the encryption was successful.
 */
  public static boolean encrypt(String data, String filePath, String key, String initializationVector) {
	  

	  

    IvParameterSpec initializationVectorSpec = new IvParameterSpec(initializationVector.getBytes(
        StandardCharsets.UTF_8));
    SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      System.out.println("There was a problem creating the Cipher.");
      return false;
    }
    

    try {
      //cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initializationVectorSpec);
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initializationVectorSpec);
    } catch (InvalidAlgorithmParameterException e) {
      System.out.println("There was a problem initializing the Cipher.");
      return false;
    } catch (InvalidKeyException e) {
      System.out.println("The provided key is invalid. It must consist of 16 characters.");
      return false;
    }

    byte[] encrypted = null;
    try {
      encrypted = cipher.doFinal(data.getBytes());
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      System.out.println("There was a problem completing the encryption.");
      return false;
    }

    Path file = Paths.get(filePath);

    try {
      Files.write(file, encrypted);
    } catch (IOException e) {
      System.out.println("There was a problem writing to file.");
      return false;
    }


    return true;        
  }

  /**
   * 
   * @param key The key required to decrypt the file.
   * @param initializationVector The initialization vector that is required to decrypt the file.
   * @param filePath The file to decrypt.
   * @return String Returns the decrypted text.
   */
  public static String decrypt(String key, String initializationVector, String filePath) {

    Path file = Paths.get(filePath);
    byte[] encrypted;
    try {
      encrypted = Files.readAllBytes(file);
    } catch (IOException e) {
      return "";
    }
    IvParameterSpec initializationVectorSpec = new IvParameterSpec(initializationVector.getBytes(
        StandardCharsets.UTF_8));
    SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      System.out.println("There was a problem getting an instance of the cipher.");
      return "";
    }
    try {
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, initializationVectorSpec);
    } catch (InvalidAlgorithmParameterException e) {
      System.out.println("There was a problem initializing the cipher.");
      return "";
    } catch (InvalidKeyException e) {
      System.out.println("The provided key is invalid.");
      return "";
    }

    byte[] originalText;
    try {
      originalText = cipher.doFinal(encrypted);
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      System.out.println("There was a problem decrypting the file.");
      return "";
    }

    return new String(originalText);


    }
}
