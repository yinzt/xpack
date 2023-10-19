package cn.com.xcsa.xpack.third;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wuhui
 */
@Data
public class AccessToken implements Serializable {

    private String accessToken;

    private Integer expireIn;

    public AccessToken(String at, Integer ei) {
        this.accessToken = at;
        this.expireIn = ei;
    }
}
