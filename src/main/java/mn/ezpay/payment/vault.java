package mn.ezpay.payment;

import mn.ezpay.security.rsaencryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.getInstance;

public class vault {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String amount(double amount) {
        amount = round(amount, 2);
        String am = amount + "";
        if (am.endsWith(".0")) am = am + "0";
        am = am.replace(".", "");
        while (am.length() < 12) {
            am = "0" + am;
        }

        return am;
    }


    public static String amount(double amount, int p) {
        amount = round(amount, 2);
        String am = amount + "";
        if (am.endsWith(".0")) am = am + "0";
        am = am.replace(".", "");
        while (am.length() < p) {
            am = "0" + am;
        }

        return am;
    }

    public static String settleamount(int q, double amount, int p, double ramount) {
        amount = round(amount, 2);
        String am = amount + "";
        if (am.endsWith(".0")) am = am + "0";
        am = am.replace(".", "");
        while (am.length() < 12) {
            am = "0" + am;
        }

        String qm = q + "";
        while (qm.length() < 3) {
            qm = "0" + qm;
        }

        ramount = round(ramount, 2);
        String ram = ramount + "";
        if (ram.endsWith(".0")) ram = ram + "0";
        ram = ram.replace(".", "");
        while (ram.length() < 12) {
            ram = "0" + ram;
        }

        String pm = p + "";
        while (pm.length() < 3) {
            pm = "0" + pm;
        }


        String bm = qm+am;//+pm+ram

        while (bm.length() < 90) {
            bm = bm + "0";
        }

        return bm;
    }

    public static byte[] hexStringToByteArray(String amount) {
        int len = amount.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(amount.charAt(i), 16) << 4)
                    + Character.digit(amount.charAt(i + 1), 16));
        }
        return data;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x:", b & 0xff));
        return sb.toString();
    }

    public static String getNumber(char[] data, int offset, int len) {
        byte[] srn = new byte[len];
        for (int i = 0; i < len; i++) {
            srn[i] = (byte) data[i + offset];
        }
        String[] hex = byteArrayToHex(srn).split(":");
        String value = "";
        for (int i = 0; i < hex.length; i++) {
            value += (Integer.parseInt(hex[i]) - 30) + "";
        }

        return value;
    }

    public static String getString(char[] data, int offset, int len) {
        byte[] srn = new byte[len];
        for (int i = 0; i < len; i++) {
            srn[i] = (byte) data[i + offset];
        }
        String[] hex = byteArrayToHex(srn).split(":");
        String value = "";
        for (int i = 0; i < hex.length; i++) {
            value += hex[i] + "";
        }

        return value;
    }

    public static byte[] encrypt(byte[] data, boolean flag) throws Exception {
        if (flag) {
            PublicKey publicKey = rsaencryption.generatePublicKey();
            data = rsaencryption.encrypt(data, publicKey);
        }
        return data;
    }

    public static String decryptDesc(byte[] message) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest("7!,VV=tT%[m?N}$m"
                .getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, key, iv);

        final byte[] plainText = decipher.doFinal(message);

        return new String(plainText, "UTF-8");
    }

    public static String encryptedData(String data, String fileName) {
        try {
            String parse = data.toString();
            return rsaencryption.encrypt(parse, getPublicKey(fileName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return data;
    }

    public static RSAPublicKey getPublicKey(String fileName) throws Exception {
        InputStream fis = new FileInputStream(fileName);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) fis.available()];
        dis.readFully(keyBytes);
        dis.close();

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey(InputStream fis) throws Exception {
//        InputStream fis = new FileInputStream(filename);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[fis.available()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static PrivateKey getPrivateKey(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[fis.available()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static String decrypt(byte[] buffer, String filename) {
        try {
            Cipher rsa;
            rsa = getInstance("RSA");
            rsa.init(DECRYPT_MODE, getPrivateKey(filename));
            byte[] utf8 = rsa.doFinal(buffer);
            return new String(utf8, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatCard(String value) {
        String str = "";
        int start = 4, offset = 14;
        if (value.indexOf(" ") == -1) offset = 11;
        for (int i = 0; i < value.length(); i++) {
            if (i >= start && i <= offset && value.charAt(i) != ' ')
                str += "*";
            else
                str += value.charAt(i);
        }

        return str;
    }

    public static String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled;
    }

    public static String sha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            return bytesToHex(md.digest());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String priceWithoutDecimal1(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.## MNT");
        return formatter.format(price);
    }

    public static String priceWithoutDecimal(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.## ₮");
        return formatter.format(price);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    public static byte[] fromHexString(final String encoded) {
        if ((encoded.length() % 2) != 0)
            throw new IllegalArgumentException("Input string must contain an even number of characters");

        final byte result[] = new byte[encoded.length()/2];
        final char enc[] = encoded.toCharArray();
        for (int i = 0; i < enc.length; i += 2) {
            StringBuilder curr = new StringBuilder(2);
            curr.append(enc[i]).append(enc[i + 1]);
            result[i/2] = (byte) Integer.parseInt(curr.toString(), 16);
        }
        return result;
    }

    public static String generateToken() {
        long randValue = (long) (Math.random() * 5999999);
        String uid = UUID.randomUUID().toString();
        String token = randValue + uid + shuffleString("!@#$%^&*()?><\":}{");
        token = sha256(token);
        return token;
    }

    public static void main(String[] arg) {
        try {
            FileOutputStream fos = new FileOutputStream("c:\\settlement.dat");
            fos.write((fromHexString("0092600003000005002020010000c000129200000000040003313331333337303730303030303030303030343337353200063030303030310090303032303030303030303030383030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030")));
            fos.close();
        } catch (Exception ex){

        }
    }
}
