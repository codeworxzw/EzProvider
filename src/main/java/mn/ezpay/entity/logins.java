package mn.ezpay.entity;

import javax.persistence.*;

@Entity
public class logins implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String qr_data;
    @Column
    private String status;
    @Column
    private String walletId;
    @Column
    private String _date;
    @Column
    private String logged;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQr_data() {
        return qr_data;
    }

    public void setQr_data(String qr_data) {
        this.qr_data = qr_data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String getLogged() {
        return logged;
    }

    public void setLogged(String logged) {
        this.logged = logged;
    }
}
