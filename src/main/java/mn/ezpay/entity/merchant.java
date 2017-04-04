package mn.ezpay.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class merchant implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String merchantId;
    @Column
    private String name;
    @Column
    private String phone;

    @OneToMany(mappedBy = "merchants", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<terminal> terminalList;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<terminal> getTerminalList() {
        return terminalList;
    }

    public void setTerminalList(List<terminal> terminalList) {
        this.terminalList = terminalList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
