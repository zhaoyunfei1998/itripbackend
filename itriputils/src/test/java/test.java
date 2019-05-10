
import cn.itrip.beans.vo.hotel.ItripHotelVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.List;

public class test {
    public static void main(String args[])throws Exception{
        String url="http://localhost:8082/solr/collection1";
        HttpSolrClient httpSolrClient=new HttpSolrClient(url);
        //设置解析器
        httpSolrClient.setParser(new XMLResponseParser());
        //设置连接的最长时间
        httpSolrClient.setConnectionTimeout(500);
        SolrQuery solrQuery=new SolrQuery();
        solrQuery.setQuery("address:北京");
        QueryResponse queryResponse=httpSolrClient.query(solrQuery);
        List<ItripHotelVO> hotelVOList=queryResponse.getBeans(ItripHotelVO.class);
        for (ItripHotelVO hotel:hotelVOList) {
            System.out.println(hotel.getHotelName());
        }

    }
}
