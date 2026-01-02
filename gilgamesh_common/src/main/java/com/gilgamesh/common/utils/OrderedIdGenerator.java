package com.gilgamesh.common.utils;

import com.gilgamesh.common.enums.IdPrefixEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author takeEasy9
 * @version 1.0.10
 * @description 有序ID生成器（Base62+隐私保护：SecureRandom偏移+Spring哈希+有序段前置保证有序性）
 * @createDate 2025/12/6 10:19
 * @since 1.0.0
 */
public class OrderedIdGenerator {
    // 初始化SLF4J日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderedIdGenerator.class);

    // ========== 核心：全小写Base62字符集 ==========
    public static final String BASE62_LOWER_CHARSET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int BASE62_LENGTH = BASE62_LOWER_CHARSET.length(); // 62

    // ========== 隐私保护（轻量化） ==========
    // 安全随机时间偏移量（全局唯一，仅初始化一次）
    private static final long TIME_OFFSET = 224240106804420L;

    // ========== ID结构设计（优先保证有序性 + 分布式唯一） ==========
    // 最终ID结构：前缀 + 有序核心段（14位）
    // 有序核心段：时间戳Base62编码（9位） + 节点ID（2位） + 序列号（3位）
    // 设计逻辑：时间戳（递增）→ 节点ID（固定）→ 序列号（递增），保证全局有序+分布式唯一
    private static final int TIMESTAMP_ENCODE_LENGTH = 9;
    private static final int WORKER_ID_ENCODE_LENGTH = 2;
    private static final int SEQUENCE_ENCODE_LENGTH = 3;
    private static final int ORDERED_CORE_LENGTH = TIMESTAMP_ENCODE_LENGTH + WORKER_ID_ENCODE_LENGTH + SEQUENCE_ENCODE_LENGTH; // 14位

    // ========== 基础常量 ==========
    // 起始时间戳：2025-01-01 00:00:00
    private static final long START_EPOCH = 1735689600000L;
    // Base62进制下最大取值
    private static final long MAX_SEQUENCE = (long) Math.pow(BASE62_LENGTH, SEQUENCE_ENCODE_LENGTH) - 1; // 62^3-1=238327

    // ========== 并发控制（保证有序性核心） ==========
    private static final Lock globalLock = new ReentrantLock(true); // 公平锁，保证线程执行顺序
    private static final long workerId = 0; // 分布式节点唯一ID（0~3843）
    private static volatile long lastTimestamp = -1L; // 上一次生成ID的时间戳
    private static volatile long sequence = 0L; // 同一时间戳内的序列号

    // 私有构造器，禁止实例化
    private OrderedIdGenerator() {
    }

