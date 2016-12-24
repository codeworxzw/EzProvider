package mn.ezpay.dao;

import mn.ezpay.entity.merchant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class merchantDao extends dao<merchant> {
    public merchant findOne(final int id) {
        return findOne(merchant.class, id);
    }

    public List<merchant> findAll(int page, int size, String order, String dir) {
        return findAll(merchant.class, page, size, order, dir);
    }
}
