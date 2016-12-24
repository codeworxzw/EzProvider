package mn.ezpay.controller;

import mn.ezpay.entity.cards;
import mn.ezpay.entity.wallets;
import mn.ezpay.payment.utils;
import mn.ezpay.security.base64;
import mn.ezpay.service.walletService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.List;

@RestController
public class walletsController {

    @Autowired
    walletService service;

    @RequestMapping(value = "wallets/create", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public wallets create(@RequestParam String walletId, String deviceName) {
        if (walletId != null && deviceName != null)
            return service.create(walletId, deviceName);

        return new wallets();
    }

    @RequestMapping(value = "wallets/activation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public wallets activision(@RequestParam String walletId, String pin) {
        return service.activision(walletId, pin);
    }

    @RequestMapping(value = "wallets/save", method = RequestMethod.POST, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public wallets save(@RequestBody wallets entity) {
        service.save(entity);
        return entity;
    }

    @RequestMapping(value = "wallets/delete", method = RequestMethod.DELETE)
    public wallets delete(@RequestParam int id) {
        wallets item = service.findOne(id);
        service.delete(item);
        return item;
    }

    @RequestMapping(value = "wallets/update", method = RequestMethod.POST, consumes = "application/json", produces = "application/json; charset=utf-8", headers = "Accept=*/*")
    public wallets update(@RequestBody wallets entity) {
        service.update(entity);
        return entity;
    }

    @RequestMapping(value = "wallets/findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Hashtable findAll(@RequestParam int page, @RequestParam int size, @RequestParam String order, @RequestParam String dir) {
        List list = service.findAll(page, size, order, dir);
        Hashtable pageable = new Hashtable();
        pageable.put("total", service.total());
        pageable.put("data", list);
        return pageable;
    }

    @RequestMapping(value = "wallets/findOne", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public wallets findOne(@RequestParam String walletId)  {
        wallets wallet = service.findOne(walletId);
        if (wallet != null && wallet.getStatus().equals("active")) {
            List<cards> cards = wallet.getCards();
            if (cards != null) {
                for (int i = 0; i < cards.size(); i++) {
                    cards c = cards.get(i);
                    String enc = utils.decrypt(base64.decode(c.getEnc()), getClass().getClassLoader().getResource("cfg/private.der").getFile());

                    try {
                        JSONObject item = new JSONObject(enc);
                        if (item.getString("loyalty").equals("false"))
                            item.put("card_id", utils.formatCard(item.getString("card_id")));

                        item.put("id", c.getId());
                        c.setEnc(item.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else {

        }

        return wallet;
    }

}
