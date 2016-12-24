package mn.ezpay.dao;

import mn.ezpay.entity.cards;
import mn.ezpay.payment.utils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class cardDao extends dao<cards> {
    public cards findOne(final int id) {
        return findOne(cards.class, id);
    }

    public List<cards> findAll(int page, int size, String order, String dir) {
        return findAll(cards.class, page, size, order, dir);
    }

    public cards update(cards entity) {
        if (entity.getEnc().indexOf("card_id") != -1) {
            entity.setEnc(utils.encryptedData(entity.getEnc(), getClass().getClassLoader().getResource("cfg/public.der").getFile()));
        }
        return super.update(entity);
    }

    public cards activate(int id, String pin) {
        cards item = findOne(id);
        if (item != null && item.getPpin().equals(pin)) {
            item.setStatus("active");
            item = update(item);
        }
        return item;
    }
}
