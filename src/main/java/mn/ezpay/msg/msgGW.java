package mn.ezpay.msg;

import mn.ezpay.security.base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

    public static String getJson(String serverUrl, String username, String pass){

        StringBuilder sb = new StringBuilder();

        String http = serverUrl;

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(http);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(50000);
            urlConnection.setReadTimeout(50000);
            urlConnection.setRequestProperty("Content-Type", "application/plain");
            urlConnection.connect();
            //You Can also Create JSONObject here
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            String jsonobject = "appId=ZvUU9LUEVzaWRmR0JORk&appSecret=N2FtWnZVVTlMVUVWemFX&loginname="+username+"&password="+pass;
            out.write(jsonobject);// here i sent the parameter
            out.close();
            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                System.out.println(sb);
                return base64.encode(sb.toString().getBytes("UTF-8"));
            } else {
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }
}
