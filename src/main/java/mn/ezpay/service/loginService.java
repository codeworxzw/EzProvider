package mn.ezpay.service;

import mn.ezpay.dao.loginDao;
import mn.ezpay.entity.logins;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class loginService {
    @Autowired
    loginDao dao;

    public logins findOne(int id) {
        return dao.findOne(id);
    }

    public logins findOne(String id) {
        return dao.findOne(id);
    }

    public long total() {
        return dao.total();
    }

    public void save(logins entity) {
        dao.save(entity);
    }

    public logins update(logins entity) {
        return dao.update(entity);
    }

    public logins confirm(String walletId, String qr_data) {
        return dao.confirm(walletId, qr_data);
    }

    public void delete(logins entity) {
        dao.delete(entity);
    }

    public List<logins> findAll(int page, int size, String order, String dir) {
        return dao.findAll(page, size, order, dir);
    }
}
