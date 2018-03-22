package cn.xuhao.demo.paymentserver.util;

import java.beans.ConstructorProperties;

public class Result<T> {
    private int status;
    private int ecode;
    private String msg;
    private T content;

    public Result(ErrorCodeEnum codeEnum, T content) {
        this.status = codeEnum.getCode();
        this.msg = codeEnum.getMessage();
        this.content = content;
    }

    public Result(int code, String msg, T content) {
        this.status = code;
        this.ecode = code;
        this.msg = msg;
        this.content = content;
    }

    public Result(Integer code, Integer ecode, String msg, T content) {
        this.status = code.intValue();
        this.ecode = ecode.intValue();
        this.msg = msg;
        this.content = content;
    }

    public int getStatus() {
        return this.status;
    }

    public int getEcode() {
        return this.ecode;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getContent() {
        return this.content;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setEcode(int ecode) {
        this.ecode = ecode;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean equals(Object o) {
        if(o == this) {
            return true;
        } else if(!(o instanceof Result)) {
            return false;
        } else {
            Result<?> other = (Result)o;
            if(!other.canEqual(this)) {
                return false;
            } else if(this.getStatus() != other.getStatus()) {
                return false;
            } else if(this.getEcode() != other.getEcode()) {
                return false;
            } else {
                label40: {
                    Object this$msg = this.getMsg();
                    Object other$msg = other.getMsg();
                    if(this$msg == null) {
                        if(other$msg == null) {
                            break label40;
                        }
                    } else if(this$msg.equals(other$msg)) {
                        break label40;
                    }

                    return false;
                }

                Object this$content = this.getContent();
                Object other$content = other.getContent();
                if(this$content == null) {
                    if(other$content != null) {
                        return false;
                    }
                } else if(!this$content.equals(other$content)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Result;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getStatus();
        result = result * 59 + this.getEcode();
        Object $msg = this.getMsg();
        result = result * 59 + ($msg == null?43:$msg.hashCode());
        Object $content = this.getContent();
        result = result * 59 + ($content == null?43:$content.hashCode());
        return result;
    }

    public String toString() {
        return "Result(status=" + this.getStatus() + ", ecode=" + this.getEcode() + ", msg=" + this.getMsg() + ", content=" + this.getContent() + ")";
    }

    @ConstructorProperties({"status", "ecode", "msg", "content"})
    public Result(int status, int ecode, String msg, T content) {
        this.status = status;
        this.ecode = ecode;
        this.msg = msg;
        this.content = content;
    }
}