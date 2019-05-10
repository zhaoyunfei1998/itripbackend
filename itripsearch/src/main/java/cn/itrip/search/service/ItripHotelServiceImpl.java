package cn.itrip.search.service;

import cn.itrip.beans.vo.hotel.ItripHotelVO;
import cn.itrip.beans.vo.hotel.SearchHotCityVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.search.dao.ItripHotelMapperImpl;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("itripHotelService")
public class ItripHotelServiceImpl implements ItripHotelService{
    /**
     * solr地址
     * 用于查询酒店信息
     */
    private String url="http://localhost:8082/solr/hotel";
    ItripHotelMapperImpl baseDao;
    SolrQuery solrQuery;

    /**
     *
     * @param vo 查询热门酒店vo
     * @return
     * @throws Exception
     */
    @Override
    public List<ItripHotelVO> getHotelList(SearchHotCityVO vo) throws Exception{
        baseDao=new ItripHotelMapperImpl<ItripHotelVO>(url);
        solrQuery=new SolrQuery();
        solrQuery.setQuery("cityId:"+vo.getCityId());
        List<ItripHotelVO> list=baseDao.query(solrQuery,vo.getCount(),ItripHotelVO.class);
        return list;
    }

    /**
     *
     * @param vo 酒店分页vo
     * @return
     * @throws Exception
     */
    @Override
    public Page<ItripHotelVO> getHotelListPage(SearchHotelVO vo) throws Exception {
        baseDao=new ItripHotelMapperImpl<ItripHotelVO>(url);
        solrQuery=new SolrQuery();
        StringBuffer buffer=new StringBuffer();
        int num=0;
        if(EmptyUtils.isNotEmpty(vo.getDestination())){
            //目的地
            buffer.append("destination:"+vo.getDestination());
            num++;
        }
        StringBuffer buffer1=new StringBuffer();
        if(EmptyUtils.isNotEmpty(vo.getFeatureIds())){
            String[] featureIds=vo.getFeatureIds().split(",");
            int count=0;
            for (String fid:featureIds) {
                if(count==0) {
                    buffer1.append("featureIds:*," + fid + ",*");
                }else{
                    buffer1.append(" OR featureIds:*," + fid + ",*");
                }
                count++;
                solrQuery.addFacetQuery(buffer1.toString());
            }
        }
        if(EmptyUtils.isNotEmpty(vo.getHotelLevel())){
            if(num==0) {
                buffer.append("hotelLevel:" + vo.getHotelLevel());
            }else{
                buffer.append(" AND hotelLevel:" + vo.getHotelLevel());
            }
        }
        if(EmptyUtils.isNotEmpty(vo.getKeywords())){
            if(num==0) {
                buffer.append("keywords:" + vo.getKeywords());
            }else{
                buffer.append(" AND keyword:" + vo.getKeywords());
            }
        }
        if(EmptyUtils.isNotEmpty(vo.getMinPrice())){
            if(num==0) {
                buffer.append("minPrice:["+vo.getMinPrice()+" TO " + vo.getMaxPrice()+"]");
            }else{
                buffer.append(" AND minPrice:["+vo.getMinPrice()+" TO " + vo.getMaxPrice()+"]");
            }
            num++;
        }
        if(EmptyUtils.isNotEmpty(vo.getAscSort())){
            solrQuery.addSort(vo.getAscSort(), SolrQuery.ORDER.asc);
        }
        if(EmptyUtils.isNotEmpty(vo.getDescSort())){
            solrQuery.addSort(vo.getDescSort(), SolrQuery.ORDER.desc);
        }
        solrQuery.setQuery(buffer.toString());
        Page<ItripHotelVO> list=baseDao.queryPage(solrQuery,ItripHotelVO.class,vo.getPageNo(),vo.getPageSize());
        return list;
    }
}
