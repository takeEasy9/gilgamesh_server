package com.gilgamesh.advice;

import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.enums.SystemCodeMsg;
import com.gilgamesh.common.enums.SystemEnums;
import com.gilgamesh.common.exceptions.BizException;
import com.gilgamesh.common.exceptions.SystemException;
import com.gilgamesh.common.utils.ResponseUtil;
import com.gilgamesh.common.utils.StringUtil;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description rest controller 全局异常处理器
 * @createDate 2024/5/2 16:15
 * @since 1.0.0
 */
@RestControllerAdvice
public class RestControllerGlobalExceptionHandlerAdvice {
    private final Logger logger = LoggerFactory.getLogger(RestControllerGlobalExceptionHandlerAdvice.class);

    /**
     * 当前运行环境
     */
    private final String activeProfile;

    public RestControllerGlobalExceptionHandlerAdvice(@Value("${spring.profiles.active:Unknown}") String activeProfile) {
        this.activeProfile = activeProfile;
    }

    /**
     * HTTP NOT Fund 异常处理
     *
     * @param e NoHandlerFoundException
     * @return Map < String, Object>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> noHandlerFoundExceptionHandler(NoHandlerFoundException e) {
        logger.error("<全局异常处理>: API NOT FOUND异常：", e);
        return ResponseUtil.failed(SystemCodeMsg.SYSTEM_HTTP_API_NOT_FOUND);
    }

    /**
     * HTTP消息转换异常处理
     *
     * @param e MethodArgumentNotValidException
     * @return Map < String, Object>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        logger.error("<全局异常处理>:HTTP消息转换异常：", e);
        return ResponseUtil.failed(SystemCodeMsg.SYSTEM_HTTP_API_NOT_FOUND);
    }

    /**
     * 参数校验失败，三种使用场景会抛出三种异常或者警告，分别是MethodArgumentNotValidException、ConstraintViolationException、BindException异常
     *
     * @param e MethodArgumentNotValidException
     * @return Map < String, Object>
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> bindExceptionHandler(BindException e) {
        logger.error("<全局异常处理>:接口参数校验异常：", e);
        // 生产环境只返回入参无效这一信息
        if (SystemEnums.Profile.PRODUCT.getValue().equals(activeProfile)) {
            return ResponseUtil.failed(BizCodeMsg.ACCESS_PARAM_INVALID);
        } else {
            // 从异常对象中拿到ObjectError对象, 并从ObjectError对象获取校验失败提示信息
            var errors = e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).filter(StringUtil::isNotEmpty).toList();
            // 非生产环境提取错误提示信息进行返回
            return ResponseUtil.failed(BizCodeMsg.ACCESS_PARAM_INVALID.getCode(), errors.toString());
        }

    }

    /**
     * 一些常见异常处理
     *
     * @param e Exception
     * @return Mono<Map < String, Object>>
     */
    @ExceptionHandler({NullPointerException.class, IndexOutOfBoundsException.class, SQLException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> exceptionHandler(NullPointerException e) {
        // 打印异常堆栈信息
        logger.error("<全局异常处理>:空指针异常：", e);
        return ResponseUtil.failed(BizCodeMsg.GUI_FAILED);
    }

    /**
     * 业务异常处理
     *
     * @param e BusinessException
     * @return Mono<Map < String, Object>>
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> businessExceptionHandler(BizException e) {
        // 打印异常堆栈信息
        logger.error("<全局异常处理>: 自定义业务异常：", e);
        return ResponseUtil.failed(BizCodeMsg.GUI_FAILED);
    }

    /**
     * 业务异常处理
     *
     * @param e BusinessException
     * @return Mono<Map < String, Object>>
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> businessExceptionHandler(SystemException e) {
        // 打印异常堆栈信息
        logger.error("<全局异常处理>: 自定义系统异常：", e);
        return ResponseUtil.failed(BizCodeMsg.GUI_FAILED);
    }

    /**
     * 用作兜底处理
     *
     * @param e Exception
     * @return Mono<Map < String, Object>>
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> exceptionHandler(Exception e) {
        // 打印异常堆栈信息
        logger.error("<全局异常处理>:其他异常：", e);
        return ResponseUtil.failed(BizCodeMsg.GUI_FAILED);
    }
}
