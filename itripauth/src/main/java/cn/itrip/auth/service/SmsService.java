package cn.itrip.auth.service;

public interface SmsService {
    /**
     * 发送手机验证
     * @param phone 手机号
     * @param temId 模板Id
     * @param smsNum 生成的随机验证码和过期时间
     */
    public void send(String phone,String temId,String[] smsNum);
}
