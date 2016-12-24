package mn.ezpay.service;

import mn.ezpay.dao.providerDao;
import mn.ezpay.entity.providers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class providerService {
    @Autowired
    providerDao dao;

    public providers findOne(int id) {
        return dao.findOne(id);
    }

    public long total() {
        return dao.total();
    }

    public void save(providers entity) {
        dao.save(entity);
    }

    public void update(providers entity) {
        dao.update(entity);
    }

    public void delete(providers entity) {
        dao.delete(entity);
    }

    public List<providers> findAll(int page, int size, String order, String dir) {
        return dao.findAll(page, size, order, dir);
    }
}
