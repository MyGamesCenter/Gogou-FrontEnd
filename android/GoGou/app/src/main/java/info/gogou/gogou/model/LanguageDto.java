package info.gogou.gogou.model;

import java.io.Serializable;

/**
 * Created by grace on 16-3-17.
 */
public class LanguageDto extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String username;

    private Integer sortOrder;

    public String getCode() {
        return code;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
