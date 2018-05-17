package conta;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.security.cert.CertificateException;

import org.apache.commons.ssl.PKCS8Key;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.cfdi.exceptions.InconsistentDataException;

//import sun.security.pkcs.PKCS8Key;


/**
 * Libreria con la criptografia necesario para la generacion de facturas electronicas, asi como la manipulacion del Certificado de Sello Digital
 *
 * @author Alberto Sandoval Sotelo 20/Agosto/2010
 * 
 * @version	2.0 20/08/2013 <BR>
 * 		-Genera archivo PFX para la cancelacion de CFDI's con libreria Bouncy Castle
 */
public class LbsCrypt {
	PKCS8Key pkcs8 = null;
   private PrivateKey private_key;
   private PublicKey public_key;
   private X509Certificate cert;
   private String certNo="";
   byte[] certificado;
   byte[] key;
   public static final String PFX_PASS="q_q48df";
   private String tipo;
   private String rfc;
   private String curp;
   private String nombre;
   private String pass;

   /**
    * Constructor principal. Crea un objeto LbsCrypt, a partir de un .Key, un .Cer y una contraseña, correspondientes al Certificado de Sello Digital.
    *  
    * 
    * @param isKey Inputsretam al archivo .Key del Certificado de Sello Digital
    * @param Pass Contraseña del .key
    * @param isCer Inputstream al archivo .cer del Certificado de Sello Digital
    * 
    * @throws GeneralSecurityException Al no poder des-encriptar el archivo .key. Puede ser debido a que la contraseña sea invalida o el archivo en si se invalido
    * @throws IOException Al no encontrar alguno de los archivos o no poder acceder a el.
    * @throws CertificateException Error al procesar el Certificado. Probablemente por errores en el archivo.
 * @throws InconsistentDataException 
    */
   public LbsCrypt(InputStream isKey, String Pass, InputStream isCer) throws GeneralSecurityException, IOException, CertificateException, InconsistentDataException{
	   pass=Pass;
	   key  = InputStreamToByteArray(isKey);
		try{
			pkcs8 = new PKCS8Key(new ByteArrayInputStream(key), Pass.toCharArray());
    	}catch(Exception e){
//    		e.printStackTrace();
    		throw new InconsistentDataException("Error al validar la contraseña con la llave privada, verificar contrase\u00F1a y/o archivo.");
    	}

        byte[] decrypted = pkcs8.getDecryptedBytes();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec( decrypted );

        PrivateKey pk = null;
        if (pkcs8.isDSA()){
          pk = KeyFactory.getInstance( "DSA" ).generatePrivate( spec );
        }
        else if (pkcs8.isRSA()){
          pk = KeyFactory.getInstance( "RSA" ).generatePrivate( spec );
        }

        private_key=pk;
        certificado  = InputStreamToByteArray(isCer);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        cert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(certificado));
        public_key=cert.getPublicKey();
        for (int x=15;x<35;x++)
            certNo+=(char)certificado[x];
        boolean[] keyUsage = cert.getKeyUsage();
        if (keyUsage[3] || keyUsage[4])
        	tipo="FIEL";
        else
        	tipo="CSD";
        
        //Datos de cliente
        String[] principalArray = cert.getSubjectX500Principal().toString().split(",");
        for (String s : principalArray)
        	if (s.trim().startsWith("OID.2.5.4.45")){
        		rfc = s.split("=")[1].trim();
        	}else if (s.trim().startsWith("SERIALNUMBER")){
        		curp = s.split("=")[1].trim();
        	}else if (s.trim().startsWith("CN")){
        		nombre = s.split("=")[1].trim();
        	}
        
//        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
//        System.out.println("Cert. No: "+certNo);
//        System.out.println("Tipo: "+tipo);
//        System.out.println("RFC: "+rfc);
//        System.out.println("CURP: "+curp);
//        System.out.println("Nombre: "+nombre);
//        System.out.println("Desde: "+cert.getNotBefore());
//        System.out.println("Hasta: "+cert.getNotAfter());
//        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
        
   }
