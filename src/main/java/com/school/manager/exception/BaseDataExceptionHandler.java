package com.school.manager.exception;

import com.school.manager.common.JsonResult;
import com.school.manager.common.ResultType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常拦截类
 */
@Slf4j
@RestControllerAdvice
public class BaseDataExceptionHandler {

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //System.out.println("请求有参数才进来");
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e){
        JsonResult result = new JsonResult(ResultType.SUCCESS);
        //业务异常
        if(e instanceof BaseDataException){
            result.setCode(((BaseDataException) e).getCode());
            result.setMsg(e.getMessage());
        }else{
            result.setCode(ResultType.WRONG.getCode());
            result.setMsg(ResultType.WRONG.getInfo());
        }
        log.error(e.getMessage(),e);
        return result;
    }
}
