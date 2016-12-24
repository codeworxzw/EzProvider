package mn.ezpay.service;

import mn.ezpay.dao.cardDao;
import mn.ezpay.entity.cards;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class cardService {
    @Autowired
    cardDao dao;

    public cards findOne(int id) {
        return dao.findOne(id);
    }

    public long total() {
        return dao.total();
    }

    public void save(cards entity) {
        dao.save(entity);
    }

    public cards update(cards entity) {
        return dao.update(entity);
    }

    public cards activate(int id, String pin) {
        return dao.activate(id, pin);
    }

    public void delete(cards entity) {
        dao.delete(entity);
    }

    public List<cards> findAll(int page, int size, String order, String dir) {
        return dao.findAll(page, size, order, dir);
    }
}
