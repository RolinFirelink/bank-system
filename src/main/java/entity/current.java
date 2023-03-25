package entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 活期存款账目表
 */
public class current {

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

    public current() {
    }

    public current(String id, String bankId, String userId, BigDecimal decimal) {
        this.id = id;
        this.bankId = bankId;
        this.userId = userId;
        this.decimal = decimal;
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
}
