package mn.ezpay.controller;

import mn.ezpay.entity.logins;
import mn.ezpay.entity.wallets;
import mn.ezpay.security.randomStr;
import mn.ezpay.service.loginService;
import mn.ezpay.service.walletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class loginsController {

    @Autowired
    loginService service;
    @Autowired
    walletService wservice;

    @RequestMapping(value = "login/request", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String request() {
        logins entity = new logins();
        String qr = "";
        entity.setQr_data(qr = new randomStr(13).nextString());
        entity.setStatus("inactive");
        service.save(entity);
        return qr;
    }

    @RequestMapping(value = "login/check", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public logins check(@RequestParam String qr, HttpSession session) {
        logins entity = service.findOne(qr);
        if (entity.getStatus().equals("active")) {
            session.setAttribute("token", entity.getQr_data());
        }
        return entity;
    }

    @RequestMapping(value = "login/sign", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public logins sign(@RequestParam String walletId, @RequestParam String pin, @RequestParam String qr, HttpSession session) {
        wallets w = wservice.check(walletId, pin);
        logins entity = service.findOne(qr);
        if (w != null && w.getStatus().equals("active")) {
            entity.setWalletId(w.getWalletId());
            entity.setStatus("active");
            service.update(entity);
            session.setAttribute("token", entity.getQr_data());
        }
        return entity;
    }

    @RequestMapping(value = "login/signout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void signout(HttpSession session) {
        session.setAttribute("token", null);
    }

    @RequestMapping(value = "login/session", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public logins session(HttpSession session) {
        String token = (String) session.getAttribute("token");
        return service.findOne(token);
    }

    @RequestMapping(value = "login/confirm", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public logins confirm(@RequestParam String walletId, @RequestParam String qr_data, HttpSession session) {
        logins lg = service.confirm(walletId, qr_data);
        if (lg.getStatus().equals("active")) {
            session.setAttribute("token", lg.getQr_data());
        }
        return lg;
    }
}
