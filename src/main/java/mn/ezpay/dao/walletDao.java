package mn.ezpay.dao;

import mn.ezpay.entity.wallets;
import mn.ezpay.msg.msgGW;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class walletDao extends dao<wallets> {

    public wallets findOne(final int id) {
        return findOne(wallets.class, id);
    }

    public wallets findOne(final String id) {
        return findOne(wallets.class, "walletId", id);
    }

    public List<wallets> findAll(int page, int size, String order, String dir) {
        return findAll(wallets.class, page, size, order, dir);
    }

    public wallets create(String walletId, String deviceName) {
        getSession();
        session.getTransaction().begin();
        wallets res = null;
        try {
            Query query = session.getNamedQuery("removeWallet");
            query.setParameter("walletId", walletId);
            query.setParameter("status", "inactive");
            query.executeUpdate();
            session.getTransaction().commit();

            wallets w = findOne(walletId);
            if (w == null) w = new wallets();

            w.setWalletId(walletId);
            w.setStatus("inactive");
            w.setDeviceName(deviceName);
            int pin = 10000 + (int) (Math.random() * 10000);
            w.setPin(Integer.toString(pin));
            res = update(w);

            //sms gateway
            msgGW.send(w.getWalletId(), msgGW.buildMsg(1, new String[]{w.getPin()}));
        } catch (Exception ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return res;
    }

    public wallets activision(String walletId, String pin) {
        getSession();
        session.getTransaction().begin();
        wallets res = null;
        try {
            Query query = session.getNamedQuery("activisionQuery");
            query.setParameter("walletId", walletId);
            query.setParameter("status", "inactive");
            query.setParameter("pin", pin);
            List<wallets> list = query.list();
            session.getTransaction().commit();
            if (list.size() > 0) {
                wallets w = list.get(0);
                w.setStatus("active");
                res = update(w);
            } else
                res = findOne(walletId);
        } catch (Exception ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return res;
    }

    public wallets check(String walletId, String pin) {
        getSession();
        session.getTransaction().begin();
        wallets res = null;
        try {
            Query query = session.getNamedQuery("activisionQuery");
            query.setParameter("walletId", walletId);
            query.setParameter("status", "active");
            query.setParameter("pin", pin);
            List<wallets> list = query.list();
            if (list.size() > 0)
                res = list.get(0);
            else
                res = findOne(walletId);
        } catch (Exception ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }
        return res;
    }
}
