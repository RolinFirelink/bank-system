package entity;

import java.util.Objects;

public class register {
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
     * 开户登记号
     */
    private String accountRegistration;

    public register() {
    }

    public register(String id, String bankId, String userId, String accountRegistration) {
        this.id = id;
        this.bankId = bankId;
        this.userId = userId;
        this.accountRegistration = accountRegistration;
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

    public String getAccountRegistration() {
        return accountRegistration;
    }

    public void setAccountRegistration(String accountRegistration) {
        this.accountRegistration = accountRegistration;
    }
}
