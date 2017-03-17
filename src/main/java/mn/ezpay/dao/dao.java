package mn.ezpay.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class dao<T> {
    @Autowired
    public SessionFactory sessionFactory;

    public Criteria crit;
    public long total = 0;
    public Session session;

    public Session getSession() {
//        if (session.isCl)
//        return (session = sessionFactory.getCurrentSession());
//        //return (session = sessionFactory.openSession());
        if (session != null && session.isOpen()) {
            session.clear();
            return (session = sessionFactory.getCurrentSession());
        }
        return (session = sessionFactory.getCurrentSession());
    }

    public void close() {
       if (session != null && session.isOpen())
            session.close();
    }

    public void save(final T entity) {
        getSession();
        session.getTransaction().begin();
        try {
            session.save(entity);
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }
    }

    public T update(final T entity) {
        getSession();
        session.getTransaction().begin();
        try {
            session.saveOrUpdate(entity);
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return entity;
    }

    protected T findOne(final Class<T> type, final int id) {
        getSession();
        session.getTransaction().begin();
        T item = null;
        try {
            item = (T) session.get(type, id);
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }
        return item;
    }

    public void delete(final T entity) {
        getSession();
        session.getTransaction().begin();
        try {
            session.delete(entity);
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }
    }

    public long total(final Class<T> type) {
        long total = 0;
        getSession();
        session.getTransaction().begin();
        try {
            crit = session.createCriteria(type);
            crit.setProjection(Projections.rowCount());
            total = (Long) crit.uniqueResult();
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return total;
    }

    public <T> List<T> findAll(final Class<T> type) {
        getSession();
        session.getTransaction().begin();
        List<T> list = null;
        try {
            crit = session.createCriteria(type);
            list = crit.list();
            total = totalUniq(crit);
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return list;
    }

    public T findOne(final Class<T> type, String field, String value) {
        getSession();
        session.getTransaction().begin();
        T item = null;
        try {
            crit = session.createCriteria(type);
            crit.setFirstResult(0);
            crit.setMaxResults(1);
            crit.add(Restrictions.eq(field, value));
            crit.addOrder(Order.asc("id"));
            List<T> list = crit.list();
            item = list.size() > 0 ? list.get(0) : null;
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }
        return item;
    }

    public <T> List<T> findAll(final Class<T> type, int page, int size, String order, String dir) {
        getSession();
        session.getTransaction().begin();
        List<T> list = new LinkedList<>();
        try {
            crit = session.createCriteria(type);
            crit.setFirstResult((page - 1) * size);
            crit.setMaxResults(size);
            if (order == null) {
                order = "id";
                dir = "asc";
            }
            if ("desc".equals(dir))
                crit.addOrder(Order.desc(order));
            else
                crit.addOrder(Order.asc(order));
            list = crit.list();
            total = totalUniq(crit);
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return list;
    }

    public String jsonString(Object object) throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(object);
        return json;
    }

    public long totalUniq(Criteria crit) {
        crit.setProjection(Projections.rowCount());
        crit.setFirstResult(0);
        crit.setMaxResults(1);
        long total = (Long) crit.uniqueResult();
        return total;
    }

    public long total() {
        return total;
    }

}
