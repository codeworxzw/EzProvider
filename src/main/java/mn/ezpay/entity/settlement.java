package mn.ezpay.entity;

import javax.persistence.*;
@Table(name = "settlement")
@Entity
public class settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String merchantId;
    @Column
    private String merchantData;
    @Column
    private double amount;
    @Column
    private int count;
    @Column
    private String _date;
    @Column
    private String _day;
    @Column
    private String respondCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String getRespondCode() {
        return respondCode;
    }

    public void setRespondCode(String respondCode) {
        this.respondCode = respondCode;
    }

    public String getMerchantData() {
        return merchantData;
    }

    public void setMerchantData(String merchantData) {
        this.merchantData = merchantData;
    }

    public String get_day() {
        return _day;
    }

    public void set_day(String _day) {
        this._day = _day;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
