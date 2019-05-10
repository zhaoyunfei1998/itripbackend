package cn.itrip.auth.service.Impl;

import cn.itrip.auth.service.ItripUserService;
import cn.itrip.auth.service.SmsService;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.userinfo.ItripUserVO;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import cn.itrip.dao.itripauth.ItripUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.util.Date;
import java.util.List;
@Service("ItripUserService")
public class ItripUserServiceImpl implements ItripUserService {
    @Resource
    private ItripUserMapper itripUserMapper;
    @Resource
    private SmsService smsService;
    @Resource
    private RedisAPI redisAPI;
    /**
     * 查询用户列表
     * @return
     */
    @Override
    public List<ItripUser> listItripUser() {
        return itripUserMapper.listItripUser();
    }

    @Override
    public ItripUser login(String name,String password) {
        //根据前端传过来的密码生成32位暗码
        password=MD5.getMd5(password,32);
        /*
            查询数据库是否存在此昵称
            1，如果存在，则判断密码是否正确
            2.不存在返回null
         */
        ItripUser itripUser=itripUserMapper.getItripUserByUserName(name);
        if(itripUser!=null){
            if(itripUser.getUserPassword().equals(password)){
                return itripUser;
            }
        }
        return null;
    }

    @Override
    public int registerByPhone(ItripUserVO user) {
        //手机号注册
        //创建用户
        user.setUserPassword(MD5.getMd5(user.getUserPassword(),32));
        int i=itripUserMapper.addItripUser(user);
        //生成验证码
        if(i>0){
            int code=MD5.getRandomCode();
            String[] sms={String.valueOf(code),"1"};
            //发送验证码
           /* smsService.send(user.getUserCode(),"1",sms);*/
            //将验证码保存到redis
            redisAPI.set(user.getUserCode(),300,code+"");
            /*"activation"*/
            return 1;
        }
        return 0;
    }
    /**
     *修改用户信息
     * 1.验证验证码是否输入正确
     * 2，验证码输入正确则修改用户激活状态为1
     */
    public int updateByItripUser(String user,String code){
        if((redisAPI.get(user))!=null){
            System.out.print(redisAPI.get(user));
            if(redisAPI.get(user).equals(code)){
                ItripUser itripUser=new ItripUser();
                itripUser.setActivated(1);
                itripUser.setCreationDate(new Date());
                itripUser.setUserCode(user);
                itripUser.setUserType(0);
                return itripUserMapper.updateItripUserByUserCode(itripUser);
            }
        }
        return 0;
    }
    public ItripUser getItripUserByWeibo(String weibo){
        return itripUserMapper.getItripUserByWeibo(weibo);
    }

    public int addWeiboUser(ItripUser itripUser){
        return itripUserMapper.addWeiboUser(itripUser);
    }
}
