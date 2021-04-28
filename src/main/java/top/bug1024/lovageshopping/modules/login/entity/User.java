package top.bug1024.lovageshopping.modules.login.entity;


import java.io.Serializable;
import java.util.Date;


import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 2090714647038636896L;
    @TableId
    private int userId;
    private String nickName;
    private String realName;
    private String userMail;
    private String loginPassword;
    private String payPassword;
    private String userMobile;
    private Date modifyTime;
    private Date userRegtime;
    private String userRegip;
    private Date userLasttime;
    private String userLastip;
    private String userMemo;
    private String sex;
    private String birthDate;
    private String pic;
    private Integer status;
    private Integer score;

    public User(String userMobile, String loginPassword, String payPassword, Date modifyTime, Date userRegtime) {
        this.userMobile = userMobile;
        this.loginPassword = loginPassword;
        this.payPassword = payPassword;
        this.modifyTime = modifyTime;
        this.userRegtime = userRegtime;
    }
}
