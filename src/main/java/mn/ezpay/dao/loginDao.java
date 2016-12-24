package mn.ezpay.dao;

import mn.ezpay.entity.logins;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class loginDao extends dao<logins> {
    public logins findOne(final int id) {
        return findOne(logins.class, id);
    }

    public logins findOne(final String id) {
        return findOne(logins.class, "qr_data", id);
    }

    public List<logins> findAll(int page, int size, String order, String dir) {
        return findAll(logins.class, page, size, order, dir);
    }

    public logins confirm(String walletId, String qr_data) {
        logins entity = findOne(qr_data);
        if (entity != null) {
            entity.setWalletId(walletId);
            entity.setStatus("active");
            entity = update(entity);
        }
        return entity;
    }
}
