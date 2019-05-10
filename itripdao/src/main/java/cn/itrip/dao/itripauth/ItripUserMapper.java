package cn.itrip.dao.itripauth;

import cn.itrip.beans.pojo.*;
import cn.itrip.beans.vo.userinfo.ItripUserVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ItripUserMapper {

    /*
     * 查询用户列表
     * @return
     */
    public List<ItripUser> listItripUser();

    /*
     *根据用户名查询用户信息
     */
    public ItripUser getItripUserByUserName(@Param("userCode")String userCode);

    /**
     * 添加用户
     * @return
     */
    public int addItripUser(ItripUserVO itripUserVO);

    /**
     * 修改用户
     * @param itripUser
     * @return
     */
    public int updateItripUserByUserCode(ItripUser itripUser);

    /**
     * 根据微博查询用户
     * @param weibo
     * @return
     */
    public ItripUser getItripUserByWeibo(String weibo);

    /**
     * 添加微博用户
     * @param itripUser
     * @return
     */
    public int addWeiboUser(ItripUser itripUser);
}