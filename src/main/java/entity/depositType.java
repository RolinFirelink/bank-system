package entity;

/**
 * 存款类型代码表
 * 由于完全用不上但是课程又有要求因此创建该实体类但在项目中不做使用
 */
public enum depositType {

    CURRENT("活期存款",200),
    FIX_TIME("定期存款",400);

    /**
     * 存款类型名
     */
    private String depositName;

    /**
     * 存款类型代码
     */
    private Integer code;

    depositType(String depositName, Integer code) {
        this.depositName = depositName;
        this.code = code;
    }
}
