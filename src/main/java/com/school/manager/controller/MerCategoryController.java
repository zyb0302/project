package com.school.manager.controller;

import com.school.manager.service.IMerCategoryService;
import com.github.pagehelper.PageInfo;
import com.school.manager.common.PageDataDto;
import com.school.manager.common.JsonResult;
import com.school.manager.common.ResultType;
import com.school.manager.model.MerCategory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@RestController
public class MerCategoryController {

    @Autowired
    private IMerCategoryService MerCategoryService;

    @RequestMapping("/queryMerCategoryList")
    public JsonResult queryMerCategoryList (@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                                     MerCategory MerCategory) {
        JsonResult<PageDataDto> result = new JsonResult<PageDataDto>(ResultType.SUCCESS);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        PageInfo<MerCategory> MerCategoryPageInfo = MerCategoryService.queryMerCategoryList(MerCategory,pageInfo);
        List<MerCategory> list = MerCategoryPageInfo.getList();
        PageDataDto pageDataDto = new PageDataDto(MerCategoryPageInfo.getTotal(),
                list,
                MerCategoryPageInfo.getPageNum(),
                MerCategoryPageInfo.getPageSize(),
                MerCategoryPageInfo.getPages());
        result.setData(pageDataDto);
        return result;
    }

    @RequestMapping("/addMerCategory")
    public JsonResult addMerCategory (@RequestBody MerCategory MerCategory) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        MerCategoryService.addMerCategory(MerCategory);
        return result;
    }

    @RequestMapping("/updateMerCategory")
    public JsonResult updateMerCategory (@RequestBody MerCategory MerCategory) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        MerCategoryService.updateMerCategory(MerCategory);
        return result;
    }

    @RequestMapping("/deleteMerCategory")
    public JsonResult deleteMerCategory (@RequestParam(value="id") Integer id) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        MerCategoryService.deleteMerCategory(id);
        return result;
    }
    @RequestMapping("/deleteAll")
    public JsonResult deleteAll () {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        MerCategoryService.deleteAll();
        return result;
    }

    @RequestMapping("/queryMerCategoryById")
    public JsonResult queryMerCategoryById (@RequestParam(value="id") Integer id) {
        JsonResult<MerCategory> result = new JsonResult<>(ResultType.SUCCESS);
        MerCategory MerCategory = MerCategoryService.queryMerCategoryById(id);
        result.setData(MerCategory);
        return result;
    }

    @RequestMapping("/queryMerCategoryAll")
    public JsonResult queryMerCategoryAll () {
        JsonResult<List<MerCategory>> result = new JsonResult<>(ResultType.SUCCESS);
        List<MerCategory> list = MerCategoryService.queryMerCategoryAll();
        result.setData(list);
        return result;
    }

    @RequestMapping("/queryByParentIdAndLevel")
    public JsonResult queryByParentIdAndLevel (@RequestParam(value="parentId") Integer parentId,@RequestParam(value="level") Integer level) {
        JsonResult<List<MerCategory>> result = new JsonResult<>(ResultType.SUCCESS);
        List<MerCategory> list = MerCategoryService.queryByParentIdAndLevel(parentId,level);
        result.setData(list);
        return result;
    }

    @RequestMapping("/collectAllCategory")
    public JsonResult collectAllCategory () {
        JsonResult<List<MerCategory>> result = new JsonResult<>(ResultType.SUCCESS);
        try {
            MerCategoryService.deleteAll();
            URL realUrl = new URL("https://www.mercari.com/jp/category/");
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            //   connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//重点
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(300000);
            connection.setReadTimeout(300000);
            // 建立实际的连接
            connection.connect();
            //请求成功
            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //10MB的缓存
                byte[] buffer = new byte[10485760];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                String jsonString = baos.toString();
                Document document = Jsoup.parse(jsonString);
                Elements elements = document.select("div[data-test=category-list-box]").
                        select("div[data-test=category-list-individual-box]");
                for (Element postItem : elements) {
                    //一级分类id
                    String category1_id = postItem.attr("name");
                    category1_id = category1_id.substring(category1_id.indexOf("-")+1);
                    System.out.println("一级分类id："+category1_id);
                    String category1_name = postItem.select("h2").text();
                    System.out.println("一级分类名称："+category1_name);
                    MerCategoryService.addMerCategory(buildCategory(Integer.parseInt(category1_id),category1_name,-1,1));
                    //所有二级分类标签
                    Elements category2 = postItem.select("h3");
                    Elements uls = postItem.getElementsByTag("ul");
                    for (int i=0;i<uls.size();i++) {
                        //二级分类
                        String category2_id = uls.get(i).select("a").first().attr("href");
                        String category2_name = category2.get(i).text();
                        category2_id = category2_id.substring(category2_id.lastIndexOf("/")+1);
                        System.out.println("二级分类id："+category2_id);
                        System.out.println("二级分类名称："+category2_name);
                        MerCategoryService.addMerCategory(buildCategory(Integer.parseInt(category2_id),category2_name,Integer.parseInt(category1_id),2));
                        Elements a = uls.get(i).select("a");
                        for (int j=1;j<a.size();j++) {
                            String category3_id = a.get(j).attr("href");
                            category3_id = category3_id.substring(category3_id.lastIndexOf("/")+1);
                            System.out.println("三级分类id："+category3_id);
                            String category3_name = a.get(j).text();
                            System.out.println("三级分类名称："+category3_name);
                            MerCategoryService.addMerCategory(buildCategory(Integer.parseInt(category3_id),category3_name,Integer.parseInt(category2_id),3));
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }


    private MerCategory buildCategory (Integer categoryId,String categoryName,Integer parentId,Integer level) {
        MerCategory merCategory = new MerCategory();
        merCategory.setCategoryId(categoryId);
        merCategory.setCategoryName(categoryName);
        merCategory.setParentId(parentId);
        merCategory.setLevel(level);
        return merCategory;
    }
}
