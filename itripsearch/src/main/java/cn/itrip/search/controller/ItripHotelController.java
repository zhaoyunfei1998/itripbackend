package cn.itrip.search.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.vo.hotel.ItripHotelVO;
import cn.itrip.beans.vo.hotel.SearchHotCityVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.search.service.ItripHotelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api/hotellist")
public class ItripHotelController {
    @Resource
    ItripHotelService itripHotelService;

    @RequestMapping(value = "searchItripHotelListByHotCity",method = RequestMethod.POST)
    @ResponseBody
    public Dto<List<ItripHotelVO>> searchItripHotelListByHotCity(@RequestBody SearchHotCityVO vo){
        if(EmptyUtils.isEmpty(vo.getCityId())){
            return DtoUtil.returnFail("城市Id不能为空","20004");
        }
        List<ItripHotelVO> list = null;
        try {
            list =itripHotelService.getHotelList(vo);
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败","20003");
        }
        return DtoUtil.returnDataSuccess(list);
    }
    @RequestMapping(value ="searchItripHotelPage" ,method = RequestMethod.POST)
    @ResponseBody
    public Dto<Page<ItripHotelVO>>  searchItripHotelPage(@RequestBody SearchHotelVO vo){
        if(EmptyUtils.isEmpty(vo.getDestination())){
            return DtoUtil.returnFail("目的地不能为null","20002");
        }
        Page<ItripHotelVO> list=null;
        try {
            list=itripHotelService.getHotelListPage(vo);
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败","20001");
        }

        return DtoUtil.returnDataSuccess(list);
    }
}
