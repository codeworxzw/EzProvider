package mn.ezpay.msg;

import mn.ezpay.payment.bank;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.json.JSONObject;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class msg implements msgImpl {
    public ISOMsg buildIsoMsg;

    public msg() {

    }

    public byte[] readBinary(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        return Files.readAllBytes(path);
    }

    public void logISOMsg(ISOMsg msg) {
        System.out.println("----ISO MESSAGE-----");
        try {
            System.out.println("  MTI : " + msg.getMTI());
            for (int i = 1; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    System.out.println("    Field-" + i + " : " + msg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("--------------------");
        }
    }

    @Override
    public byte[] purchase(JSONObject json) {
        return new byte[0];
    }

    @Override
    public byte[] purchase_reversal(JSONObject json) {
        return new byte[0];
    }

    @Override
    public byte[] purchase_void(JSONObject json) {
        return new byte[0];
    }

    @Override
    public byte[] settlement(JSONObject json) {
        return new byte[0];
    }

    @Override
    public byte[] batch_upload(JSONObject json) {
        return new byte[0];
    }

    @Override
    public JSONObject response(SSLSocket connection, JSONObject json, bank bank) throws Exception {
        return null;
    }
}
