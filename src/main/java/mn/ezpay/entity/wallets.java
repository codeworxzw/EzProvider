package mn.ezpay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "activisionQuery",
                query = "SELECT * FROM wallets WHERE walletId=:walletId and status=:status and pin=:pin",
                resultClass = wallets.class
        ),
        @NamedNativeQuery(
                name = "removeWallet",
                query = "DELETE FROM wallets WHERE walletId=:walletId and status=:status",
                resultClass = wallets.class
        )
})
@Entity
public class wallets implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String walletId;
    @Column
    private String _date;
    @Column
    private String status;
    @Column
    private String deviceName;
    @Column
    private String pin;

    @OneToMany(mappedBy = "cards", cascade = CascadeType.ALL)
    private List<cards> cards;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<cards> getCards() {
        return cards;
    }

    public void setCards(List<cards> cards) {
        this.cards = cards;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
