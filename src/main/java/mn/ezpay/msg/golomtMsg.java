package mn.ezpay.msg;

import mn.ezpay.payment.utils;
import mn.ezpay.payment.bank;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.json.JSONObject;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by User on 12/11/2016.
 */
public class golomtMsg extends msg {

    public golomtMsg() {
        super();
    }

    public byte[] purchase(JSONObject json) {
        try {
            InputStream fis = new FileInputStream(getClass().getClassLoader().getResource("golomt/request_basic.xml").getFile());
            GenericPackager packager = new GenericPackager(fis);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0200");
            byte[] header = {0x00, 0x53, 0x60, 0x00, 0x03, 0x00, 0x00};
            isoMsg.setHeader(header);
            isoMsg.set(3, "000000");
            isoMsg.set(4, utils.amount(json.getDouble("amount")));
            isoMsg.set(11, json.getString("traceNo"));
            isoMsg.set(22, "022");
            isoMsg.set(24, "003");
            isoMsg.set(25, "00");
            if (json.getString("card_id").startsWith("5163"))
                isoMsg.set(35, "5163815415811069=18012015415812405415");//4380531201242233=1902520278358390020
            else
                isoMsg.set(35, "3594961881442508=20021011859412300203");//"4380531201242233=1902520278358390020");//
            isoMsg.set(41, json.getString("terminalId"));
            isoMsg.set(42, json.getString("bankMerchantId"));
            isoMsg.set(62, "000001");

            buildIsoMsg = isoMsg;
            buildIsoMsg.recalcBitMap();
            logISOMsg(buildIsoMsg);
            byte[] send_PackedRequestData = buildIsoMsg.pack();
            byte[] data = readBinary(getClass().getClassLoader().getResource("golomt/data.dat").getFile().substring(1));
            byte[] data_after = new byte[85];

            for (int i = 0; i <= 34; i++) {
                data_after[i] = data[i];
            }

            for (int i = 35; i < 54; i++) {
                data_after[i] = send_PackedRequestData[i];
            }

            for (int i = 54; i < 62; i++) {
                data_after[i] = send_PackedRequestData[i + 1];
            }

            for (int i = 62; i < 77; i++) {
                data_after[i] = send_PackedRequestData[i + 3];
            }

            data_after[77] = 0;
            for (int i = 78; i < 85; i++) {
                data_after[i] = send_PackedRequestData[i + 2];
            }

            for (int i = 20; i <= 26; i++) { //amount talbariig zasaj baina
                data_after[i] = send_PackedRequestData[i + 1];
            }

            data_after[17] = 0;

            /*
            //new mode
            int[][] blocks = {{0, 35, -1},{35,54,0},{54,62,1},{62,77,3},{77,77,0},{78,85,2},{20,27,1},{17,17,0}};
            for (int i = 0; i < blocks.length; i++) {
                if (blocks[i][2] == -1)
                    data_after = setarray(blocks[i][0], blocks[i][1], 0, data, data_after);
                else
                if (blocks[i][0] == blocks[i][1])
                    data_after[blocks[i][0]] = (byte)blocks[i][2];
                else
                    data_after = setarray(blocks[i][0], blocks[i][1], blocks[i][2], send_PackedRequestData, data_after);
            }
            //end new mode
           */
            return data_after;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public byte[] purchase_reversal(JSONObject json) {
        try {

            InputStream fis = new FileInputStream(getClass().getClassLoader().getResource("golomt/request_reversal.xml").getFile());
            GenericPackager packager = new GenericPackager(fis);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0400");
            byte[] header = {0x00, 0x6f, 0x60, 0x00, 0x03, 0x00, 0x00};
            isoMsg.setHeader(header);
            isoMsg.set(2, json.getString("card_id"));
            isoMsg.set(3, "010000");
            isoMsg.set(4, json.getString("amount"));
            isoMsg.set(11, json.getString("traceNo"));
            isoMsg.set(14, json.getString("expire").replace("/", ""));
            isoMsg.set(22, "022");
            isoMsg.set(24, "003");
            isoMsg.set(25, "00");
            isoMsg.set(41, json.getString("terminalId"));
            isoMsg.set(42, json.getString("bankMerchantId"));
            //isoMsg.set(60, "00120000000000000100000000000006");
            isoMsg.set(62, "000001");

            buildIsoMsg = isoMsg;

            buildIsoMsg.recalcBitMap();
            byte[] send_PackedRequestData = buildIsoMsg.pack();
            byte[] data_after = purchase_reversal_default();
            for (int i = 18; i <= 43; i++)
                data_after[i] = send_PackedRequestData[i + 1];

            for (int i = 45; i <= 50; i++)
                data_after[i] = send_PackedRequestData[i + 2];

            for (int i = 53; i <= 67; i++)
                data_after[i] = send_PackedRequestData[i + 4];

            logISOMsg(buildIsoMsg);
            return data_after;
        } catch (Exception ex) {
            System.out.println(ex.getMessage().toString());
        }

        return null;
    }

    public byte[] settlement(JSONObject json) {
        try {
            InputStream fis = new FileInputStream(getClass().getClassLoader().getResource("golomt/request_settlement.xml").getFile());
            GenericPackager packager = new GenericPackager(fis);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0500");
            byte[] header = {0x00, 0x56, 0x60, 0x00, 0x03, 0x00, 0x00};
            isoMsg.setHeader(header);
            isoMsg.set(3, "920000");
            try {
                if (json.getString("processCode").equals("960000"))
                    isoMsg.set(3, "960000");
            } catch (Exception ex) {

            }
            isoMsg.set(11, json.getString("traceNo"));
            isoMsg.set(24, "003");
            isoMsg.set(41, json.getString("terminalId"));
            isoMsg.set(42, json.getString("bankMerchantId"));
            isoMsg.set(60, json.getString("batchNo"));
            isoMsg.set(63, json.getString("amount"));

            buildIsoMsg = isoMsg;

            buildIsoMsg.recalcBitMap();
            byte[] send_PackedRequestData = buildIsoMsg.pack();
            byte[] data_after = settlement_default();

            try {
                if (json.getString("processCode").equals("960000"))
                    data_after[17] = send_PackedRequestData[18];
            } catch (Exception ex) {

            }

            for (int i = 25; i <= 32; i++)
                data_after[i] = send_PackedRequestData[i + 2];

            for (int i = 33; i <= 47; i++)
                data_after[i] = send_PackedRequestData[i + 4];

            for (int i = 33; i <= 47; i++)
                data_after[i] = send_PackedRequestData[i + 4];

            for (int i = 57; i < 87; i++)
                data_after[i] = send_PackedRequestData[i + 3];

            logISOMsg(buildIsoMsg);
            return data_after;
        } catch (Exception ex) {
            System.out.println(ex.getMessage().toString());
        }

        return null;
    }

    public byte[] batch_upload(JSONObject json) {
        try {
            InputStream fis = new FileInputStream(getClass().getClassLoader().getResource("golomt/request_batch.xml").getFile());
            GenericPackager packager = new GenericPackager(fis);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0320");
            byte[] header = {0x00, 0x7b, 0x60, 0x00, 0x03, 0x00, 0x00};
            isoMsg.setHeader(header);
            isoMsg.set(2, json.getString("card_id"));
            isoMsg.set(3, "010001");
            isoMsg.set(4, json.getString("amount"));
            isoMsg.set(11, json.getString("traceNo"));
            isoMsg.set(12, json.getString("transTime"));//HHMMSS
            isoMsg.set(13, json.getString("transDate")); //MMDD
            isoMsg.set(14, json.getString("expire").replace("/", ""));
            isoMsg.set(22, "022");
            isoMsg.set(24, "003");
            isoMsg.set(25, "00");
            isoMsg.set(37, json.getString("systemRef"));
            isoMsg.set(38, json.getString("approveCode"));
            //isoMsg.set(39, json.getString("respondCode"));
            isoMsg.set(41, json.getString("terminalId"));
            isoMsg.set(42, json.getString("bankMerchantId"));
            //isoMsg.set(60, "0200000002            ");
            isoMsg.set(62, json.getString("oldTraceNo"));
            System.out.println("Respond code " + json.getString("respondCode"));
            buildIsoMsg = isoMsg;
            byte[] send_PackedRequestData = buildIsoMsg.pack();

            byte[] data_after = batch_upload_default();
            for (int i = 18; i <= 26; i++)
                data_after[i] = send_PackedRequestData[i + 1];

            for (int i = 35; i <= 48; i++)
                data_after[i] = send_PackedRequestData[i + 1];

            for (int i = 50; i <= 61; i++)
                data_after[i] = send_PackedRequestData[i + 2];

            for (int i = 62; i <= 67; i++)
                data_after[i] = send_PackedRequestData[i + 3];

            data_after[68] = 0;
            data_after[69] = 0;

            for (int i = 70; i <= 77; i++)
                data_after[i] = send_PackedRequestData[i + 2];

            for (int i = 78; i <= 92; i++)
                data_after[i] = send_PackedRequestData[i + 4];

            for (int i = 118; i <= 124; i++)
                data_after[i] = send_PackedRequestData[i - 21];

            logISOMsg(buildIsoMsg);
            return data_after;
        } catch (Exception ex) {
            System.out.println(ex.getMessage().toString());
        }

        return null;
    }

    public byte[] purchase_void(JSONObject json) {
        try {
            InputStream fis = new FileInputStream(getClass().getClassLoader().getResource("golomt/request_void.xml").getFile());
            GenericPackager packager = new GenericPackager(fis);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0200");
            byte[] header = {0x00, 0x6f, 0x60, 0x00, 0x03, 0x00, 0x00};
            isoMsg.setHeader(header);
            isoMsg.set(2, json.getString("card_id"));
            isoMsg.set(3, "020000");
            isoMsg.set(4, json.getString("amount"));
            isoMsg.set(11, json.getString("traceNo"));
            isoMsg.set(12, json.getString("transTime"));//HHMMSS
            isoMsg.set(13, json.getString("transDate")); //MMDD
            isoMsg.set(14, json.getString("expire").replace("/", ""));
            isoMsg.set(22, "022");
            isoMsg.set(24, "003");
            isoMsg.set(25, "00");
            isoMsg.set(37, json.getString("systemRef"));
            isoMsg.set(38, json.getString("approveCode"));
            isoMsg.set(41, json.getString("terminalId"));
            isoMsg.set(42, json.getString("bankMerchantId"));
            isoMsg.set(60, "012000000100000");
            isoMsg.set(62, json.getString("oldTraceNo"));

            buildIsoMsg = isoMsg;
            buildIsoMsg.recalcBitMap();
            byte[] send_PackedRequestData = buildIsoMsg.pack();

            byte[] data_after = purchase_void_default();
            for (int i = 18; i <= 45; i++)
                data_after[i] = send_PackedRequestData[i + 1];

            for (int i = 50; i <= 61; i++)
                data_after[i] = send_PackedRequestData[i + 2];
            for (int i = 62; i <= 67; i++)
                data_after[i] = send_PackedRequestData[i + 3];
            for (int i = 68; i <= 75; i++)
                data_after[i] = send_PackedRequestData[i + 4];
            for (int i = 76; i <= 90; i++)
                data_after[i] = send_PackedRequestData[i + 6];

            logISOMsg(buildIsoMsg);

            return data_after;
        } catch (Exception ex) {
            System.out.println(ex.getMessage().toString());
        }

        return null;
    }

    public byte[] purchase_void_default() {
        String str = "006f60000300000200703c05800cc00014169496255596404287020000000000100000000009141407070519010022000300303730353431393831393539363030393736313231323131333830303030303030303030323436333800123030303030303130303030300006303030303031";
        return utils.hexStringToByteArray(str);
    }

    public byte[] settlement_default() {
        String str = "0056600003000005002020010000c000129200000000110003313231323131333830303030303030303030323436333800063030303030310030303031303030303030303031303030303030303030303030303030303030";
        return utils.hexStringToByteArray(str);
    }

    public byte[] batch_upload_default() {
        String str = "007b60000300000320703c05800ec00014169496255596404287010001000000000000000012181442063019010022000300303633303431393739393531353931393634303031323132313133383030303030303030303032343633380022303230303030303030322020202020202020202020200006303030303031";
        return utils.hexStringToByteArray(str);
    }

    public byte[] purchase_reversal_default() {
        String str = "004a600003000004007024058000c000041694962555964042870100000000000001000000101901002200030031323132313133383030303030303030303032343633380006303030303031";
        return utils.hexStringToByteArray(str);
    }

    public JSONObject response(SSLSocket connection, JSONObject json, bank bank) throws Exception {
        JSONObject res = new JSONObject();
        BufferedReader receive_PackedResponseData = null;
        if (connection.isConnected()) {
            int q = 0;
            while (true) {
                System.out.println("-- RESPONSE recieved---");

                receive_PackedResponseData = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                char[] buf = new char[58];
                for (int i = 0; i < buf.length; i++) buf[i] = ' ';
                receive_PackedResponseData.read(buf);
                System.out.println("Pre Response (PURCHASE) (Orginal String): " + buf.length + " " + new String(buf));
                if (buf[10] != ' ') {
                    if (bank.getMode().equals("purchase")) {
                        System.out.println("Response (PURCHASE) (Orginal String): " + new String(buf));

                        res.put("traceNo", json.getString("traceNo"));
                        res.put("systemRef", utils.getNumber(buf, 30, 12));
                        res.put("cardNo", utils.formatCard(json.getString("card_id"))); //nemegdel
                        res.put("terminalId", json.getString("terminalId"));//nemegdel
                        res.put("bankMerchantId", json.getString("bankMerchantId"));//nemegdel
                        res.put("approveCode", utils.getNumber(buf, 42, 6));
                        res.put("transTime", utils.getString(buf, 23, 3));
                        res.put("transDate", utils.getString(buf, 26, 2));
                        res.put("amount", utils.amount(json.getDouble("amount")));
                        if (buf[55] != ' ')
                            res.put("respondCode", "3" + buf[48] + "3" + buf[49]);
                        else
                            res.put("respondCode", "3" + buf[42] + "3" + buf[43]);

                        System.out.println("Payment result : " + (res.getString("respondCode")));
                    } else if (bank.getMode().equals("void")) {
                        System.out.println("Response (VOID) (Orginal String): " + new String(buf));
                        System.out.println("Payment result : " + ((buf[42] + "" + buf[43])));
                        res.put("respondCode", "3" + buf[42] + "3" + buf[43]);
                    } else if (bank.getMode().equals("reversal")) {
                        System.out.println("Response (REVERSAL) (Orginal String): " + new String(buf));
                        System.out.println("Payment result : " + ((buf[42] + "" + buf[43])));
                        res.put("respondCode", "3" + buf[42] + "3" + buf[43]);
                    } else if (bank.getMode().equals("settlement")) {
                        System.out.println("Response (SETTLEMENT) (Orginal String): " + new String(buf));
                        System.out.println("Payment result : " + ((buf[42] + "" + buf[43])));
                        res.put("respondCode", "3" + buf[42] + "3" + buf[43]);
                    } else if (bank.getMode().equals("batch")) {
                        System.out.println("Response (BATCH) (Orginal String): " + new String(buf));
                        System.out.println("Payment result : " + ((buf[42] + "" + buf[43])));
                        res.put("respondCode", "3" + buf[42] + "3" + buf[43]);
                    }
                    receive_PackedResponseData.close();
                    break;
                }
                q++;
                Thread.sleep(1000);
                if (q == 3) {
                    receive_PackedResponseData.close();
                    break;
                }
            }
        }

        return res;
    }
}
