package com.school.manager.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.school.manager.model.Image;
import com.school.manager.model.MerCategory;
import com.school.manager.service.IMerItemsService;
import com.github.pagehelper.PageInfo;
import com.school.manager.common.PageDataDto;
import com.school.manager.common.JsonResult;
import com.school.manager.common.ResultType;
import com.school.manager.model.MerItems;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class MerItemsController {

    @Autowired
    private IMerItemsService MerItemsService;

    @Value("${file.path}")
    private String filePath;

    @Value("${server.host}")
    private String host;

    @RequestMapping("/queryMerItemsList")
    public JsonResult queryMerItemsList (@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                                     MerItems MerItems) {
        JsonResult<PageDataDto> result = new JsonResult<PageDataDto>(ResultType.SUCCESS);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        PageInfo<MerItems> MerItemsPageInfo = MerItemsService.queryMerItemsList(MerItems,pageInfo);
        List<MerItems> list = MerItemsPageInfo.getList();
        PageDataDto pageDataDto = new PageDataDto(MerItemsPageInfo.getTotal(),
                list,
                MerItemsPageInfo.getPageNum(),
                MerItemsPageInfo.getPageSize(),
                MerItemsPageInfo.getPages());
        result.setData(pageDataDto);
        return result;
    }

    @RequestMapping("/addMerItems")
    public JsonResult addMerItems (@RequestBody MerItems MerItems,HttpServletRequest request) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        HttpSession session = request.getSession();
        Object object = session.getAttribute("images");
        if (object != null) {
            StringBuffer sb = new StringBuffer();
            List<Image> images = (List)session.getAttribute("images");
            images.forEach(item -> sb.append(item.getPath()+","));
            MerItems.setImages(sb.toString());
        }
        MerItemsService.addMerItems(MerItems);
        return result;
    }

    @RequestMapping("/updateMerItems")
    public JsonResult updateMerItems (@RequestBody MerItems MerItems,HttpServletRequest request) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        HttpSession session = request.getSession();
        Object object = session.getAttribute("images");
        if (object != null) {
            StringBuffer sb = new StringBuffer();
            List<Image> images = (List)session.getAttribute("images");
            images.forEach(item -> sb.append(item.getPath()+","));
            MerItems.setImages(sb.toString());
        }
        MerItemsService.updateMerItems(MerItems);
        return result;
    }

    @RequestMapping("/deleteMerItems")
    public JsonResult deleteMerItems (@RequestParam(value="id") Integer id) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        MerItemsService.deleteMerItems(id);
        return result;
    }
    @RequestMapping(value = "batchDeleteMerItems")
    public JsonResult batchDeleteMerItems(@RequestParam(value = "ids") int[] ids) throws Exception{
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        for (int i=0;i<ids.length;i++) {
            MerItemsService.deleteMerItems(ids[i]);
        }
        return result;
    }

    @RequestMapping("/queryMerItemsById")
    public JsonResult queryMerItemsById (@RequestParam(value="id") Integer id,HttpServletRequest request) {
        JsonResult<MerItems> result = new JsonResult<>(ResultType.SUCCESS);
        MerItems MerItems = MerItemsService.queryMerItemsById(id);
        HttpSession session = request.getSession();
        if (MerItems != null) {
            String[] split = MerItems.getImages().split(",");
            List<Image> list = new ArrayList<>();
            for (int i=0;i<split.length;i++) {
                Image image = new Image();
                image.setId(i+1);
                image.setPath(split[i]);
                list.add(image);
            }
            MerItems.setList(list);
            session.setAttribute("images",list);
        }
        result.setData(MerItems);
        return result;
    }

    @RequestMapping("/queryMerItemsAll")
    public JsonResult queryMerItemsAll () {
        JsonResult<List<MerItems>> result = new JsonResult<>(ResultType.SUCCESS);
        List<MerItems> list = MerItemsService.queryMerItemsAll();
        result.setData(list);
        return result;
    }
    /**
     * 修改发布状态，启用、停用
     * @param waiting
     * @return
     */
    @RequestMapping("/updateWaiting")
    public JsonResult updateWaiting (@RequestParam(value = "waiting") Integer waiting,@RequestParam(value = "id") Integer id) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        MerItemsService.updateWaiting(waiting,id);
        return result;
    }

    @PostMapping(value="/multifileUpload")
    public JsonResult multifileUpload(HttpServletRequest request){
        JsonResult<String> result = new JsonResult<>(ResultType.SUCCESS);
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        if(files.isEmpty()){
            result = new JsonResult<>(ResultType.FILE_NULL);
        }
        for(MultipartFile file:files){
            String fileName1 = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
          //  String filepath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\phone\\"+fileName;
            if(file.isEmpty()){
                result = new JsonResult<>(ResultType.FILE_NULL);
            }else{
                File dest = new File(filePath+fileName);
                if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                try {
                    //保存文件到本地磁盘
                    file.transferTo(dest);

                }catch (Exception e) {
                    e.printStackTrace();
                    result = new JsonResult<>(ResultType.FILE_ERROR);
                }
            }
        }
        return result;
    }

    @RequestMapping("/collectMerItems")
    public JsonResult collectMerItems () {
        JsonResult<List<MerCategory>> result = new JsonResult<>(ResultType.SUCCESS);
        try {
            URL realUrl = new URL("https://jp.mercari.com/item/m67208856125");
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
                    //MerCategoryService.addMerCategory(buildCategory(Integer.parseInt(category1_id),category1_name,-1,1));
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
                       // MerCategoryService.addMerCategory(buildCategory(Integer.parseInt(category2_id),category2_name,Integer.parseInt(category1_id),2));
                        Elements a = uls.get(i).select("a");
                        for (int j=1;j<a.size();j++) {
                            String category3_id = a.get(j).attr("href");
                            category3_id = category3_id.substring(category3_id.lastIndexOf("/")+1);
                            System.out.println("三级分类id："+category3_id);
                            String category3_name = a.get(j).text();
                            System.out.println("三级分类名称："+category3_name);
                           // MerCategoryService.addMerCategory(buildCategory(Integer.parseInt(category3_id),category3_name,Integer.parseInt(category2_id),3));
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


    @PostMapping(value="/multifileUpload1")
    public JsonResult multifileUpload1(HttpServletRequest request){
        JsonResult<List<Image>> result = new JsonResult<>(ResultType.SUCCESS);
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        HttpSession session = request.getSession();
        if(files.isEmpty()){
            result = new JsonResult<>(ResultType.FILE_NULL);
        }
        int i=0;
        List<Image> list = new ArrayList<>();
        for(MultipartFile file:files){
            i++;
            String fileName = file.getOriginalFilename();
            String substring = fileName.substring(fileName.indexOf("."));
            String name = UUID.randomUUID().toString();
            fileName = name + substring;
            Image image = new Image();
            image.setId(i);
            image.setPath(host + "/images/"+fileName);
            list.add(image);

            String filepath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\"+fileName;
            if(file.isEmpty()){
                result = new JsonResult<>(ResultType.FILE_NULL);
            }else{
                File dest = new File(filePath+fileName);
               // File dest = new File(filepath);
                if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                try {
                    //保存文件到本地磁盘
                    file.transferTo(dest);

                }catch (Exception e) {
                    e.printStackTrace();
                    result = new JsonResult<>(ResultType.FILE_ERROR);
                }
            }
        }
        session.setAttribute("images",list);
        result.setData(list);
        return result;
    }

    @RequestMapping("/changeOrder")
    public JsonResult changeOrder (@RequestParam(value = "orderTag") String orderTag,HttpServletRequest request) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        HttpSession session = request.getSession();
        List<Image> images = (List)session.getAttribute("images");
        List<Image> list = new ArrayList<>();
        String[] split = orderTag.split(",");
        for (int i = 0;i<split.length;i++) {
            for (Image image : images) {
                if (Integer.parseInt(split[i]) == image.getId()) {
                    list.add(image);
                }
            }
        }
        session.setAttribute("images",list);
        return result;
    }

    @RequestMapping("/collectMerItemById")
    public JsonResult collectMerItemById (@RequestParam(value = "ids") String ids) {
        JsonResult<String> result2 = new JsonResult<>(ResultType.SUCCESS);
        String[] split = ids.split("\\s+");
        for (int i = 0;i<split.length; i++) {
            boolean numeric = isNumeric(split[i]);
            if (numeric) {
                colelet(Long.parseLong(split[i]));
            }
        }
        return result2;
    }

    @RequestMapping("/deleteImage")
    public JsonResult deleteImage (@RequestParam(value = "imageId") String imageId,HttpServletRequest request) {
        JsonResult<String> result = new JsonResult<>(ResultType.SUCCESS);
        HttpSession session = request.getSession();
        List<Image> images = (List)session.getAttribute("images");
        List<Image> list = new ArrayList<>();
        for (Image image : images) {
            if (Integer.parseInt(imageId) != image.getId()) {
                list.add(image);
            }
        }
        session.setAttribute("images",images);
        return result;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    private boolean isNumeric(String str){
        final Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    private void colelet(Long shopId) {
        //HttpResponse shopInfo = getShopInfo(96083154434L);
        HttpResponse shopInfo = getShopInfo(shopId);
        String result = shopInfo.body();
        JSONObject jsonObject = JSON.parseObject(result);
        Object result1 = jsonObject.get("result");
        MerItems merItems = new MerItems();
        if ("OK".equals(result1.toString())) {
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null) {
                String id = data.getString("id");   //商品id
                // merItems.setSku(id);

                String name = data.getString("name");    //商品名称
                merItems.setItemname(name);

                Double price = data.getDouble("price");   //商品价格
                merItems.setPrice(price);

                String description = data.getString("description");  //商品描述
                merItems.setDes(description);

                String photos = data.getString("photos");  //商品图片
                photos = photos.replace("[","").replace("]","").replace("\"","");
                merItems.setImages(photos);
                //商品分类信息
                JSONObject item_category = data.getJSONObject("item_category");
                String t1CategoryName = item_category.getString("root_category_name");
                merItems.setT1CategoryName(t1CategoryName);
                Integer t1CategoryId = item_category.getInteger("root_category_id");
                merItems.setT1CategoryId(t1CategoryId);
                String t2CategoryName = item_category.getString("parent_category_name");
                merItems.setT2CategoryName(t2CategoryName);
                Integer t2CategoryId = item_category.getInteger("parent_category_id");
                merItems.setT2CategoryId(t2CategoryId);
                String t3CategoryName = item_category.getString("name");
                merItems.setT3CategoryName(t3CategoryName);
                Integer t3CategoryId = item_category.getInteger("id");
                merItems.setT3CategoryId(t3CategoryId);
                //商品状态
                JSONObject item_condition = data.getJSONObject("item_condition");
                if (item_condition != null) {
                    String itemstatus = item_condition.getString("name");
                    merItems.setItemstatus(itemstatus);
                }
                //运费负担
                JSONObject shipping_payer = data.getJSONObject("shipping_payer");
                if (shipping_payer != null) {
                    String postageby = shipping_payer.getString("name");
                    merItems.setPostageby(postageby);
                }
                //发货方式
                JSONObject shipping_method = data.getJSONObject("shipping_method");
                if (shipping_method != null) {
                    String deliverymethod = shipping_method.getString("name");
                    merItems.setDeliverymethod(deliverymethod);
                }
                //发货地址
                JSONObject shipping_from_area = data.getJSONObject("shipping_from_area");
                if (shipping_from_area != null) {
                    String deliveryaddress = shipping_from_area.getString("name");
                    merItems.setDeliveryaddress(deliveryaddress);
                }
                //发货日期
                JSONObject shipping_duration = data.getJSONObject("shipping_duration");
                if (shipping_duration != null) {
                    String deliverydate = shipping_duration.getString("name");
                    merItems.setDeliverydate(deliverydate);
                }
                //尺寸
                JSONObject item_size = data.getJSONObject("item_size");
                if (item_size != null) {
                    String size = item_size.getString("name");
                    merItems.setSize(size);
                }
                //品牌
                JSONObject item_brand = data.getJSONObject("item_brand");
                if (item_brand != null) {
                    String brand = item_brand.getString("name");
                    merItems.setBrand(brand);
                }
                MerItemsService.addMerItems(merItems);
            }
        }
    }

    private HttpResponse getShopInfo(Long shopId) {
        Map<String, String> headers = new HashMap<String, String>() {{
            put("Host", "api.mercari.jp");
            put("Connection", "keep-alive");
            put("sec-ch-ua", "\"Chromium\";v=\"94\", \"Google Chrome\";v=\"94\", \";Not A Brand\";v=\"99\"");
            put("Accept", "application/json, text/plain, */*");
            put("X-Platform", "web");
            put("DPoP", "eyJ0eXAiOiJkcG9wK2p3dCIsImFsZyI6IkVTMjU2IiwiandrIjp7ImNydiI6IlAtMjU2Iiwia3R5IjoiRUMiLCJ4IjoiNXJpdTBqclVkWjlQX0JmRDVGYUFqTFdRTkpIZnJqRjVMS2tqRjRyYWJ1byIsInkiOiJQN3lQc2FLTGJJT0VDVjZNQUwxWVhXUEdzUkVyUDJzVEhBYmFnamdDdmU4In19.eyJpYXQiOjE2MzI4MTMxOTcsImp0aSI6ImQxZTk4YTRhLWNkYTEtNDMzMi05OGU5LTI4NTViNjY1NTMwZCIsImh0dSI6Imh0dHBzOi8vYXBpLm1lcmNhcmkuanAvaXRlbXMvZ2V0IiwiaHRtIjoiR0VUIn0.SWg7OeLgMzFSWQ9KD8xT8Y7XSI9Sgi8l0eLf_2etlm8WY-YV17cQAb-Ub4uRCVZqbeQmjRftRjeYazHlTg0LkA");
            put("sec-ch-ua-mobile", "?0");
            put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.12 Safari/537.36");
            put("sec-ch-ua-platform", "\"Windows\"");
            put("Origin", "https://jp.mercari.com");
            put("Sec-Fetch-Site", "cross-site");
            put("Sec-Fetch-Mode", "cors");
            put("Sec-Fetch-Dest", ".empty");
            put("Referer", "https://jp.mercari.com/");
        }};
        return HttpRequest.get("https://api.mercari.jp/items/get?id=m" + shopId)
                .addHeaders(headers)
//                .setHttpProxy("127.0.0.1", 10809)
                .execute();
    }
}
