package com.school.manager.controller;

import com.github.pagehelper.PageInfo;
import com.school.manager.common.CaptchaImgWrapper;
import com.school.manager.common.JsonResult;
import com.school.manager.common.PageDataDto;
import com.school.manager.common.ResultType;
import com.school.manager.model.User;
import com.school.manager.service.IUserService;
import com.sun.media.sound.InvalidDataException;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 登录验证
     * @param user
     * @return
     */
    @RequestMapping("/userLogin")
    public JsonResult login (@RequestBody User user) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        User user1 = userService.queryByName(user.getUsername());
        if (user1 == null) {
            result = new JsonResult<String>(ResultType.USER_NOT_EXIST);
        }else {
            if (user1.getStatus() == 2) {
                return new JsonResult<String>(ResultType.USER_NOT_LOGIN);
            }
            if (!user.getPassword().equals(user1.getPassword())) {
                result = new JsonResult<String>(ResultType.PWSSWORD_ERROR);
            }else {
                //登录成功用户信息放到session
                session.setAttribute("username",user1.getUsername());
                session.setAttribute("userId",user1.getId());
                result.setData(user1.getUsername());
            }
        }
        return result;
    }

    @RequestMapping("/queryUserList")
    public JsonResult queryUserList (@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                                     User res) {
        JsonResult<PageDataDto> result = new JsonResult<PageDataDto>(ResultType.SUCCESS);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        PageInfo<User> carPageInfo = userService.queryUserList(res,pageInfo);
        List<User> resList = carPageInfo.getList();
        PageDataDto pageDataDto = new PageDataDto(carPageInfo.getTotal(),
                resList,
                carPageInfo.getPageNum(),
                carPageInfo.getPageSize(),
                carPageInfo.getPages());
        result.setData(pageDataDto);
        return result;
    }

    @RequestMapping("/addUser")
    public JsonResult addUser (@RequestBody User user) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        User user1 = userService.queryByName(user.getUsername());
        if (user1 != null) {
            result.setCode(ResultType.USER_EXIST.getCode());
            result.setMsg(ResultType.USER_EXIST.getInfo());
            return result;
        }
        userService.add(user);
        return result;
    }
    @RequestMapping("/updateUser")
    public JsonResult updateUser (@RequestBody User user) {
        JsonResult<User> result = new JsonResult<User>(ResultType.SUCCESS);
        userService.update(user);
        return result;
    }

    @RequestMapping("/deleteUser")
    public JsonResult deleteResult (@RequestParam(value = "id") Integer id) {
        JsonResult<User> result = new JsonResult<User>(ResultType.SUCCESS);
        userService.delete(id);
        return result;
    }

    @RequestMapping(value = "batchDelete")
    public JsonResult batchDelete(@RequestParam(value = "ids") int[] ids) throws Exception{
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        for (int i=0;i<ids.length;i++) {
            userService.delete(ids[i]);
        }
        return result;
    }

    @RequestMapping("/updatePassword")
    public JsonResult updatePassword (@RequestBody User user) {
        JsonResult<User> result = new JsonResult<User>(ResultType.SUCCESS);
        User user1 = userService.queryByName(user.getUsername());
        if (user1 != null) {
            user1.setPassword(user.getPassword());
        }
        userService.update(user1);
        return result;
    }

    /**
     * 修改用户状态，启用、停用
     * @param status
     * @return
     */
    @RequestMapping("/updateStatus")
    public JsonResult updateStatus (@RequestParam(value = "status") Integer status,@RequestParam(value = "id") Integer id) {
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        userService.updateStatus(status,id);
        return result;
    }

    @RequestMapping("/queryUserById")
    public JsonResult queryUserById (@RequestParam(value = "id") Integer id) {
        JsonResult<User> result = new JsonResult<User>(ResultType.SUCCESS);
        User user = userService.queryUserById(id);
        result.setData(user);
        return result;
    }


    @RequestMapping("/queryUserByUsername")
    public JsonResult queryUserByUsername (@RequestParam(value = "username") String username) {
        JsonResult<User> result = new JsonResult<User>(ResultType.SUCCESS);
        User user1 = userService.queryByName(username);
        result.setData(user1);
        return result;
    }

    @RequestMapping(value = "/sendImg", method = RequestMethod.GET)
    public void sendImg(Integer width, Integer height, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        HttpSession session = req.getSession();
        if (width == null || height == null) {
            throw new InvalidDataException("数据效验为空");
        }
        //四位随机码
        String captcha = CaptchaImgWrapper.generateCaptch(4);
        RenderedImage image = CaptchaImgWrapper.generatePic(width, height, captcha);
        // 调用工具类生成的验证码和验证码图片
        session.setAttribute("imgCode", captcha);
        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", -1);
        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos;
        sos = resp.getOutputStream();
        ImageIO.write(image, "png", sos);
        sos.close();
    }

    @RequestMapping(value = "/verifyCode")
    public JsonResult<String> verifyImg(@RequestParam(value = "code") String code, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        String imgCode = (String) session.getAttribute("imgCode");
        JsonResult<String> result = new JsonResult<>(ResultType.SUCCESS);
        if (code != null && imgCode != null && code.equalsIgnoreCase(imgCode)) {// 不区分大小写
            result.setData("验证码正确");
        } else {// 验证码错误或者数据为空
            result = new JsonResult<String>(ResultType.VCODE_ERROR);
        }
        return result;
    }

    //图片上传
    @RequestMapping("upload")
    public JsonResult upload(MultipartFile file, HttpServletRequest request){
        JsonResult<String> result = new JsonResult<String>(ResultType.SUCCESS);
        String prefix="";
        String dateStr="";
        //保存上传
        OutputStream out = null;
        InputStream fileInput=null;
        HttpSession session = request.getSession();
        try{
            if(file!=null){
                String originalName = file.getOriginalFilename();
                prefix=originalName.substring(originalName.lastIndexOf(".")+1);
                Date date = new Date();
                String uuid = UUID.randomUUID()+"";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateStr = simpleDateFormat.format(date);
                //      String filepath = "D:\\mycode\\LayUiTest\\src\\main\\resources\\static\\images\\" + dateStr+"\\"+uuid+"." + prefix;
                //    String filepath = "D:\\image"; webapp\WEB-INF\static
                //     String filepath = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF\\static\\img\\"+originalName;
                String realPath = request.getRealPath("/");  //获取磁盘根路径
                String substring = realPath.substring(0, realPath.indexOf("out"));
                String filepath = substring + "\\src\\main\\webapp\\WEB-INF\\static\\img\\"+originalName;
                File files=new File(filepath);
                //打印查看上传路径
                System.out.println(filepath);
                if(!files.getParentFile().exists()){
                    files.getParentFile().mkdirs();
                }
                file.transferTo(files);
                session.setAttribute("imgpath","/static/img/"+originalName);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(out!=null){
                    out.close();
                }
                if(fileInput!=null){
                    fileInput.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }
}
