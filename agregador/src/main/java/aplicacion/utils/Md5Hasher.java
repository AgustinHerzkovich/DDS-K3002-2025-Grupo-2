package aplicacion.utils;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Md5Hasher {
    private static Md5Hasher instance;
    private MessageDigest hasher;
    private Md5Hasher(){
        try {
            this.hasher = MessageDigest.getInstance("MD5");
        }catch (Exception ignored){}
    }
    public static Md5Hasher getInstance(){
        if(instance == null)
            instance = new Md5Hasher();
        return instance;
    }
    public String hash(String cadena){
        byte[] digest = hasher.digest(cadena.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
