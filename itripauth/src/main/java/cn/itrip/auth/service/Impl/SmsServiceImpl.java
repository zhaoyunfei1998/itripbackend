package cn.itrip.auth.service.Impl;

import cn.itrip.auth.service.SmsService;
import com.cloopen.rest.sdk.CCPRestSDK;
import org.springframework.stereotype.Service;

import java.util.HashMap;
@Service("SmsService")
public class SmsServiceImpl implements SmsService{
    @Override
    public void send(String phone, String temId, String[] smsNum) {
        HashMap<String, Object> result = null;
        CCPRestSDK restAPI = new CCPRestSDK();
        restAPI.init("app.cloopen.com", "8883");
        // 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
        restAPI.setAccount("8aaf07086a43ad03016a58b1f1c718f1", "ad5250c5433442619218beff52178d09");
        // 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在控制首页中看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN。
        restAPI.setAppId("8aaf07086a43ad03016a58b1f21918f7");
        // 请使用管理控制台中已创建应用的APPID。
        result = restAPI.sendTemplateSMS(phone,temId ,smsNum);
        System.out.println("SDKTestGetSubAccounts result=" + result);
        if(!"000000".equals(result.get("statusCode"))){
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
    }
}
