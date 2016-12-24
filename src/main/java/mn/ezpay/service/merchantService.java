package mn.ezpay.service;

import mn.ezpay.dao.merchantDao;
import mn.ezpay.entity.merchant;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class merchantService {

    @Autowired
    merchantDao dao;

    public merchant findOne(int id) {
        return dao.findOne(id);
    }

    public long total() {
        return dao.total();
    }

    public void save(merchant entity) {
        dao.save(entity);
    }

    public void update(merchant entity) {
        dao.update(entity);
    }

    public void delete(merchant entity) {
        dao.delete(entity);
    }

    public List<merchant> findAll(int page, int size, String order, String dir) {
        return dao.findAll(page, size, order, dir);
    }

}
