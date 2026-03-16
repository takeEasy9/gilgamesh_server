package com.gilgamesh.common.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 自定义可重复读取请求体的RequestWrapper, 核心：将请求体缓存到字节数组，所有流读取操作均从缓存获取
 * @createDate 2026/1/3 11:35
 * @since 1.0.0
 */
public class RepeatableReadRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger logger = LoggerFactory.getLogger(RepeatableReadRequestWrapper.class);
    /**
     * 默认缓冲区大小（8KB，符合JVM默认最优值）
     */
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    /**
     * 缓存的请求体字节数组
     */
    private final byte[] body;

    public RepeatableReadRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 一次性读取原始请求体并缓存
        this.body = this.readRequestBodyWithBufferedReader(request);
    }

    /**
     * 核心优化：使用BufferedReader读取请求体（高效+编码安全）
     *
     * @param request 原始请求
     * @return 缓存的请求体字节数组
     * @throws IOException 读取异常
     */
    private byte[] readRequestBodyWithBufferedReader(HttpServletRequest request) throws IOException {
        // 1. 确定请求编码（默认UTF-8，避免乱码）
        Charset charset = Charset.forName(getCharacterEncoding());
        // 2. 优先尝试字符流读取（BufferedReader高效读取）
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), charset),
                DEFAULT_BUFFER_SIZE // 自定义缓冲区大小，提升读取效率
        )) {
            // 字符流拼接容器（StringBuilder比StringBuffer更高效，无线程安全问题）
            StringBuilder bodyBuilder = new StringBuilder();
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int readLen;
            // 按缓冲区读取，减少IO调用次数
            while ((readLen = reader.read(buffer)) != -1) {
                bodyBuilder.append(buffer, 0, readLen);
            }
            // 转换为字节数组（按请求编码）
            return bodyBuilder.toString().getBytes(charset);
        } catch (IOException e) {
            // 3. 字符流读取失败（如二进制请求体），兜底用字节流读取
            logger.warn("BufferedReader读取请求体失败，兜底使用字节流读取：{}", e.getMessage());
            return readRequestBodyByBytes(request.getInputStream());
        }
    }

    /**
     * 兜底方案：字节流读取请求体（兼容二进制场景）
     *
     * @param inputStream 原始输入流
     * @return 字节数组
     * @throws IOException 读取异常
     */
    private byte[] readRequestBodyByBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int readLen;
        while ((readLen = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readLen);
        }
        return outputStream.toByteArray();
    }

    /**
     * 重写输入流读取：从缓存字节数组读取
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // 无需实现，同步读取场景
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            // 优化：重写read(byte[])方法，提升批量读取效率
            @Override
            public int read(byte[] b) throws IOException {
                return byteArrayInputStream.read(b);
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return byteArrayInputStream.read(b, off, len);
            }
        };
    }

    /**
     * 重写Reader读取：从缓存字节数组读取
     */
    @Override
    public java.io.BufferedReader getReader() throws IOException {
        return new java.io.BufferedReader(new java.io.InputStreamReader(getInputStream(), getCharacterEncoding()));
    }

    /**
     * 获取缓存的请求体字节数组
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * 获取请求体字符串
     */
    public String getBodyAsString() throws UnsupportedEncodingException {
        return new String(body, getCharacterEncoding());
    }
}
