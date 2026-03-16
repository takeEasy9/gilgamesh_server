package com.gilgamesh.common.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 可重复读取的ContentCachingRequestWrapper扩展类
 * @createDate 2026/1/3 12:06
 * @since 1.0.0
 */
public class RepeatableContentCachingRequestWrapper extends ContentCachingRequestWrapper {

    /**
     * 构造方法：默认缓存限制（Spring原生默认值：2048字节）
     *
     * @param request 原始HttpServletRequest
     */
    public RepeatableContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 构造方法：自定义缓存大小限制
     *
     * @param request           原始HttpServletRequest
     * @param contentCacheLimit 缓存字节上限（超出会抛出IOException）
     */
    public RepeatableContentCachingRequestWrapper(HttpServletRequest request, int contentCacheLimit) {
        super(request, contentCacheLimit);
    }

    /**
     * 重写getReader：流读取完毕时，从缓存重建Reader，支持重复读取
     *
     * @return 可重复读取的BufferedReader
     * @throws IOException IO异常
     */
    @Override
    public BufferedReader getReader() throws IOException {
        ServletInputStream inputStream = super.getInputStream();
        // 原始流已读取完毕 → 从缓存字节数组重建Reader
        if (inputStream.isFinished()) {
            byte[] cachedBody = super.getContentAsByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBody);
            // 严格使用请求的字符编码，避免乱码
            return new BufferedReader(new InputStreamReader(byteArrayInputStream, getCharacterEncoding()));
        }
        // 原始流未读取 → 调用父类原生逻辑
        return super.getReader();
    }

    /**
     * 重写getInputStream：流读取完毕时，从缓存重建ServletInputStream，支持重复读取
     *
     * @return 可重复读取的ServletInputStream
     * @throws IOException IO异常
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream originalInputStream = super.getInputStream();
        // 原始流已读取完毕 → 从缓存字节数组重建流
        if (originalInputStream.isFinished()) {
            byte[] cachedBody = super.getContentAsByteArray();
            return new CachedBodyServletInputStream(cachedBody);
        }
        // 原始流未读取 → 返回原生流
        return originalInputStream;
    }

    /**
     * 基于缓存字节数组的ServletInputStream实现类
     * 支持重复读取，核心适配ContentCachingRequestWrapper的缓存机制
     */
    public static class CachedBodyServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream cachedBodyInputStream;

        /**
         * 构造方法：传入缓存的请求体字节数组
         *
         * @param cachedBody 从ContentCachingRequestWrapper获取的缓存字节
         */
        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
        }

        /**
         * 判断流是否读取完毕
         *
         * @return true=已读完，false=还有数据
         */
        @Override
        public boolean isFinished() {
            return cachedBodyInputStream.available() == 0;
        }

        /**
         * 判断流是否就绪（同步读取场景直接返回true）
         *
         * @return 始终返回true
         */
        @Override
        public boolean isReady() {
            return true;
        }

        /**
         * 设置读取监听器（同步场景不支持，抛出不支持异常）
         *
         * @param listener 读取监听器
         */
        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException("CachedBodyServletInputStream 不支持异步读取监听器");
        }

        /**
         * 读取单个字节（核心重复读取逻辑）
         *
         * @return 读取的字节（-1表示读取完毕）
         * @throws IOException IO异常
         */
        @Override
        public int read() throws IOException {
            return cachedBodyInputStream.read();
        }
    }
}
