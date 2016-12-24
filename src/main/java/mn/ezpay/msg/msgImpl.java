package mn.ezpay.msg;

import mn.ezpay.payment.bank;
import org.json.JSONObject;

import javax.net.ssl.SSLSocket;

public interface msgImpl {
    public byte[] purchase(JSONObject json);

    public byte[] purchase_reversal(JSONObject json);

    public byte[] purchase_void(JSONObject json);

    public byte[] settlement(JSONObject json);

    public byte[] batch_upload(JSONObject json);

    public JSONObject response(SSLSocket connection, JSONObject json, bank bank) throws Exception;
}
