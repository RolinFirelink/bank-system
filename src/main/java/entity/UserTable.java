package entity;

import java.math.BigDecimal;

public class UserTable {
    /**
     * 唯一主键
     */
    private String id;
    /**
     * 银行卡号
     */
    private String account;

    /**
     * 密码,长度为6位
     */
    private String password;

    /**
     * 用户金额
     */
    private BigDecimal decimal;

    public UserTable() {
    }

    public UserTable(String id, String account, String password, BigDecimal decimal) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.decimal = decimal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    @Override
    public String toString() {
        return "UserTable{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", decimal=" + decimal +
                '}';
    }
}
