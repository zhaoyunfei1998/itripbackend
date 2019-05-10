package cn.itrip.auth.service;

import cn.itrip.beans.pojo.*;
import cn.itrip.beans.vo.userinfo.ItripUserVO;

import java.util.List;

public interface ItripUserService {
    /*
     * 查询用户列表
     * @return
     */
    public List<ItripUser> listItripUser();

    /*
     *
     * @param userName
     * @return
     */
    public ItripUser login(String name,String password);

    /**
     * 手机号注册
     * @param user
     * @return
     */
    public int registerByPhone(ItripUserVO user);

    /**
     * 修改用户信息
     * @return
     */
    public int updateByItripUser(String user,String code);

    /**
     * 根据微博查询用户信息
     * @param weibo
     * @return
     */
    public ItripUser getItripUserByWeibo(String weibo);

    public int addWeiboUser(ItripUser itripUser);
}
