package com.imooc.ad.search;

import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 15:45
 * @Description:
 */
public interface ISearch {

    SearchResponse fetchAds(SearchRequest request);
}
