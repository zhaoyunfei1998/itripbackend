package cn.itrip.auth.exception;

public class TokenValidationFailedException extends  Exception {

    public TokenValidationFailedException(String msg) {
        super(msg);
    }
}
