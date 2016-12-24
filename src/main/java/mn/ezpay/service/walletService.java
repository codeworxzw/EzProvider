package mn.ezpay.service;

import mn.ezpay.dao.walletDao;
import mn.ezpay.entity.wallets;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class walletService {
    @Autowired
    walletDao dao;

    public wallets findOne(int id) {
        return dao.findOne(id);
    }

    public wallets findOne(String id) {
        return dao.findOne(id);
    }

    public long total() {
        return dao.total();
    }

    public void save(wallets entity) {
        dao.save(entity);
    }

    public wallets create(String walletId, String deviceName) {
        return dao.create(walletId, deviceName);
    }

    public wallets activision(String walletId, String pin) {
        return dao.activision(walletId, pin);
    }

    public wallets check(String walletId, String pin) {
        return dao.check(walletId, pin);
    }

    public void update(wallets entity) {
        dao.update(entity);
    }

    public void delete(wallets entity) {
        dao.delete(entity);
    }

    public List<wallets> findAll(int page, int size, String order, String dir) {
        return dao.findAll(page, size, order, dir);
    }
}
