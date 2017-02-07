package mn.ezpay.service;

import mn.ezpay.dao.tokenDao;
import mn.ezpay.entity.cards;
import mn.ezpay.entity.multitoken;
import mn.ezpay.entity.token;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class tokenService {

    @Autowired
    tokenDao dao;

    public long total() {
        return dao.total();
    }

    public void save(token entity) {
        dao.save(entity);
    }

    public void update(token entity) {
        dao.update(entity);
    }

    public void delete(token entity) {
        dao.delete(entity);
    }

    public List<token> findAll(int page, int size, String order, String dir) {
        return dao.findAll(page, size, order, dir);
    }

    public List<cards> findLoyalty(String token) {
        return dao.findLoyalty(token);
    }

    public token findToken(String token) {
        return dao.findToken(token);
    }

    public cards confirmCard(cards entity) {
        return dao.confirmCard(entity);
    }

    public List<multitoken> token5(String walletId, String hashed) {
        return dao.generate5Token(walletId, hashed);
    }

    public token payment(token entity) {
        return dao.payment(entity, findToken(entity.getToken()));
    }

    public token check(token token) {
        return dao.check(token);
    }

    public token request(token entity) {
        return dao.request(entity);
    }

}
