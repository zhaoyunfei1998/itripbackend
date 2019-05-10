package cn.itrip.search.dao;

import cn.itrip.common.Page;
import org.apache.solr.client.solrj.SolrQuery;
import java.util.List;

public class ItripHotelMapperImpl<T> extends BaseDao{

    public ItripHotelMapperImpl(String url) {
        super(url);
    }

    /**
     * 根据城市Id查询热门酒店
     * @param solrQuery 查询条件
     * @param count 查询条数
     * @param clazz 类型
     * @return
     * @throws Exception
     */
    public List<T> getHotelList(SolrQuery solrQuery, Integer count, Class clazz)throws Exception{
        return this.query(solrQuery,count,clazz);
    }

    /**
     * 酒店分页查询
     * @param solrQuery 查询条件
     * @param clazz 类型
     * @param pageNo 查询起始位置
     * @param pageSize 查询条数
     * @return
     * @throws Exception
     */
    public Page<T> getHotelListPage(SolrQuery solrQuery,Class clazz,Integer pageNo,Integer pageSize)throws Exception{
        return this.queryPage(solrQuery,clazz,pageNo,pageSize);
    }
}