//   
//   public LbsCrypt(InputStream isCer) throws GeneralSecurityException, IOException, CertificateException, InconsistentDataException{
//	   this(Base64.toBase64String(InputStreamToByteArray(isCer)));
//   }
   
   public LbsCrypt(String strCert) throws java.security.cert.CertificateException{
        certificado = Base64.decode(strCert);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        cert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(certificado));
        public_key=cert.getPublicKey();
        for (int x=15;x<35;x++)
            certNo+=(char)certificado[x];
        boolean[] keyUsage = cert.getKeyUsage();
        if (keyUsage[3] || keyUsage[4])
        	tipo="FIEL";
        else
        	tipo="CSD";
        
        //Datos de cliente
        String[] principalArray = cert.getSubjectX500Principal().toString().split(",");
        for (String s : principalArray)
        	if (s.trim().startsWith("OID.2.5.4.45")){
        		rfc = s.split("=")[1].trim();
        	}else if (s.trim().startsWith("SERIALNUMBER")){
        		curp = s.split("=")[1].trim();
        	}else if (s.trim().startsWith("CN")){
        		nombre = s.split("=")[1].trim();
        	}
   }
   
   
   /**
    * Construye archvo PFX en base al certificado previamente cargado
    * 
    * @return archivo pfx como byte array
    * 
    * @throws NoSuchAlgorithmException
    * @throws java.security.cert.CertificateException
    * @throws IOException
    * @throws KeyStoreException
    * @throws NoSuchProviderException
    */
   public byte[] getPfx() throws NoSuchAlgorithmException, java.security.cert.CertificateException, IOException, KeyStoreException, NoSuchProviderException{
	   Security.addProvider(new BouncyCastleProvider());
	   KeyStore keyS = KeyStore.getInstance("PKCS12","BC");
       keyS.load(null,null);
//       keyS.setCertificateEntry("cert", cert);
       keyS.setKeyEntry("PFX", private_key, PFX_PASS.toCharArray(), new X509Certificate[]{cert});
//       FileOutputStream out = new FileOutputStream("pfx"+".pfx");
       ByteArrayOutputStream buffer = new ByteArrayOutputStream();
       
       keyS.store(buffer, PFX_PASS.toCharArray());
       return buffer.toByteArray();
   }
   
   public LbsCrypt(InputStream isPfx, String Pass) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, java.security.cert.CertificateException, IOException, UnrecoverableKeyException{
	   pass=Pass;
	   Security.addProvider(new BouncyCastleProvider());
	   KeyStore keyS = KeyStore.getInstance("PKCS12","BC");
       keyS.load(isPfx, Pass.toCharArray());

       private_key=(PrivateKey) keyS.getKey("PFX", Pass.toCharArray());
       cert = (X509Certificate) keyS.getCertificateChain("PFX")[0];
       public_key=cert.getPublicKey();
       
       certificado = cert.getEncoded();
   }
   
   
   @SuppressWarnings("deprecation")
   public byte[] getPemCert(){
	   ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
	   try{
		   OutputStreamWriter oswriter=new OutputStreamWriter(byteArrayOutputStream);
		   PEMWriter writer=new PEMWriter(oswriter);
		   writer.writeObject(cert);
		   writer.close();
	   }catch(Exception e){
		   return null;
	   }
	   return byteArrayOutputStream.toByteArray();
   }
   
   //Re-encripta la llave privada (Para cuando viene de un PFX)
   public byte[] getKeyBytes(String passphrase){
	   Security.addProvider(new BouncyCastleProvider());
	   StringWriter str;
	   try{
		   JceOpenSSLPKCS8EncryptorBuilder encryptorBuilder = new JceOpenSSLPKCS8EncryptorBuilder(PKCS8Generator.DES3_CBC);
		   encryptorBuilder.setPasssword(passphrase.toCharArray());
		   OutputEncryptor oe = encryptorBuilder.build();
		   JcaPKCS8Generator gen = new JcaPKCS8Generator(private_key,oe);
		   return gen.generate().getContent();
	   }catch(Exception e){
		   e.printStackTrace();
		   return null;
	   }
   }

   
   @SuppressWarnings("deprecation")
   public byte[] getPemKey(String passphrase){
	   Security.addProvider(new BouncyCastleProvider());
	   StringWriter str;
	   try{
		   JceOpenSSLPKCS8EncryptorBuilder encryptorBuilder = new JceOpenSSLPKCS8EncryptorBuilder(PKCS8Generator.DES3_CBC);
		   encryptorBuilder.setPasssword(passphrase.toCharArray());
		   OutputEncryptor oe = encryptorBuilder.build();
		   JcaPKCS8Generator gen = new JcaPKCS8Generator(private_key,oe);
		   PemObject obj = gen.generate();
		   str = new StringWriter();
		   PEMWriter pemWrt = new PEMWriter(str);
		   pemWrt.writeObject(obj);
		   pemWrt.close();
		   str.close();
		   return str.toString().getBytes();
	   }catch(Exception e){
		   e.printStackTrace();
		   return null;
	   }
   }


