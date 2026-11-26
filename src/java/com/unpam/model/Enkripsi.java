package com.unpam.model;

import java.security.MessageDigest;

public class Enkripsi {

    /**
     * Menghasilkan hash MD5 dari nilai input.
     *
     * @param value Nilai yang akan di-hash.
     * @return String representasi hexadecimal dari hash MD5.
     * @throws Exception Jika terjadi kesalahan selama proses hashing.
     */
    public String hashMD5(String value) throws Exception {
       
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(value.getBytes());

        
        byte[] byteData = md.digest();

        
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteData) {
            
            String hex = Integer.toHexString(0xff & b);
            
            if (hex.length() == 1) {
                hexString.append('0');
            }
            
            hexString.append(hex);
        }
        
        return hexString.toString();
    }

    public String HashMD5(String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
