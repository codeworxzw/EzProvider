package mn.ezpay.entity;

import javax.persistence.*;

@Entity
public class terminal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String terminalId;
    @Column
    private String bankMerchantId;
    @Column
    private String merchantId;

    @ManyToOne
    @JoinColumn(name = "merchantId", referencedColumnName = "merchantId", insertable = false, updatable = false)
    private merchant merchants;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getBankMerchantId() {
        return bankMerchantId;
    }

    public void setBankMerchantId(String bankMerchantId) {
        this.bankMerchantId = bankMerchantId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
