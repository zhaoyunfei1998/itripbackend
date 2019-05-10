package cn.itrip.common;

import cn.itrip.beans.pojo.ItripUser;
import com.alibaba.fastjson.JSONObject;
import cz.mallat.uasparser.UserAgentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TokenUtil {

    @Autowired
    public RedisAPI redisAPI;

    private final String PREFIX = "token";
    public  final int SESSION_TIMEOUT=2*60*60;//默认2h

    // 获取Token
    public String getToken(String agent, ItripUser user){
        try {
            UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(
                    agent);
            StringBuilder sb = new StringBuilder();
            sb.append(PREFIX);//统一前缀
            if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
                if (UserAgentUtil.CheckAgent(agent)) {
                    sb.append("MOBILE-");
                } else {
                    sb.append("PC-");
                }
            } else if (userAgentInfo.getDeviceType()
                    .equals("Personal computer")) {
                sb.append("PC-");
            } else
                sb.append("MOBILE-");
//			sb.append(user.getUserCode() + "-");
            sb.append(MD5.getMd5(user.getUserCode(),32) + "-");//加密用户名称
            sb.append(user.getId() + "-");
            sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + "-");
            sb.append(MD5.getMd5(agent, 6));// 识别客户端的简化实现——6位MD5码

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String token,ItripUser user){
        if(token.startsWith(PREFIX+"-PC")){
            redisAPI.set(token,SESSION_TIMEOUT,JSONObject.toJSONString(user));
        }else{
            redisAPI.set(token,JSONObject.toJSONString(user));
        }
    }

    /**
     * 验证Token是否存在
     * @param token
     * @return
     */
    public boolean ckeckToken(String token){
        if(redisAPI.get(token)=="1"){
            return true;
        }
        return false;
    }
    /**
     *
     * 删除Token
     * @param token
     */
    public void deleteToken(String token){
        redisAPI.delete(token);
    }
}
