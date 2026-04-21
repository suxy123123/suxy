package org.sfc.sfc.comon;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> ok(T data){
        Result result = new Result<>();
        result.setCode(200);
        result.setData(data);
        result.setMsg("success");
        return result;
    }
    public static <T> Result<T> ok(String msg){
        Result result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> error(String msg){
        Result result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
    public static <T> Result<T> error(int code,String msg){
        Result result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
