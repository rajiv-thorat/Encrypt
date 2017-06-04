/**
 * 
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import encryption.EncryptData;

/**
 * @author rajiv
 *
 */
public class Begin {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    if(args.length == 3 && args[0].equals("encrypt")){
      String weatherInfo = getData(args[1]);      
      if(EncryptData.encrypt(weatherInfo,args[2])){
        System.out.println("Encryption successful.");
      }
      
    } else if(args.length == 4 && args[0].equals("decrypt")){
      System.out.println(EncryptData.decrypt(args[1], args[2], args[3]));
    } else {
      System.out.println("There is a problem with the command you typed");
      System.out.println("To encrypt use : Begin encrypt URL FilePath Key InitializationVector");
      System.out.println("To decrypt use : Begin decrypt FilePath Key InitializationVector");
    }    

  }
  /**
   * 
   * @param URL 
   * @return String Returns the data that the site returned.
   */
  public static String getData(String URL){
    StringBuilder data = new StringBuilder();
    URL url;
    try {
      url = new URL(URL);
    } catch (MalformedURLException e) {    
      System.out.println("There is a problem with the URL you have provided.");      
      return "";
    }
    HttpURLConnection conn;
    try {
      conn = (HttpURLConnection) url.openConnection();
    } catch (IOException e) {
      System.out.println("Could not open a connection to the URL you have provided.");
      return "";
      
    }
        try {
      conn.setRequestMethod("GET");
    } catch (ProtocolException e) {
      System.out.println("There is a problem with the connection protocol to the URL you have provided.");
      return "";
    }
        BufferedReader rd;
    try {
      rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));    
      String line;        
      while ((line = rd.readLine()) != null) {
        data.append(line);
      }      
      rd.close();
    } catch (IOException e) {
      System.out.println("There is a problem reading from the connection to the URL you have provided.");
      return "";
    }
        
    return data.toString();
  }

}
