package mn.ezpay.controller;

import mn.ezpay.entity.cards;
import mn.ezpay.entity.multitoken;
import mn.ezpay.entity.token;
import mn.ezpay.payment.vault;
import mn.ezpay.security.base64;
import mn.ezpay.service.tokenService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.List;

@RestController
public class paymentController {

    @Autowired
    tokenService service;

    @RequestMapping(value = "token/delete", method = RequestMethod.DELETE)
    public token delete(@RequestParam String token) {
        token item = service.findToken(token);
        service.delete(item);
        return item;
    }

    @RequestMapping(value = "token/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json; charset=utf-8", headers = "Accept=*/*")
    public token update(@RequestBody token entity) {
        service.update(entity);
        return entity;
    }

    @RequestMapping(value = "token/findOne", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public token findOne(@RequestParam String token) {
        token t = service.findToken(token);
        return t;
    }

    @RequestMapping(value = "token/findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Hashtable findAll(@RequestParam int page, @RequestParam int size, @RequestParam String order, @RequestParam String dir) {
        List list = service.findAll(page, size, order, dir);
        Hashtable pageable = new Hashtable();
        pageable.put("total", service.total());
        pageable.put("data", list);
        return pageable;
    }

    @RequestMapping(value = "token/loyalty", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Hashtable findLoyalty(@RequestParam String token) {
        try {
            List list = service.findLoyalty(token);
            Hashtable loyalty = new Hashtable();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    cards c = (cards) list.get(i);
                    String data = new String(vault.decrypt(base64.decode(c.getEnc()), getClass().getClassLoader().getResource("cfg/private.der").getFile()));
                    JSONObject json = new JSONObject(data);
                    if (json.getString("loyalty").equals("true")) {
                        loyalty.put(json.getString("bank_name"), json.getString("card_id"));
                    }
                }
            } else
                loyalty.put("msg", "bad token.");

            return loyalty;
        } catch (Exception ex) {

        }

        return null;
    }

    @RequestMapping(value = "token/token5", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<multitoken> token5(@RequestParam String walletId, String hashed) {
        return service.token5(walletId, hashed);
    }

    @RequestMapping(value = "token/request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public token request(@RequestBody token token) {
        return service.request(token);
    }

    @RequestMapping(value = "token/check", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public token check(@RequestBody token token) {
        return service.check(token);
    }
}
