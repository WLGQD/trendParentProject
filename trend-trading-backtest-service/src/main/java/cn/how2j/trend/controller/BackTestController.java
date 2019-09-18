package cn.how2j.trend.controller;
 
import cn.how2j.trend.pojo.IndexData;
import cn.how2j.trend.service.BackTestService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class BackTestController {
    @Autowired BackTestService backTestService;
 
    @GetMapping("/simulate/{code}/{startDate}/{endDate}")
    @CrossOrigin
    public Map<String,Object> backTest(@PathVariable("code") String code,@PathVariable("startDate") String strStartDate
            ,@PathVariable("endDate") String strEndDate) throws Exception {
        List<IndexData> allIndexDatas = backTestService.listIndexData(code);
        String indexStartDate = allIndexDatas.get(0).getDate();
        String indexEndDate = allIndexDatas.get(allIndexDatas.size()-1).getDate();
        System.err.println("ffffff");
        allIndexDatas = filterByDateRange(allIndexDatas, strStartDate, strEndDate);
        Map<String,Object> result = new HashMap<>();
        result.put("indexStartDate", indexStartDate);
        result.put("indexEndDate", indexEndDate);
        result.put("indexDatas", allIndexDatas);
        return result;
    }

    private List<IndexData> filterByDateRange(List<IndexData> allIndexDatas, String strStartDate, String strEndDate) {
        if (StrUtil.isBlankOrUndefined(strStartDate) || StrUtil.isBlankOrUndefined(strEndDate)){
            return allIndexDatas;
        }
        List<IndexData> result = new ArrayList<>();
        Date startDate = DateUtil.parse(strStartDate);
        Date endDate = DateUtil.parse(strEndDate);
        for (IndexData indexData: allIndexDatas) {
            Date date = DateUtil.parse(indexData.getDate());
            if (date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime()){
                result.add(indexData);
            }
        }
        return result;
    }
}