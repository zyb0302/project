package com.school.manager.controller;

import com.school.manager.model.MerItems;
import com.school.manager.service.IMarketConfirmService;
import com.github.pagehelper.PageInfo;
import com.school.manager.common.PageDataDto;
import com.school.manager.common.JsonResult;
import com.school.manager.common.ResultType;
import com.school.manager.model.MarketConfirm;
import com.school.manager.service.IMerItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MarketConfirmController {

    @Autowired
    private IMarketConfirmService MarketConfirmService;

    @Autowired
    private IMerItemsService merItemsService;

    @RequestMapping("/queryMarketConfirmList")
    public JsonResult queryMarketConfirmList (@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                                     MarketConfirm MarketConfirm) {
        JsonResult<PageDataDto> result = new JsonResult<PageDataDto>(ResultType.SUCCESS);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        PageInfo<MarketConfirm> MarketConfirmPageInfo = MarketConfirmService.queryMarketConfirmList(MarketConfirm,pageInfo);
        List<MarketConfirm> list = MarketConfirmPageInfo.getList();
        PageDataDto pageDataDto = new PageDataDto(MarketConfirmPageInfo.getTotal(),
                list,
                MarketConfirmPageInfo.getPageNum(),
                MarketConfirmPageInfo.getPageSize(),
                MarketConfirmPageInfo.getPages());
        result.setData(pageDataDto);
        return result;
    }

    @RequestMapping("/addMarketConfirm")
    public JsonResult addMarketConfirm (@RequestBody MarketConfirm MarketConfirm) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        MarketConfirmService.addMarketConfirm(MarketConfirm);
        return result;
    }

    @RequestMapping("/updateMarketConfirm")
    public JsonResult updateMarketConfirm (@RequestBody MarketConfirm MarketConfirm) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        MarketConfirmService.updateMarketConfirm(MarketConfirm);
        return result;
    }

    @RequestMapping("/deleteMarketConfirm")
    public JsonResult deleteMarketConfirm (@RequestParam(value="id") Integer id) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        MarketConfirmService.deleteMarketConfirm(id);
        return result;
    }
    @RequestMapping(value = "batchMarketConfirm")
    public JsonResult batchMarketConfirm(@RequestParam(value = "ids") int[] ids) throws Exception{
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        for (int i=0;i<ids.length;i++) {
            MarketConfirmService.deleteMarketConfirm(ids[i]);
        }
        return result;
    }

    @RequestMapping(value = "batchDeleteMarketConfirm")
    public JsonResult batchDeleteMarketConfirm(@RequestParam(value = "ids") int[] ids) throws Exception{
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        for (int i=0;i<ids.length;i++) {
            MarketConfirmService.deleteMarketConfirm(ids[i]);
        }
        return result;
    }

    @RequestMapping("/queryMarketConfirmById")
    public JsonResult queryMarketConfirmById (@RequestParam(value="id") Integer id) {
        JsonResult<MarketConfirm> result = new JsonResult<>(ResultType.SUCCESS);
        MarketConfirm MarketConfirm = MarketConfirmService.queryMarketConfirmById(id);
        result.setData(MarketConfirm);
        return result;
    }

    @RequestMapping("/queryMarketConfirmAll")
    public JsonResult queryMarketConfirmAll () {
        JsonResult<List<MarketConfirm>> result = new JsonResult<>(ResultType.SUCCESS);
        List<MarketConfirm> list = MarketConfirmService.queryMarketConfirmAll();
        result.setData(list);
        return result;
    }

    @RequestMapping("/queryMarketConfirmByUserIdAndPhoneId")
    public JsonResult queryMarketConfirmByUserIdAndPhoneId (@RequestParam(value="UserId") String UserId,@RequestParam(value="PhoneId") String PhoneId) {
        MarketConfirm marketConfirm = MarketConfirmService.queryMarketConfirmByUserIdAndPhoneId(UserId,PhoneId);
        if (marketConfirm != null) {
            String isconfirm = marketConfirm.getIsconfirm();
            if ("true".equals(isconfirm)) {
                JsonResult<List<MerItems>> result1 = new JsonResult<>(ResultType.SUCCESS);
                List<MerItems> merItems = merItemsService.queryMerItemsByUserIdAndWaiting(marketConfirm.getUserid());
                result1.setData(merItems);
                return result1;
            }else {
                return new JsonResult<>(ResultType.NOT_CONFIRM);
            }
        }else {
            return new JsonResult<>(ResultType.NOT_EXIST_CONFIRM);
        }
    }

    @RequestMapping("/updateIsconfirm")
    public JsonResult updateIsconfirm (@RequestParam(value = "isconfirm") String isconfirm,@RequestParam(value = "id") Integer id) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        MarketConfirmService.updateIsconfirm(isconfirm,id);
        return result;
    }
}
