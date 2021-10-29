var table,layer,laydate,upload,form;
layui.use(['table',"layer",'laydate','upload','form'], function(){
    table = layui.table;
    layer = layui.layer;
    upload = layui.upload;
    form = layui.form;
    //执行一个laydate实例
    laydate = layui.laydate;
    laydate.render({
        elem: '#datetime' //指定元素
    });
    //普通图片上传
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
            if(res.code == 1000){
                return layer.msg('upload successful');
            }else{
                return layer.msg('upload fail');
            }
        }
        ,error: function(){
            //演示失败状态，并实现重传
            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">upload fail</span> <a class="layui-btn layui-btn-xs demo-reload">retry</a>');
            demoText.find('.demo-reload').on('click', function(){
                uploadInst.upload();
            });
        }
    });
});