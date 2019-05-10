package cn.itrip.search.service;

import cn.itrip.beans.vo.hotel.ItripHotelVO;
import cn.itrip.beans.vo.hotel.SearchHotCityVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.Page;

import java.util.List;

public interface ItripHotelService {
    /**
     * 根据城市Id查询热门酒店
     * @param vo
     * @return
     * @throws Exception
     */
    public List<ItripHotelVO> getHotelList(SearchHotCityVO vo) throws Exception;

    /**
     * 酒店分页查询
     * @param vo
     * @return
     * @throws Exception
     */
    public Page<ItripHotelVO> getHotelListPage(SearchHotelVO vo) throws Exception;
}