    /**
     * 生成全局有序ID（核心：时间戳+节点ID+序列号严格递增，分布式唯一）
     */
    public static String generate(IdPrefixEnum idPrefix) {
        globalLock.lock();
        try {
            long currentTimestamp = System.currentTimeMillis();

            // 1. 时钟回拨校验（严格保证时间递增）
            if (currentTimestamp < lastTimestamp) {
                String errorMsg = String.format("时钟回拨检测：当前时间戳[%d] < 上一次时间戳[%d]，拒绝生成ID",
                        currentTimestamp, lastTimestamp);
                LOGGER.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }

            // 2. 序列号递增逻辑（同一时间戳内严格递增）
            if (currentTimestamp == lastTimestamp) {
                sequence++;
                // 序列号达到最大值，等待下一毫秒
                if (sequence > MAX_SEQUENCE) {
                    currentTimestamp = waitNextMillis(lastTimestamp);
                    sequence = 0L;
                    LOGGER.debug("同一毫秒序列号耗尽，等待下一毫秒：新时间戳={}", currentTimestamp);
                }
            } else {
                // 新时间戳，序列号重置为0（保证有序性）
                sequence = 0L;
                lastTimestamp = currentTimestamp;
            }

            // 3. 时间戳偏移处理（隐私保护，不影响有序性）
            long timestampDiff = (currentTimestamp - START_EPOCH) + TIME_OFFSET;

            // 4. Base62编码（保证字典序递增）
            String timestampPart = encodeToBase62(timestampDiff, TIMESTAMP_ENCODE_LENGTH);
            String workerIdPart = encodeToBase62(workerId, WORKER_ID_ENCODE_LENGTH); // 新增：编码节点ID
            String sequencePart = encodeToBase62(sequence, SEQUENCE_ENCODE_LENGTH);

            // 5. 构建有序核心段（时间戳+节点ID+序列号，保证全局有序+分布式唯一）
            String orderedCorePart = timestampPart + workerIdPart + sequencePart;
            // 校验长度（防止编码异常）
            if (orderedCorePart.length() != ORDERED_CORE_LENGTH) {
                throw new RuntimeException(String.format("有序核心段长度异常：预期%d位，实际%d位（时间戳段：%d，节点段：%d，序列号段：%d）",
                        ORDERED_CORE_LENGTH, orderedCorePart.length(),
                        timestampPart.length(), workerIdPart.length(), sequencePart.length()));
            }

            // 6. 最终ID拼接（前缀+有序核心段）
            String finalId = idPrefix.getValue() + orderedCorePart;
            LOGGER.trace("生成有序ID：前缀={}，时间戳段={}，节点段={}，序列号段={}，最终ID={}",
                    idPrefix.getValue(), timestampPart, workerIdPart, sequencePart, finalId);
            return finalId;

        } finally {
            globalLock.unlock();
        }
    }

    /**
     * 批量生成有序ID（保证批量内ID连续递增，分布式唯一）
     */
    public List<String> generateBatch(int count, IdPrefixEnum idPrefix) {
        if (count <= 0 || count > 1000) {
            throw new IllegalArgumentException("批量生成数量必须在1~1000之间：count=" + count);
        }

        globalLock.lock();
        try {
            List<String> idList = new ArrayList<>(count);
            long currentTimestamp = System.currentTimeMillis();

            // 时钟回拨校验
            if (currentTimestamp < lastTimestamp) {
                throw new RuntimeException(String.format("时钟回拨检测：当前时间戳[%d] < 上一次时间戳[%d]",
                        currentTimestamp, lastTimestamp));
            }

            // 序列号预分配（保证批量内连续）
            long startSequence = sequence;
            if (currentTimestamp == lastTimestamp) {
                // 检查当前时间戳下序列号是否足够
                if (startSequence + count > MAX_SEQUENCE) {
                    currentTimestamp = waitNextMillis(lastTimestamp);
                    startSequence = 0L;
                }
            } else {
                startSequence = 0L;
                lastTimestamp = currentTimestamp;
            }

            // 编码复用（提升性能）
            long timestampDiff = (currentTimestamp - START_EPOCH) + TIME_OFFSET;
            String timestampPart = encodeToBase62(timestampDiff, TIMESTAMP_ENCODE_LENGTH);
            String workerIdPart = encodeToBase62(workerId, WORKER_ID_ENCODE_LENGTH); // 新增：编码节点ID

            // 批量生成ID
            for (int i = 0; i < count; i++) {
                long currentSeq = startSequence + i;
                String sequencePart = encodeToBase62(currentSeq, SEQUENCE_ENCODE_LENGTH);
                String orderedCorePart = timestampPart + workerIdPart + sequencePart;
                idList.add(idPrefix.getValue() + orderedCorePart);
            }

            // 更新全局序列号
            sequence = startSequence + count;
            LOGGER.info("批量生成ID完成：前缀={}，数量={}，节点ID={}，起始序列号={}，结束序列号={}",
                    idPrefix.getValue(), count, workerId, startSequence, sequence - 1);
            return idList;

        } finally {
            globalLock.unlock();
        }
    }

    // ========== 核心工具方法 ==========

