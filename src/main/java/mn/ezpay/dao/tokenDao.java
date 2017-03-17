package mn.ezpay.dao;

import mn.ezpay.entity.*;
import mn.ezpay.payment.make;
import mn.ezpay.payment.type;
import mn.ezpay.payment.vault;
import mn.ezpay.security.base64;
import org.bouncycastle.util.encoders.Base64;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Repository
public class tokenDao extends dao<token> {
    private static int NEW = 0;
    private static int SUCCESS = 3;
    private static int FAIL = 5;
    private static int CONFIRMED = 1;
    private static int CANCELLED = 4;
    private static int USER_ACCEPT = 2;
    private static int SETTLEMENT = 7;

    private static int BARCODE13 = 9;
    private static int CARD_LENGTH = 16;
    private static int TIMEOUT = 60;
    private static int RESPONDING_TIME = 1000 * 60;

    public List<token> findAll(int page, int size, String order, String dir) {
        return findAll(token.class, page, size, order, dir);
    }

    public token findToken(String token) {
        token t = null;
        getSession();
        session.getTransaction().begin();
        try {
            crit = session.createCriteria(token.class);
            crit.add(Restrictions.eq("token", token));
            //crit.add(Restrictions.lt("timestampdiff(SECOND,_date,current_timestamp)", 60*60*10000));
            crit.addOrder(Order.desc("_date"));
            List<token> list = crit.list();
            session.getTransaction().commit();
            t = list.size()>0?list.get(0):new token();
        } catch (Exception ex) {
            t = new token();
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return t;
    }

    public List<cards> findLoyalty(String token) {
        List<cards> list = null;
        token entity = findToken(token);
        String walletId = entity.getWalletId();
        getSession();
        session.getTransaction().begin();
        try {
            crit = session.createCriteria(cards.class);
            crit.add(Restrictions.eq("walletId", walletId));
            crit.add(Restrictions.eq("status", "active"));
            list = crit.list();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return list;
    }

    public wallets findWallet(String walletId) {
        getSession();
        session.getTransaction().begin();
        wallets res = null;
        try {
            crit = session.createCriteria(wallets.class);
            crit.add(Restrictions.eq("walletId", walletId));
            List<wallets> list = crit.list();
            res = (list.size()>0?list.get(0):null);
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return res;
    }

    public JSONObject findCard(JSONObject card, wallets wallet, token token) {
        JSONObject sel = new JSONObject();
        try {
            for (int i = 0; i < wallet.getCards().size(); i++) {
                cards item = wallet.getCards().get(i);
                JSONObject card_data = new JSONObject(vault.decrypt(Base64.decode(item.getEnc()), getClass().getClassLoader().getResource("cfg/default.der").getFile()));
                if (card.getString("card_id").indexOf("*") != -1 || token.getType().equals("long")) {
                    String pan4 = card.getString("card_id").substring(15, 19);
                    if (card_data.getString("card_id").endsWith(pan4)) {
                        sel = card_data;
                        System.out.println(card.getString("card_id")+" "+card_data.getString("card_id"));
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sel;
    }

    public multitoken update_multi(multitoken entity) {
        getSession();
        session.getTransaction().begin();
        try {
            multitoken item = (multitoken) session.merge(entity);
            session.getTransaction().commit();
            return item;
        } catch (RuntimeException ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return entity;
    }

    public List<multitoken> generate5Token(String walletId, String hashed) {
        getSession();
        List tokens;
        session.getTransaction().begin();

//        Query queryDisable = session.getNamedQuery("disableTokens");
//        queryDisable.setParameter("walletId", walletId);
//        queryDisable.executeUpdate();

        Query query5 = session.getNamedQuery("multiTokens");
        query5.setParameter("walletId", walletId);
        tokens = query5.list();
        session.getTransaction().commit();
        for (int i = 0; i < 5; i++) {
            String token = vault.generateToken();
            multitoken m = new multitoken();
            m.setToken(token);
            m.setStatus("active");
            m.setWalletId(walletId);
            m.setHashed(hashed);
            tokens.add(update_multi(m));
        }

        return tokens;
    }

    public String batchNo(String terminalId, String merchantId)  {
        String batchNo = "0";
        getSession();
        session.getTransaction().begin();
        try {
            crit = session.createCriteria(trace.class);
            crit.add(Restrictions.eq("terminalId", terminalId));
            crit.add(Restrictions.eq("merchantId", merchantId));
            List<trace> list = crit.list();

            if (list.size() > 0) {
                trace t = list.get(0);
                t.setBatchNo(t.getBatchNo() + 1);
                session.saveOrUpdate(t);
                batchNo = t.getBatchNo() + "";
                while (batchNo.length() < 6) {
                    batchNo = "0" + batchNo;
                }
            }

            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            close();
        }
        return batchNo;
    }

    public String traceNo(String terminalId, String merchantId)  {
        String traceNo = "0";
        getSession();
        session.getTransaction().begin();
        try {
            crit = session.createCriteria(trace.class);
            crit.add(Restrictions.eq("terminalId", terminalId));
            crit.add(Restrictions.eq("merchantId", merchantId));
            List<trace> list = crit.list();

            if (list.size() > 0) {
                trace t = list.get(0);
                t.setTraceNo(t.getTraceNo() + 1);
                session.saveOrUpdate(t);
                traceNo = t.getTraceNo() + "";
                while (traceNo.length() < 6) {
                    traceNo = "0" + traceNo;
                }
            }

            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            close();
        }
        return traceNo;
    }

    public token request(token entity) {
        getSession();
        session.getTransaction().begin();
        String token = "";
        try {
            Query query = session.getNamedQuery("expiredTokens");
            query.setParameter("time_out", 60);
            query.executeUpdate();
            if (entity.getType().equals("long")) {
                token = entity.getToken();
                Query query5 = session.getNamedQuery("token5");
                query5.setParameter("token", token);
                List<multitoken> list = query5.list();
                if (list.size() > 0) {
                    entity.setToken(list.get(0).getToken());
                    entity.setHashed(list.get(0).getHashed());
                    entity.setWalletId(list.get(0).getWalletId());
                    entity.setResponse("{'code':'EZ901','msg':'Token амжилттай үүслээ !'}");
                    session.save(entity);
                    session.getTransaction().commit();
                }
            } else {
                token = vault.generateToken();
                System.out.println(miniToken(token));
                entity.setResponse("{'code':'EZ901','msg':'Token амжилттай үүслээ !'}");
                entity.setToken(token);
                session.save(entity);
                session.getTransaction().commit();
            }
        } catch (Exception ex) {
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return findToken(token);
    }

    public void batch_upload(settlement entity) {
        getSession();
        try {
            session.getTransaction().begin();
            Query query = session.getNamedQuery("dayList");
            query.setParameter("_day", entity.get_day());
            query.setParameter("merchantData", entity.getMerchantData());
            List<token> list = query.list();
            session.getTransaction().commit();
            make make = new make();
            int r = 0;
            for (int i = 0; i < list.size(); i++) {
                token token = list.get(i);
                JSONObject hashed = new JSONObject(vault.decrypt(base64.decode(token.getHashed()), getClass().getClassLoader().getResource("cfg/private.der").getFile()));
                JSONObject card = null;
                if ("short".equals(token.getType()))
                    card = findCard(hashed, findWallet(token.getWalletId()), token);
                else
                    card = hashed;

                JSONObject bankEntity = new JSONObject();
                bankEntity.put("card_id", card.getString("card_id"));
                bankEntity.put("amount", token.getAmount());
                bankEntity.put("expire", card.getString("expire"));
                bankEntity.put("terminalId", token.getMerchantData().split(":")[0]);
                bankEntity.put("bankMerchantId", token.getMerchantData().split(":")[1]);
                bankEntity.put("traceNo", traceNo(token.getMerchantData().split(":")[0], token.getMerchantData().split(":")[1]));
                bankEntity.put("batchNo", batchNo(entity.getMerchantData().split(":")[0], entity.getMerchantData().split(":")[1]));

                if (token.getAmount() < 0) {
                    bankEntity.put("systemRef", token.getTraceOld().get(0).getSystemRef());
                    bankEntity.put("approveCode", token.getTraceOld().get(0).getApproveCode());
                    bankEntity.put("amount", token.getTraceOld().get(0).getAmount());
                    bankEntity.put("transTime", token.getTraceOld().get(0).getTransTime());
                    bankEntity.put("transDate", token.getTraceOld().get(0).getTransDate());
                    bankEntity.put("oldTraceNo", token.getOldTraceNo());
                } else {
                    bankEntity.put("systemRef", token.getTrace().get(0).getSystemRef());
                    bankEntity.put("approveCode", token.getTrace().get(0).getApproveCode());
                    bankEntity.put("amount", token.getTrace().get(0).getAmount());
                    bankEntity.put("transTime", token.getTrace().get(0).getTransTime());
                    bankEntity.put("transDate", token.getTrace().get(0).getTransDate());
                    bankEntity.put("oldTraceNo", token.getTraceNo());
                }
                JSONObject res = make.makePayment(type.golomt, "batch", bankEntity);
                if (res.getString("respondCode").equals("3030")) {
                   r++;
                } else {

                }
            }

            if (r == list.size()) {
                getSession();
                session.getTransaction().begin();
                Query query1 = session.getNamedQuery("settleCommit");
                query1.setParameter("day", entity.get_day());
                query1.setParameter("merchantData", entity.getMerchantData());
                query1.setParameter("merchantId", entity.getMerchantId());
                query1.setParameter("amount", entity.getAmount());
                query1.setParameter("count", r);
                query1.setParameter("respondCode", "3030");
                query1.executeUpdate();
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            //session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public settlement settlement(settlement entity) {
        getSession();
        try {
            make make = new make();
            session.getTransaction().begin();
            Query query = session.getNamedQuery("dayList");
            query.setParameter("_day", entity.get_day());
            query.setParameter("merchantData", entity.getMerchantData());
            List<token> list = query.list();
            session.getTransaction().commit();
            if (list.size() > 0) {
                double amount = 0;
                double ramount = 0;
                int q = 0, p = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getAmount() > 0) {
                        q++;
                        amount += Math.abs(list.get(i).getAmount());
                    }
                    if (list.get(i).getAmount() < 0) {
                        p++;
                        ramount += Math.abs(list.get(i).getAmount());
                    }
                }
                q -= p;
                amount -= ramount;

                entity.setAmount(amount);
                JSONObject bankEntity = new JSONObject();

                String total = vault.settleamount(q, amount, p, ramount);
                //String total = vault.amount(amount, 90);
                bankEntity.put("amount", total);
                bankEntity.put("terminalId", entity.getMerchantData().split(":")[0]);
                bankEntity.put("bankMerchantId", entity.getMerchantData().split(":")[1]);
                bankEntity.put("batchNo", batchNo(entity.getMerchantData().split(":")[0], entity.getMerchantData().split(":")[1]));
                bankEntity.put("traceNo", traceNo(entity.getMerchantData().split(":")[0], entity.getMerchantData().split(":")[1]));

                JSONObject res = make.makePayment(type.golomt, "settlement", bankEntity);
                if (res.getString("respondCode").equals("3030")) {
                    bankEntity.put("processCode", "960000");
                    res = make.makePayment(type.golomt, "settlement", bankEntity);
                    entity.setRespondCode(res.getString("respondCode"));

                    Query query1 = session.getNamedQuery("settleCommit");
                    query1.setParameter("day", entity.get_day());
                    query1.setParameter("merchantData", entity.getMerchantData());
                    query1.setParameter("merchantId", entity.getMerchantId());
                    query1.setParameter("amount", amount);
                    query1.setParameter("count", q);
                    query1.setParameter("respondCode", res.getString("respondCode"));
                    query1.executeUpdate();

                    return entity;
                } else {
                    entity.setRespondCode(res.getString("respondCode"));
                    batch_upload(entity);
                    return entity;
                }
            } else {
                System.out.println("settlement not found");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            close();
        }

        return entity;
    }

    public token check(token entity) {
        String token = tokenFull(entity); //mini baiwal tokenii buheldeh
        if (token.length() > BARCODE13) {
            token old = findToken(token);
            if (old != null) {
                if (old.getStatus() == NEW && entity.getStatus() == CONFIRMED && (entity.getMerchantData() != null && entity.getMerchantData().length() > 4)) { //merchant confirm
                    getSession();
                    session.getTransaction().begin();
                    try {
                        old.setStatus(CONFIRMED);
                        old.setResponse("{'code':'EZ901','msg':'Token-г борлуулагч баталгаажууллаа !'}");
                        old.setMerchantId(entity.getMerchantId());
                        old.setMerchantData(entity.getMerchantData());
                        old.setAmount(entity.getAmount());
                        if (entity.getAmount() < 0)
                            old.setOldTraceNo(entity.getOldTraceNo()); //butsaaltiin ueiin traceNo
                        session.merge(old);
                        session.getTransaction().commit();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        session.getTransaction().rollback();
                    } finally {
                        close();
                    }
                    return old;
                } else if (old.getStatus() == CONFIRMED && entity.getStatus() == USER_ACCEPT) {//transaction make
                    getSession();
                    session.getTransaction().begin();
                    try {
                        old.setResponse("{'code':'EZ901','msg':'Token-г хэрэглэгч баталгаажууллаа !'}");
                        old.setStatus(entity.getStatus());
                        session.merge(old);
                        session.getTransaction().commit();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        session.getTransaction().rollback();
                    } finally {
                        close();
                        if (old.getAmount() < 0)
                            old = pvoid(entity, old);
                        else
                            old = payment(entity, old);
                    }

                    return old;
                } else if (old.getStatus() == CONFIRMED && (entity.getWalletId() != null && entity.getWalletId().length() > 4)) { //wallet owner confirm
                    getSession();
                    session.getTransaction().begin();
                    try {
                        if (old.getAmount() < 0) {
                            old.setResponse("{'code':'EZ903','msg':'Буцаалтыг батлах'}");
                        } else
                            old.setResponse("{'code':'EZ903','msg':'Гvйлгээг батлах'}");
                        session.getTransaction().commit();
                    } catch (Exception ex) {
                        session.getTransaction().rollback();
                    } finally {
                        close();
                    }
                    return old;
                } else if (old.getStatus() == SUCCESS) {
                    old.setHashed("");
                    return old;
                }

                return old;
            }
        } else {
            entity.setResponse("{'code':'EZ900','msg':'Буруу token !'}");
        }

        return entity;
    }

    public token pvoid(token entity, token old) {
        try {
            if (old.getStatus() == USER_ACCEPT) {
                make make = new make();
                JSONObject hashed = new JSONObject(vault.decrypt(base64.decode(old.getHashed()), getClass().getClassLoader().getResource("cfg/private.der").getFile()));
                JSONObject card = null;
                if ("short".equals(old.getType()))
                    card = findCard(hashed, findWallet(entity.getWalletId()), old);
                else
                    card = hashed;

                if (card != null) {
                    System.out.println("card : "+ card.getString("card_id").replaceAll(" ", ""));
                    JSONObject bankEntity = new JSONObject();
                    bankEntity.put("card_id", card.getString("card_id"));
                    bankEntity.put("amount", old.getAmount());
                    bankEntity.put("expire", card.getString("expire"));
                    bankEntity.put("terminalId", old.getMerchantData().split(":")[0]);
                    bankEntity.put("bankMerchantId", old.getMerchantData().split(":")[1]);
                    bankEntity.put("traceNo", traceNo(old.getMerchantData().split(":")[0], old.getMerchantData().split(":")[1]));

                    bankEntity.put("systemRef", old.getTraceOld().get(0).getSystemRef());
                    bankEntity.put("approveCode", old.getTraceOld().get(0).getApproveCode());
                    bankEntity.put("amount", old.getTraceOld().get(0).getAmount());
                    bankEntity.put("transTime", old.getTraceOld().get(0).getTransTime());
                    bankEntity.put("transDate", old.getTraceOld().get(0).getTransDate());
                    bankEntity.put("oldTraceNo", old.getOldTraceNo());
                    System.out.println("old = "+old.getOldTraceNo());

                    JSONObject res = make.makePayment(type.golomt, "void", bankEntity);
                    if (res.getString("respondCode").equals("3030")) {
                        old.setStatus(SUCCESS);
                        update(old);
                        old.setResponse("{'code':'EZ910','msg':'Буцаалт амжилттай хийгдлээ !'}");
                        return old;
                    } else {
                        old.setStatus(8);
                        update(old);
                        old.setResponse("{'code':'EZ917','msg':'Буцаалт амжилтгүй !'}");
                        return old;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entity;
    }

    public type bankSwitch(String name) {
        if (name.equals("Голомт банк"))
            return type.golomt;

        return type.golomt;
    }

    public cards confirmCard(cards card) {
        JSONObject res = null;
        try {
            System.out.println("CONFIRM CARD BEGIN");
            make make = new make();
            JSONObject hashed = new JSONObject(vault.decrypt(base64.decode(card.getEnc()), getClass().getClassLoader().getResource("cfg/private.der").getFile()));
            String traceNo = traceNo("13133707", "000000000043752");

            JSONObject bankEntity = new JSONObject();
            bankEntity.put("card_id", hashed.getString("card_id"));
            bankEntity.put("amount", 1.0f);
            bankEntity.put("expire", hashed.getString("expire"));
            bankEntity.put("terminalId", "13133707");
            bankEntity.put("bankMerchantId", "000000000043752");
            bankEntity.put("traceNo", traceNo);
            res = make.makePayment(bankSwitch(hashed.getString("bank_name")), "purchase", bankEntity);
            if (res != null && (res.getString("respondCode").equals("3030") || res.getString("respondCode").equals("3035"))) {
                card.setPpin(traceNo);
            }
            System.out.println("CONFIRM CARD END");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return card;
    }

    public token payment(token entity, token old) {
        try {
            if (entity.getStatus() == USER_ACCEPT) {
                String traceNo = traceNo(old.getMerchantData().split(":")[0], old.getMerchantData().split(":")[1]);
                System.out.println("PAYMENT BEGIN");
                make make = new make();
                System.out.println(old.getHashed());
                JSONObject hashed = new JSONObject(vault.decrypt(base64.decode(old.getHashed()), getClass().getClassLoader().getResource("cfg/private.der").getFile()));
                System.out.println(hashed.toString());
                JSONObject card = null;
                if ("short".equals(old.getType()))
                    card = findCard(hashed, findWallet(entity.getWalletId()), old);
                else
                    card = hashed;
                if (card != null) {
                    JSONObject bankEntity = new JSONObject();
                    bankEntity.put("card_id", card.getString("card_id"));
                    bankEntity.put("amount", old.getAmount());
                    bankEntity.put("expire", card.getString("expire"));
                    bankEntity.put("terminalId", old.getMerchantData().split(":")[0]);
                    bankEntity.put("bankMerchantId", old.getMerchantData().split(":")[1]);
                    bankEntity.put("traceNo", traceNo);
                    JSONObject res = null;
                    try {
                        res = make.makePayment(bankSwitch(card.getString("bank_name")), "purchase", bankEntity);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    //if (res != null) res.put("respondCode", "99");
                    if (res != null && (res.getString("respondCode").equals("3030"))) {
                        purchase p = new purchase();
                        List<purchase> pur = new LinkedList<purchase>();
                        p.setApproveCode(res.getString("approveCode"));
                        p.setTraceNo(res.getString("traceNo"));
                        p.setSystemRef(res.getString("systemRef"));
                        p.setAmount(res.getString("amount"));
                        p.setTransTime(res.getString("transTime"));
                        p.setTransDate(res.getString("transDate"));
                        res.put("respondCode", "3030");
                        p.setRespondCode(res.getString("respondCode"));
                        p.setCode("EZ910");
                        res.put("msg", "Success");
                        pur.add(p);
                        old.setTraceNo(traceNo);
                        old.setTrace(pur);
                        old.setStatus(SUCCESS);
                        res.put("code", "EZ910");
                        old.setResponse(res.toString());
                        update(old);
                    } else {
                        if (res != null && res.getString("respondCode").equals("3931")) {
                            bankEntity.put("traceNo", traceNo); //new traceNo
                            bankEntity.put("systemRef", res.getString("systemRef"));
                            bankEntity.put("approveCode", res.getString("approveCode"));
                            bankEntity.put("amount", res.getString("amount"));
                            res = make.makePayment(bankSwitch(card.getString("bank_name")), "reversal", bankEntity);

                            purchase p = new purchase();
                            List<purchase> pur = new LinkedList<purchase>();
                            p.setApproveCode(res.getString("approveCode"));
                            p.setTraceNo(res.getString("traceNo"));
                            p.setSystemRef(res.getString("systemRef"));
                            p.setAmount(res.getString("amount"));
                            p.setTransTime(res.getString("transTime"));
                            p.setTransDate(res.getString("transDate"));
                            p.setRespondCode(res.getString("respondCode"));
                            p.setCode("EZ910");
                            res.put("msg", "Success");
                            pur.add(p);

                            old.setTrace(pur);
                            old.setStatus(CANCELLED);
                            old.setTraceNo(traceNo);
                            res.put("code", "EZ910");
                            old.setResponse(res.toString());
                            update(old);
                        } else {
                            System.out.println("FAIL");
                            old.setStatus(FAIL);
                            old.setResponse("{'code':'EZ902','msg':'Амжилтгvй !'}");
                            update(old);
                        }
                    }
                }
                System.out.println("PAYMENT END");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return old;
    }

    public String tokenFull(token entity) {
        String token = entity.getToken();
        if (token.length() == BARCODE13) {
            getSession();
            session.getTransaction().begin();
            try {
                Query query = session.getNamedQuery("miniToken");
                query.setParameter("time_out", new Integer(RESPONDING_TIME));
                List<token> list = query.list();
                for (int i = 0; i < list.size(); i++) {
                    token t = list.get(i);
                    if (t.getStatus() == NEW) {
                        String moken = miniToken(t.getToken());
                        if (token.equals(moken)) {
                            token = t.getToken();
                            break;
                        }
                    } else {
                        if (t.getMerchantData().equals(entity.getMerchantData())
                                || t.getWalletId().equals(entity.getWalletId())) {
                            String moken = miniToken(t.getToken());
                            if (token.equals(moken)) {
                                token = t.getToken();
                                break;
                            }
                        }
                    }
                }
                session.getTransaction().commit();
            } catch (Exception ex) {
                session.getTransaction().rollback();
            } finally {
                close();
            }
        }

        return token;
    }

    public String miniToken(String token) {
        String moken = "";
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if (c >= '0' && c <= '9')
                moken += c;

            if (moken.length() >= BARCODE13) break;
        }
        return moken;
    }
}
