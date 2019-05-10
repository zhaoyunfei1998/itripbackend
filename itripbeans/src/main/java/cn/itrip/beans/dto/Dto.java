package cn.itrip.beans.dto;

import org.springframework.stereotype.Component;

/**
 * 前后端交互
 * @param <T>
 */
@Component
public class Dto<T> {
    private String success;//判断系统是否出错做出相应的true或者false返回，与业务无关，出现的各种异常
    private String errorCode;//该错误码为自定义，一般0表示无措
    private String msg;//对应的提示信息
    private T data;//具体返回数据内容(pojo、自定义vo、其他)

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
