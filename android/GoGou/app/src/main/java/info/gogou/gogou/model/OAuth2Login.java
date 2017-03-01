package info.gogou.gogou.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by user on 2016/4/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuth2Login extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String errcode;

    private String errmsg;

    private String accessToken;

    private String refreshToken;

    private String nickname;

    private String gender;

    private String headimgurl;

    private byte fileOutput[];

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public byte[] getFileOutput() {
        return fileOutput;
    }

    public void setFileOutput(byte fileOutput[]) {
        this.fileOutput = fileOutput;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getHeadimgurl() {
        return headimgurl;
    }


    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

}
