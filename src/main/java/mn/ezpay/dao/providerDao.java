package mn.ezpay.dao;

import mn.ezpay.entity.providers;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class providerDao extends dao<providers> {
    public providers findOne(final int id) {
        return findOne(providers.class, id);
    }

    public List<providers> findAll(int page, int size, String order, String dir) {
        return findAll(providers.class, page, size, order, dir);
    }
}
