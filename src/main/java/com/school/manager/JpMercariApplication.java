package com.school.manager;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author jack
 * @version 1.0
 * @date 2021/9/29 10:30
 */
public class JpMercariApplication {
    public static void main(String[] args) {
        // 这边是商品的id
        HttpResponse shopInfo = getShopInfo(96083154434L);
        String result = shopInfo.body();
        JSONObject jsonObject = JSON.parseObject(result);
        Object result1 = jsonObject.get("result");
        if ("OK".equals(result1.toString())) {
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null) {
                String id = data.getString("id");   //商品id
                String name = data.getString("name");    //商品名称
                Integer price = data.getInteger("price");   //商品价格
                String description = data.getString("description");  //商品描述
                //商品分类信息
                JSONObject item_category = data.getJSONObject("item_category");
                String t1CategoryName = item_category.getString("root_category_name");
                String t1CategoryId = item_category.getString("root_category_id");
                String t2CategoryName = item_category.getString("parent_category_name");
                String t2CategoryId = item_category.getString("parent_category_id");
                String t3CategoryName = item_category.getString("name");
                String t3CategoryId = item_category.getString("id");
                String photos = data.getString("photos");  //商品图片
                photos = photos.replace("[","").replace("]","").replace("\"","");
                System.out.println(photos);
                //商品状态
                JSONObject item_condition = data.getJSONObject("item_condition");
                if (item_condition != null) {
                    String itemstatus = item_condition.getString("name");
                }
                //运费负担
                JSONObject shipping_payer = data.getJSONObject("shipping_payer");
                if (shipping_payer != null) {
                    String postageby = shipping_payer.getString("name");
                }
                //发货方式
                JSONObject shipping_method = data.getJSONObject("shipping_method");
                if (shipping_method != null) {
                    String deliverymethod = shipping_method.getString("name");
                }
                //发货地址
                JSONObject shipping_from_area = data.getJSONObject("shipping_from_area");
                if (shipping_from_area != null) {
                    String deliveryaddress = shipping_from_area.getString("name");
                }
                //发货日期
                JSONObject shipping_duration = data.getJSONObject("shipping_duration");
                if (shipping_duration != null) {
                    String deliverydate = shipping_duration.getString("name");
                }
                //尺寸
                JSONObject item_size = data.getJSONObject("item_size");
                if (item_size != null) {
                    String size = item_size.getString("name");
                }
                //品牌
                JSONObject item_brand = data.getJSONObject("item_brand");
                if (item_brand != null) {
                    String brand = item_brand.getString("name");
                }
            }
        }
        System.out.println(result);
    }

    public static HttpResponse getShopInfo(Long shopId) {
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