/**
 * Genera un par de llaves sin valor fiscal.
 * 
 * @param modulus
 * @param publicExponent
 * @param privateExponent
 */
   public LbsCrypt(BigInteger modulus, BigInteger publicExponent, BigInteger privateExponent){
        try {
            RSAPublicKeySpec public_ks = new RSAPublicKeySpec(modulus, publicExponent);
            RSAPrivateKeySpec private_ks = new RSAPrivateKeySpec(modulus, privateExponent);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            //KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    public_key=kf.generatePublic(public_ks);
                    private_key=kf.generatePrivate(private_ks);

        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(LbsCrypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LbsCrypt.class.getName()).log(Level.SEVERE, null, ex);
        }
   }

	/**
	 * Convierte un archivo del sistema a Base64
	 * 
	 * @param file
	 * @return
	 */
     public static String fileToB64 (String file) {
        File f = new File(file);
        try {
            FileInputStream fin = new FileInputStream(f);
            byte fileContent[] = new byte[(int)f.length()];
            fin.read(fileContent);
            fin.close();
            return toB64(fileContent);
        } catch (IOException ex) {
            System.out.println("Error al cargar archivo: " + file);
            return "";
        }
    }
     
    
     /**
      * Devuelve el Certificado del CSD, en formato Base 64
      * 
      * 
      * @return
      */
     public String getB64Certificado(){
    	 return toB64(certificado);
     }


	/**
	 * Crea una llave publica, sin valor fiscal. Para pruebas.
	 * 
	 **/
	   public LbsCrypt(BigInteger modulus, BigInteger publicExponent){
	        try {
	            RSAPublicKeySpec public_ks = new RSAPublicKeySpec(modulus, publicExponent);
	            KeyFactory kf = KeyFactory.getInstance("RSA");
	            //KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	                    public_key=kf.generatePublic(public_ks);
	                    private_key=null;
	        } catch (InvalidKeySpecException ex) {
	            Logger.getLogger(LbsCrypt.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (NoSuchAlgorithmException ex) {
	            Logger.getLogger(LbsCrypt.class.getName()).log(Level.SEVERE, null, ex);
	        }
	   }


	   /**
	    * Encripta algo con estandar RSA no usar para sellado de CFD's
	    * 
	    * 
	    * @param data
	    * @return
	    */
    public byte[] rsaEncrypt(byte[] data){
        byte[] cipherData = null;

            Cipher cipher;
			try {
				cipher = Cipher.getInstance("RSA");
	            cipher.init(Cipher.ENCRYPT_MODE, private_key);
	            cipherData = cipher.doFinal(data);
			}catch (Exception e) {
				e.printStackTrace();
			}
        return cipherData;
    }

    
    
    public byte[] rsaDecrypt(byte[] data){
        byte[] cipherData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, public_key);
            cipherData = cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        return cipherData;
    }

    /**
     * Sella con: SHA1withRSA para sellar cfd usa sellarCfd
     * 
     * 
     * @param data Cadena original a sellar.
     * 
     * @return El sello en arreglo de bytes
     */
    private byte[] sign32(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException{
        Signature instance = Signature.getInstance("SHA1withRSA");
        instance.initSign(private_key);
        instance.update(data);
        return instance.sign();
    }
    /**
     * Sella con: SHA1withRSA para sellar cfd usa sellarCfd
     * 
     * 
     * @param data Cadena original a sellar.
     * 
     * @return El sello en arreglo de bytes
     */
    private byte[] sign33(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException{
        Signature instance = Signature.getInstance("SHA256withRSA");
        instance.initSign(private_key);
        instance.update(data);
        return instance.sign();
    }
    
    
    public boolean verifySign(byte[] data,String signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException{
    	Signature instance = Signature.getInstance("SHA1withRSA");        
        instance.initVerify(public_key);
        instance.update(data);
        return instance.verify(Base64.decode(signature));
    }
    

    /**
     * Digestion MD5
     * 
     * 
     * @param data Datos que se desean digerir
     * @return Digestion
     * 
     * @throws NoSuchAlgorithmException
     */
    public static String ToMD5 (byte[] data) throws NoSuchAlgorithmException{
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(data);
        return toB64(md.digest());
    }

    /**
     * Regresa la digestion MDS de un archivo.
     * 
     * @param file Ruta al archivo que se desea su digestion.
     * 
     * @return Digestion en formato MD5
     */
    public static String getMD5 (String file) {
        File f = new File(file);
        try {
            FileInputStream fin = new FileInputStream(f);
            byte fileContent[] = new byte[(int)f.length()];
            fin.read(fileContent);
            fin.close();
            return ToMD5(fileContent);
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LbsCrypt.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (IOException ex) {
            System.out.println("Error al cargar reporte archivo" + file);
            return "";
        }
    }


    /**
     * Convierte un arreglo de Bytes a Base64
     * 
     * @param data Datos a convertir.
     * @return Resultado en Base64
     */
   public static String toB64(byte[] data){
      return new String(Base64.encode(data)).replaceAll("\\n", "");
   }


   /**
    * Procesa el sellado de data (la cadena original de un CFD). Aplica SHA1 despues RSA y finalmente el resultado lo convierte a Base64.
    * 
    * @param data Cadena Original
    * @return Sello en Base64
    */
   public String sellarCfdi32(byte[] data){
      try{
        return toB64(sign32(data));
      }catch (Exception e) {
			throw new RuntimeException("Error al sellar documento.");
		}
   }
   /**
    * Procesa el sellado de data (la cadena original de un CFD). Aplica SHA1 despues RSA y finalmente el resultado lo convierte a Base64.
    * 
    * @param data Cadena Original
    * @return Sello en Base64
    */
   public String sellarCfdi33(byte[] data){
      try{
        return toB64(sign33(data));
      }catch (Exception e) {
			throw new RuntimeException("Error al sellar documento.");
		}
   }


   /**
    * Metodo de pruebas para generar llaves
    * 
    */
  public void generateKeys(){
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();
            public_key = kp.getPublic();
            private_key = kp.getPrivate();
            System.out.println(public_key);
            System.out.println(private_key);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LbsCrypt.class.getName()).log(Level.SEVERE, null, ex);
        }
  }


  /**
   * Verifica la vigencia del Certificado de Sello Digital
   * 
   * @return Si es o no valido el CSD.
   */
  public boolean validarCertificado(){
        if (Calendar.getInstance().getTime().after(cert.getNotAfter()))
            return false;
        else if(Calendar.getInstance().getTime().before(cert.getNotBefore()))
            return false;
        else
            return true;
  }
  
  public boolean validarCertificado(Date fecha){
	  Calendar cal = Calendar.getInstance();
	  cal.setTime(fecha);
      if (cal.getTime().after(cert.getNotAfter()))
          return false;
      else if(cal.getTime().before(cert.getNotBefore()))
          return false;
      else
          return true;
}

  /**
   * Regresa el numero de certificado del CSD;
   * 
   * @return Numero de certificado.
   */
  public String getCertNo(){
    return certNo;
  }
  
  
	
  public String getTipo() {
	return tipo;
}


public String getRfc() {
	return rfc;
}


public String getCurp() {
	return curp;
}


public String getNombre() {
	return nombre;
}

public Date getFrom(){
	return cert.getNotBefore();
} 
public Date getUntil(){
	return cert.getNotAfter();
}
public Integer getDaysLeft(){
	return Days.daysBetween(new DateTime(),new DateTime(cert.getNotAfter())).getDays()-1;
} 

/**
   *Utileria para convertir un InputStream en un ByteArray.
   *
   * @param is InputStream
   * @return Array
   * 
   * @throws IOException
   */
	public static byte[] InputStreamToByteArray(InputStream is) throws IOException{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();
	}
	
	/**
	 * Funcion para probar la pareja de llaves, encripta la cadena Hola Mundo! con la llave privada y 
	 * desencriptar con la publica para finalmente comprar el resultado con la cadena original de Hola Mundo!
	 * @return
	 */
	public boolean testPair(){
        String str = "Hola Mundo!";
        byte[] result = rsaDecrypt(rsaEncrypt(str.getBytes()));
        
        if (result==null || !(new String(result).equals(str)))
        	return false;
        else
        	return true;
	}
	
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	
	
	  public static String xor_encrypt(String message, String key){
		    try {
		      if (message==null || key==null ) return null;

		      char[] keys=key.toCharArray();
		      char[] mesg=message.toCharArray();

		      int ml=mesg.length;
		      int kl=keys.length;
		      char[] newmsg=new char[ml];

		      for (int i=0; i<ml; i++){
		        newmsg[i]=(char)(mesg[i]^keys[i%kl]);
		      }
		      mesg=null; 
		      keys=null;
		      String temp = new String(newmsg);
		      return new String(Base64.encode(temp.getBytes()));
		    }
		    catch ( Exception e ) {
		      return null;
		    }  
		  }


		  public static String xor_decrypt(String message, String key){
		    try {
		      if (message==null || key==null ) return null;
		      char[] keys=key.toCharArray();
		      message = new String(Base64.decode(message));
		      char[] mesg=message.toCharArray();

		      int ml=mesg.length;
		      int kl=keys.length;
		      char[] newmsg=new char[ml];

		      for (int i=0; i<ml; i++){
		        newmsg[i]=(char)(mesg[i]^keys[i%kl]);
		      }
		      mesg=null; keys=null;
		      return new String(newmsg);
		    }
		    catch ( Exception e ) {
		      return null;
		    }  
		  }

		public byte[] getKeyFile() {
			return key;
		}

		public byte[] getCerFile() {
			return certificado;
		}
		public String getPass() {
			return pass;
		}
}