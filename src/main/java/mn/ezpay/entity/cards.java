package mn.ezpay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class cards implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String enc;
    @Column
    private String status;
    @Column
    private String walletId;
    @Column
    private String ppin;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "walletId", referencedColumnName = "walletId", insertable = false, updatable = false)
    private wallets cards;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnc() {
        return enc;
    }

    public void setEnc(String enc) {
        this.enc = enc;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPpin() {
        return ppin;
    }

    public void setPpin(String ppin) {
        this.ppin = ppin;
    }
}
