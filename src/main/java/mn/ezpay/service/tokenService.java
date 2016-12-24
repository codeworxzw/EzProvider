package mn.ezpay.service;

import mn.ezpay.dao.tokenDao;
import mn.ezpay.entity.cards;
import mn.ezpay.entity.token;
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

    public token payment(token entity) {
        return dao.payment(entity, findToken(entity.getToken()));
    }

    public token pvoid(token entity) {
        return dao.pvoid(entity);
    }

    public token check(token token) {
        return dao.check(token);
    }

    public token request(token entity) {
        return dao.request(entity);
    }

}
