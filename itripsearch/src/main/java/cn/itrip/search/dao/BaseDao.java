package cn.itrip.search.dao;

import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import java.util.List;

public class BaseDao<T> {

    private HttpSolrClient httpSolrClient=null;
    public BaseDao(String url){
        httpSolrClient=new HttpSolrClient(url);
        //设置解析器
        httpSolrClient.setParser(new XMLResponseParser());
    }

    /**
     *根据城市Id查询热门酒店
     * @param solrQuery 查询条件
     * @param count 条数
     *              1.判断前端传过来的是否为0或者null
     *              2.是则赋值为3，否则按count查询
     * @param clazz 类型
     * @return
     * @throws Exception
     */
    public List<T> query(SolrQuery solrQuery, Integer count, Class clazz)throws Exception{
        //设置连接的最长时间
        /*httpSolrClient.setConnectionTimeout(500);*/
        count= EmptyUtils.isEmpty(count)||count<=0 ? 3 :count;
        solrQuery.setStart(0);
        solrQuery.setRows(count);
        QueryResponse queryResponse=httpSolrClient.query(solrQuery);
        List<T> hotelVOList=queryResponse.getBeans(clazz);
        return hotelVOList;
    }

    /**
     *分页查询酒店
     * @param solrQuery 查询条件
     * @param clazz 类型
     * @param pageNo 查询起始页
     * @param pageSize 查询条数
     * @return
     * @throws Exception
     */
    public Page<T> queryPage(SolrQuery solrQuery,Class clazz,Integer pageNo,Integer pageSize) throws Exception{
        pageNo=EmptyUtils.isEmpty(pageNo)||pageNo<=0 ? 1 : pageNo;
        pageSize=EmptyUtils.isEmpty(pageSize) ? 10:pageSize;
        solrQuery.setStart((pageNo-1)*pageSize);
        solrQuery.setRows(pageSize);
        QueryResponse queryResponse=httpSolrClient.query(solrQuery);
        List<T> list=queryResponse.getBeans(clazz);
        //查询总数
        Page<T> page=new Page<>(pageNo,pageSize,new Long(queryResponse.getResults().getNumFound()).intValue());
        page.setRows(list);
        return page;
    }
}
