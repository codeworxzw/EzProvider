package mn.ezpay.msg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class msgGW {
    public static String PIN_CODE = "Tanii EzPay code : @ Enehuu message haagaad EzPay app-ruu orj code hesegt oruulna uu !";

    public static String buildMsg(int mode, String[] values) {
        String msg = "";
        if (mode == 1) {
            msg = PIN_CODE;
            for (int i = 0; i < values.length; i++)
                msg = msg.replace("@", values[i]);
        }

        msg = msg.replaceAll(" ", "%20");

        return msg;
    }

    public static String send(String phone, String msg) {
        String ret = "";
        try {
            URL oracle = new URL("http://27.123.214.168/smsmt/mt?servicename=132222&username=132222&from=132222&to="+phone+"&msg="+msg);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine = "";
            while ((inputLine = in.readLine()) != null)
                ret += inputLine;
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ret;
    }
}
