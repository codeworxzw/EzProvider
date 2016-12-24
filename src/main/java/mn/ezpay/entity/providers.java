package mn.ezpay.entity;

import javax.persistence.*;

@Entity
public class providers implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String name;
    @Column
    private String _date;
    @Column
    private String ico;
    @Column
    private String type;
    @Column
    private int max_len;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxlen() {
        return max_len;
    }

    public void setMaxlen(int max_len) {
        this.max_len = max_len;
    }
}
