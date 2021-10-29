var form, $,areaData;
layui.config({
    base : "js/"
}).extend({
    "address" : "address"
})
layui.use(['form','layer','upload','laydate',"address"],function(){
    form = layui.form;
    $ = layui.jquery;
    var layer = layui.layer,
        upload = layui.upload,
        laydate = layui.laydate,
        address = layui.address;

    //上传头像
  /*  upload.render({
        elem: '.userFaceBtn',
        url: '../../json/userface.json',
        method : "get",  //此处是为了演示之用，实际使用中请将此删除，默认用post方式提交
        done: function(res, index, upload){
            var num = parseInt(4*Math.random());  //生成0-4的随机数，随机显示一个头像信息
            $('#userFace').attr('src',res.data[num].src);
            window.sessionStorage.setItem('userFace',res.data[num].src);
        }
    });*/
    var uploadInst = upload.render({
        elem: '.userFaceBtn'
        ,url: '/upload' //改成您自己的上传接口
        ,before: function(obj){
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                $('#userFace').attr('src', result); //图片链接（base64）
            });
        }
        ,done: function(res){
            window.sessionStorage.setItem('userFace',res.data[num].src);
        }
        ,error: function(){
            //演示失败状态，并实现重传
            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function(){
                uploadInst.upload();
            });
        }
    });

    //添加验证规则
    form.verify({
        userBirthday : function(value){
            if(!/^(\d{4})[\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|1[0-2])([\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/.test(value)){
                return "出生日期格式不正确！";
            }
        }
    })
    //选择出生日期
    laydate.render({
        elem: '.userBirthday',
        format: 'yyyy年MM月dd日',
        trigger: 'click',
        max : 0,
        mark : {"0-12-15":"生日"},
        done: function(value, date){
            if(date.month === 12 && date.date === 15){ //点击每年12月15日，弹出提示语
                layer.msg('今天是马哥的生日，也是layuicms2.0的发布日，快来送上祝福吧！');
            }
        }
    });

    //获取省信息
    address.provinces();

    //提交个人资料
    form.on("submit(changeUser)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        //将填写的用户信息存到session以便下次调取
        var key,userInfo = '';
        userInfo = {
            'realname' : $(".realName").val(),
            'sex' : data.field.sex,
            'phone' : $(".userPhone").val(),
            'address' : $(".userAddress").val(),
            'email' : $(".userEmail").val()
        };
        var param = JSON.stringify(userInfo);
        $.ajax({
            type : "POST",
            dataType: "json",//通过GET方式上传请求
            contentType : "application/json",//上传内容格式为json结构
            data : param,                                 //上传的参数
            async: false ,
            url : "/updateUser",
            success : function(data) {          //请求成功的回调函数
                if (data.code == 1000) {
                    layer.msg("提交成功！");
                    initData($("#username").val());
                }else {
                    layer.msg(data.msg, { icon: 5 });
                }

            },
            error : function(e) {           //请求失败的回调函数
                console.info(e);
            }
        });
        window.sessionStorage.setItem("userInfo",JSON.stringify(userInfo));
        /*setTimeout(function(){
            layer.close(index);
            layer.msg("提交成功！");
        },2000);*/
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })

    //修改密码
    form.on("submit(changePwd)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            layer.msg("密码修改成功！");
            $(".pwd").val('');
        },2000);
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })
})
function initData(username) {
    $.ajax({
        type : "POST",
        dataType: "json",//通过GET方式上传请求
        contentType : "application/json",//上传内容格式为json结构
        async: false ,
        url : "/queryUserByUsername?username="+username,
        success : function(data) {          //请求成功的回调函数
            if (data.code == 1000) {
                var user = data.data;
                $("#userId").val(user.id);
                $("#username").val(user.name);
                $("#role").val(user.role);
                $(".userPhone").val(user.phone);
                $(".address").val(user.address);
                $(".userEmail").val(user.email);
             //   $("input[name='sex'][value='"+user.sex+"']").attr("checked",true);
                $(".userSex input[value="+user.sex+"]").attr("checked","checked"); //性别
            }else {
                layer.msg(data.msg, { icon: 5 });
            }
        },
        error : function(e) {           //请求失败的回调函数
            console.info(e);
        }
    });
}