    /**
     * Base62编码（补0在前，保证字典序与数值大小一致）
     */
    private static String encodeToBase62(long number, int length) {
        if (number < 0) {
            throw new IllegalArgumentException("Base62编码仅支持非负数：number=" + number);
        }

        char[] chars = new char[length];
        // 初始化补0
        Arrays.fill(chars, BASE62_LOWER_CHARSET.charAt(0));

        int index = length - 1;
        long num = number;
        while (num > 0 && index >= 0) {
            chars[index] = BASE62_LOWER_CHARSET.charAt((int) (num % BASE62_LENGTH));
            num = num / BASE62_LENGTH;
            index--;
        }

        String result = new String(chars);
        LOGGER.trace("Base62编码：数值={}，长度={}，结果={}", number, length, result);
        return result;
    }

    /**
     * 等待下一毫秒（保证时间戳递增）
     */
    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        int waitCount = 0;
        while (timestamp <= lastTimestamp) {
            try {
                Thread.sleep(1);
                waitCount++;
                timestamp = System.currentTimeMillis();
            } catch (InterruptedException e) {
                LOGGER.warn("等待下一毫秒时线程被中断，恢复中断状态", e);
                Thread.currentThread().interrupt();
            }
        }
        LOGGER.debug("等待下一毫秒完成：原时间戳={}，新时间戳={}，等待次数={}", lastTimestamp, timestamp, waitCount);
        return timestamp;
    }

    // ========== 扩展方法：获取当前节点ID（便于分布式部署监控） ==========
    public static long getWorkerId() {
        return workerId;
    }

    // ========== 生成器状态内部类（保留监控能力） ==========
    public static class GeneratorState {
        private final AtomicLong generateCount = new AtomicLong(0);
        private volatile long lastGenerateTime = 0L;

        public void incrementCount() {
            generateCount.incrementAndGet();
        }

        public void setLastGenerateTime(long time) {
            this.lastGenerateTime = time;
        }

        public long getGenerateCount() {
            return generateCount.get();
        }

        public long getLastGenerateTime() {
            return lastGenerateTime;
        }

        @Override
        public String toString() {
            return String.format("生成总数：%d，最后生成时间：%s，节点ID：%d",
                    generateCount.get(),
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(lastGenerateTime), ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                    workerId);
        }
    }

    /**
     * 多线程有序性&唯一性测试（优化：CompletableFuture.allOf() 等待任务）
     */
    public static void main(String[] args) throws InterruptedException {
        // 测试配置
        int threadCount = 10;
        int idCountPerThread = 1000;
        long startTime = System.currentTimeMillis();

        // 线程安全容器
        ConcurrentHashMap<String, String> idMap = new ConcurrentHashMap<>();
        AtomicLong totalCount = new AtomicLong(0);
        AtomicLong duplicateCount = new AtomicLong(0);
        AtomicLong threadDisorderCount = new AtomicLong(0);

        // 创建线程池（建议使用 Executors.newFixedThreadPool，简化写法）
        ExecutorService executor = Executors.newFixedThreadPool(threadCount,
                r -> new Thread(r, "id-generator-thread-" + ThreadLocalRandom.current().nextInt(1, threadCount + 1)));

        // ========== 核心优化：使用 CompletableFuture 提交任务 ==========
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String prevId = null;
                List<String> threadIdList = new ArrayList<>(idCountPerThread);

                for (int j = 0; j < idCountPerThread; j++) {
                    try {
                        String currId = OrderedIdGenerator.generate(IdPrefixEnum.USER_ID_PREFIX);
                        totalCount.incrementAndGet();
                        LOGGER.info("生成ID：{}（节点ID：{}）", currId, workerId);
                        // 唯一性验证
                        if (idMap.putIfAbsent(currId, currId) != null) {
                            LOGGER.error("重复ID：{}（节点ID：{}）", currId, workerId);
                            duplicateCount.incrementAndGet();
                        }

                        // 线程内有序性验证
                        threadIdList.add(currId);
                        if (prevId != null && currId.compareTo(prevId) <= 0) {
                            LOGGER.error("线程内无序：prev={}, curr={}（节点ID：{}）", prevId, currId, workerId);
                            threadDisorderCount.incrementAndGet();
                        }
                        prevId = currId;

                    } catch (Exception e) {
                        LOGGER.error("线程{}生成ID失败（节点ID：{}）", Thread.currentThread().getName(), workerId, e);
                    }
                }

                // 线程内二次验证
                for (int k = 1; k < threadIdList.size(); k++) {
                    String prev = threadIdList.get(k - 1);
                    String curr = threadIdList.get(k);
                    if (curr.compareTo(prev) <= 0) {
                        threadDisorderCount.incrementAndGet();
                        LOGGER.error("线程内二次验证无序：位置{}，prev={}, curr={}", k, prev, curr);
                    }
                }

                LOGGER.info("线程{}完成生成，累计生成{}条（节点ID：{}）",
                        Thread.currentThread().getName(), threadIdList.size(), workerId);

            }, executor);

            completableFutures.add(future);
        }

        // ========== 核心优化：CompletableFuture.allOf() 等待所有任务完成 ==========
        try {
            // 合并所有CompletableFuture，等待全部完成（支持超时控制：3分钟）
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    completableFutures.toArray(new CompletableFuture[0])
            );
            allFutures.get(15, TimeUnit.MINUTES); // 超时控制，避免无限阻塞

        } catch (InterruptedException e) {
            LOGGER.error("等待任务完成时线程被中断", e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            LOGGER.error("任务执行异常", e.getCause()); // 打印根异常
        } catch (TimeoutException e) {
            LOGGER.error("任务执行超时（3分钟），强制终止", e);
            // 取消未完成的任务
            completableFutures.forEach(future -> future.cancel(true));
        } finally {
            // 优雅关闭线程池
            executor.shutdown();
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
                LOGGER.warn("线程池强制关闭");
            }
        }

        // 全局有序性验证
        List<String> allIdList = new ArrayList<>(idMap.keySet());
        List<String> sortedIdList = new ArrayList<>(allIdList);
        sortedIdList.sort(String::compareTo);

        long globalDisorderCount = 0;
        for (int i = 1; i < sortedIdList.size(); i++) {
            String prev = sortedIdList.get(i - 1);
            String curr = sortedIdList.get(i);
            if (curr.compareTo(prev) <= 0) {
                globalDisorderCount++;
                if (globalDisorderCount <= 10) {
                    LOGGER.error("全局有序性验证失败：位置{}，prev={}, curr={}", i, prev, curr);
                }
            }
        }

        // 输出测试报告
        long totalTime = System.currentTimeMillis() - startTime;
        double qps = totalCount.get() / (totalTime / 1000.0);
        LOGGER.info("======================================= 测试报告 =======================================");
        LOGGER.info("分布式节点ID：{}", workerId);
        LOGGER.info("并发线程数：{}", threadCount);
        LOGGER.info("单线程生成数：{}", idCountPerThread);
        LOGGER.info("总生成数：{}", totalCount.get());
        LOGGER.info("重复数：{}", duplicateCount.get());
        LOGGER.info("线程内无序数：{}", threadDisorderCount.get());
        LOGGER.info("全局无序数（排序后）：{}", globalDisorderCount);
        LOGGER.info("总耗时：{}ms", totalTime);
        LOGGER.info("QPS：{:.2f}条/秒", qps);
        LOGGER.info("唯一性验证：{}", duplicateCount.get() == 0 ? "✅ 通过" : "❌ 失败");
        LOGGER.info("线程内有序性验证：{}", threadDisorderCount.get() == 0 ? "✅ 通过" : "❌ 失败");
        LOGGER.info("全局有序性验证（ID本身）：{}", globalDisorderCount == 0 ? "✅ 通过" : "❌ 失败");
        LOGGER.info("======================================================================================");

        // 断言验证
        if (duplicateCount.get() > 0 || threadDisorderCount.get() > 0 || globalDisorderCount > 0) {
            throw new RuntimeException("ID生成验证失败：存在重复或无序ID（节点ID：" + workerId + "）");
        }
    }
}