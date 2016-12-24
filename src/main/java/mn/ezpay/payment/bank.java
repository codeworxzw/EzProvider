package mn.ezpay.payment;

public class bank {
    public String url;
    public String port;
    public String mode;
    public String UAT;

    public bank() {

    }

    public bank(String u, String p, String m, String a) {
        url = u;
        port = p;
        mode = m;
        UAT = a;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUAT() {
        return UAT;
    }

    public void setUAT(String UAT) {
        this.UAT = UAT;
    }

    @Override
    public String toString() {
        return "bank{" +
                "url='" + url + '\'' +
                ", port='" + port + '\'' +
                ", mode='" + mode + '\'' +
                ", UAT='" + UAT + '\'' +
                '}';
    }
}
