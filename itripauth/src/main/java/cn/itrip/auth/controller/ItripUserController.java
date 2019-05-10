package cn.itrip.auth.controller;

import cn.itrip.auth.service.ItripUserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.ItripTokenVO;
import cn.itrip.beans.vo.userinfo.ItripUserVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.ErrorCode;
import cn.itrip.common.TokenUtil;
import cn.itrip.common.UrlUtils;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/api/")
@Api(tags = "用户管理Api")
public class ItripUserController {
    @Resource
    private ItripUserService userService;
    @Resource
    private TokenUtil tokenUtil;
    final String backUrl = "http://172.30.202.79:8083/api/backUrl";
    /**
     * 用户登录
     *
     * @return
     */
    @ApiOperation(value = "用户登录接口",response = Dto.class)
    @RequestMapping(value="dologin",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Dto listItripUser(HttpServletRequest request, @RequestParam("name" )String name, @RequestParam("password") String password){
        ItripUser itripUser=userService.login(name,password);
        if (itripUser!=null){
            //获取客户端浏览器信息
            String token=tokenUtil.getToken(request.getHeader("user-agent"),itripUser);
            //保存到redis
            Date date =new Date();
            long time=date.getTime()/1000;
            long outTime=time+2*1000*60*60;
            tokenUtil.save(token,itripUser);
            ItripTokenVO itripTokenVO=new ItripTokenVO();
            itripTokenVO.setToken(token);
            itripTokenVO.setGenTime(time);
            itripTokenVO.setExpTime(outTime);
           return  DtoUtil.returnSuccess("登录成功",itripTokenVO);
        }else{
           return  DtoUtil.returnFail("用户名或者密码错误", ErrorCode.AUTH_PARAMETER_ERROR);
        }
    }

    /**
     * 用户注销
     * @return
     */
    @RequestMapping(value = "logout",method = RequestMethod.GET)
    @ResponseBody
    public Dto logout(HttpServletRequest request){
        String token=request.getHeader("token");
        System.out.print(token);
        boolean flag=true;
        if(token!=null||token.equals("")){
            flag=tokenUtil.ckeckToken(token);
            tokenUtil.deleteToken(token);
            return DtoUtil.returnSuccess();
        }
        return DtoUtil.returnFail("退出失败",ErrorCode.AUTH_TOKEN_INVALID);
    }
    /**
     * 手机号注册
     * @param itripUserVO
     * @return
     */
    @ApiOperation(value = "手机号注册接口",response = Dto.class)
    @RequestMapping(value = "registerbyphone",method = RequestMethod.POST)
    @ResponseBody
    public Dto registerByPhone(@RequestBody ItripUserVO itripUserVO){
        //获得用户信息
         System.out.println(itripUserVO.getUserCode());
        //插入用户
        int i=userService.registerByPhone(itripUserVO);
        if(i==1){
            //返回结果
            return DtoUtil.returnSuccess();
        }
        return DtoUtil.returnFail("手机注册失败",ErrorCode.AUTH_AUTHENTICATION_FAILED);
    }

    /**
     * 手机号注册验证
     * @return
     */
    @ApiOperation(value = "手机号验证",response = Dto.class)
    @RequestMapping(value = "validatephone",method = RequestMethod.PUT)
    @ResponseBody
    public Dto validatephone(String user,String code){
        int i=userService.updateByItripUser(user,code);
        if(i==1){
            return DtoUtil.returnSuccess();
        }
        return DtoUtil.returnFail("验证码输入有误",ErrorCode.AUTH_AUTHENTICATION_FAILED);
    }

    /**
     * 微博登录
     */
    @ApiOperation(value = "微博登录")
    @RequestMapping("weibo/login")
    public void weibo(HttpServletResponse response)throws Exception{
        String url="https://api.weibo.com/oauth2/authorize?client_id=1788725870"+
                "&response_type=code"+
                "&redirect_uri="+URLEncoder.encode(backUrl,"utf-8");
        response.sendRedirect(url);
    }

    @RequestMapping("backUrl")
    public String setBackUrl(HttpServletRequest request, Model model)throws UnsupportedEncodingException {
        String url = "https://api.weibo.com/oauth2/access_token" +
                "?client_id=1788725870" +
                "&client_secret=5d07863fee9b7251ee134039685d6f87" +
                "&grant_type=authorization_code" +
                "&redirect_uri="+URLEncoder.encode(backUrl,"utf-8")+
                "&code="+request.getParameter("code");

        String result = UrlUtils.loadURL(url);
        Map<String,String> map = JSON.parseObject(result,Map.class);
        System.out.print(map);
        // 检查用户是否已经创建
        String weibo=map.get("uid");
        //System.out.print(weibo);
        int errorCode=0;
        ItripUser itripUser=null;
        if(userService.getItripUserByWeibo(weibo)==null){
            // 创建用户
            itripUser=new ItripUser();
            itripUser.setUserType(3);
            itripUser.setFlatID(Long.valueOf(weibo));
            itripUser.setWeibo(weibo);
            itripUser.setCreationDate(new Date());
            itripUser.setActivated(0);
            if(userService.addWeiboUser(itripUser)==1){
                    errorCode=1;
            }
        }else{
            errorCode=1;
            itripUser=userService.getItripUserByWeibo(weibo);
        }
        if(errorCode==1){
            // 创建token，保存Redis
            tokenUtil.save(map.get("access_token"),itripUser);
        }
        // 重定向返回前端页面
        return "index";
    }


}