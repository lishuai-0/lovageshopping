package top.bug1024.lovageshopping.common.response;

import lombok.Data;

import java.util.List;

@Data
public class BaseResponse <T>{
    private String message;
    private int code;
    private T data;
}
