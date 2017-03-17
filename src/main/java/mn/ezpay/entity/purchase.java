package mn.ezpay.entity;

import javax.persistence.*;

/**
 * Created by User on 12/11/2016.
 */
@Entity
public class purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String traceNo;
    @Column
    private String systemRef;
    @Column
    private String approveCode;
    @Column
    private String amount;
    @Column
    private String transTime;
    @Column
    private String transDate;
    @Column
    private String respondCode;
    @Column
    private String code;
    @Column
    private String msg;

    @ManyToOne
    @JoinColumn(name = "traceNo", referencedColumnName = "traceNo", insertable = false, updatable = false)
    private token token;

    @ManyToOne
    @JoinColumn(name = "traceNo", referencedColumnName = "oldTraceNo", insertable = false, updatable = false)
    private token tokenOld;

    public String getSystemRef() {
        return systemRef;
    }

    public void setSystemRef(String systemRef) {
        this.systemRef = systemRef;
    }

    public String getApproveCode() {
        return approveCode;
    }

    public void setApproveCode(String approveCode) {
        this.approveCode = approveCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getRespondCode() {
        return respondCode;
    }

    public void setRespondCode(String respondCode) {
        this.respondCode = respondCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public mn.ezpay.entity.token getToken() {
        return token;
    }

    public void setToken(mn.ezpay.entity.token token) {
        this.token = token;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
