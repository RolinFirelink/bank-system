package entity;

public class Bank {
    /**
     * 唯一主键
     */
    private String id;

    /**
     * 银行名
     */
    private String name;

    public Bank() {
    }

    public Bank(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
