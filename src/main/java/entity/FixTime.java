package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FixTime {

    /**
     * 唯一主键
     */
    private String id;

    /**
     * 银行id
     */
    private String bankId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 存款金额
     */
    private BigDecimal decimal;

    /**
     * 存款时间过后用户应得的金额
     */
    private BigDecimal allMoney;

    /**
     * 定期存款时间
     */
    private String days;

    public FixTime() {
    }

    public FixTime(String id, String bankId, String userId, BigDecimal decimal, BigDecimal allMoney, String days) {
        this.id = id;
        this.bankId = bankId;
        this.userId = userId;
        this.decimal = decimal;
        this.allMoney = allMoney;
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    public BigDecimal getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(BigDecimal allMoney) {
        this.allMoney = allMoney;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "FixTime{" +
                "id='" + id + '\'' +
                ", bankId='" + bankId + '\'' +
                ", userId='" + userId + '\'' +
                ", decimal=" + decimal +
                ", allMoney=" + allMoney +
                ", days='" + days + '\'' +
                '}';
    }
}
