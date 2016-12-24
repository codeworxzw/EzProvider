package mn.ezpay.payment;

import mn.ezpay.msg.golomtMsg;
import mn.ezpay.msg.msg;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.BufferedOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Properties;

public class make {

    public static SSLSocketFactory getSocketFactoryFromPEM(String pemPath) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SSLContext context = SSLContext.getInstance("TLSv1.2");

        PEMReader reader = new PEMReader(new FileReader(pemPath));
        X509Certificate cert = (X509Certificate) reader.readObject();

        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(null);
        keystore.setCertificateEntry("alias", cert);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keystore, null);
        KeyManager[] km = kmf.getKeyManagers();
        context.init(km, null, null);

        return context.getSocketFactory();
    }

    public static SSLSocket getSSLSocket(bank bank) throws Exception {
        SSLSocketFactory sslSocketFactory = getSocketFactoryFromPEM(bank.getUAT());
        SSLSocket sslsocket = (SSLSocket) sslSocketFactory.createSocket(bank.getUrl(), Integer.parseInt(bank.getPort()));

        sslsocket.startHandshake();
        return sslsocket;
    }

    public static bank getBank(String key, String mode) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream("properties.properties");
            prop.load(input);
            return new bank(prop.getProperty(key + ".url"),
                    prop.getProperty(key + ".port"),
                    mode,
                    prop.getProperty(key + ".UAT"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new bank();
    }

    public static JSONObject makePayment(type bank, String mode, JSONObject input) {
        switch (bank) {
            case golomt:
                return sendMsg(getBank("golomt", mode), new golomtMsg(), input);
        }

        return null;
    }

    public static JSONObject sendMsg(bank bank, msg msg, JSONObject json) {
        JSONObject res = new JSONObject();
        try {
            res.put("respondCode", "99");

            byte[] bytes = new byte[10];
            if ("purchase".equals(bank.getMode())) bytes = msg.purchase(json);
            else if ("reversal".equals(bank.getMode())) bytes = msg.purchase_reversal(json);
            else if ("void".equals(bank.getMode())) bytes = msg.purchase_void(json);
            else if ("settlement".equals(bank.getMode())) bytes = msg.settlement(json);
            else if ("batch".equals(bank.getMode())) bytes = msg.batch_upload(json);

            bytes = utils.encrypt(bytes, false);
            SSLSocket connection = getSSLSocket(bank);
            if (connection.isConnected()) {
                try {
                    BufferedOutputStream outStream = null;
                    System.out.println("-----Request sent to server---");
                    System.out.println("Original :" + new String(bytes));

                    outStream = new BufferedOutputStream(connection.getOutputStream());
                    outStream.write(bytes);
                    outStream.flush();

                    return msg.response(connection, json, bank);
                } catch (Exception ex) {
                    return res;
                }
            }
        } catch (Exception ex) {
            System.out.println("Payment SSL problem !");
            ex.printStackTrace();
            return res;
        }
        return res;
    }
}
