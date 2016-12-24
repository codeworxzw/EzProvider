package mn.ezpay.entity;

import javax.persistence.*;

@Entity
public class trace implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String terminalId;
    @Column
    private String merchantId;
    @Column
    private String batchNo;
    @Column
    private int traceNo;

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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public int getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(int traceNo) {
        this.traceNo = traceNo;
    }
}
