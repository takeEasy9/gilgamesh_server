package com.gilgamesh.common.utils;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 雪花ID生成器
 * @createDate 2026/1/25 9:35
 * @since 1.0.0
 */
public class SnowflakeIdGenerator {
    // 固定配置：dataCenterId=0、machineId=0
    private final long dataCenterId = 0;
    private final long machineId = 0;

    // 起始时间戳（自定义，建议设为项目上线时间，示例：2026-01-01 00:00:00 UTC+8）
    private final long twepoch = 1735622400000L;

    // 雪花算法核心位段配置
    private final long dataCenterIdBits = 5L;
    private final long machineIdBits = 5L;
    private final long sequenceBits = 12L;

    // 位移计算
    private final long machineIdShift = sequenceBits;
    private final long dataCenterIdShift = sequenceBits + machineIdBits;
    private final long timestampLeftShift = sequenceBits + machineIdBits + dataCenterIdBits;
    private final long sequenceMask = ~(-1L << sequenceBits);

    // 并发控制变量
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    // 单例模式（线程安全）
    private static class SingletonHolder {
        private static final SnowflakeIdGenerator INSTANCE = new SnowflakeIdGenerator();
    }

    public static SnowflakeIdGenerator getInstance() {
        return SingletonHolder.INSTANCE;
    }

    // 私有构造器：校验dataCenterId和machineId范围
    private SnowflakeIdGenerator() {
        if (dataCenterId > ((1 << dataCenterIdBits) - 1) || dataCenterId < 0) {
            throw new IllegalArgumentException("DataCenter ID can't be greater than 31 or less than 0");
        }
        if (machineId > ((1 << machineIdBits) - 1) || machineId < 0) {
            throw new IllegalArgumentException("Machine ID can't be greater than 31 or less than 0");
        }
    }

    // 核心方法1：生成当前系统时间的ID（简化调用）
    public synchronized long nextId() {
        return nextId(System.currentTimeMillis());
    }

    // 核心方法2：支持传入自定义时间戳生成ID（兼容原有逻辑）
    public synchronized long nextId(long customTimestamp) {
        long timestamp = customTimestamp;

        // 时钟回拨校验：防止时间倒退导致ID重复
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 同一毫秒内生成多个ID，序列号递增（最多4096个/毫秒）
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 序列号溢出，等待下一个毫秒
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒，序列号重置为0
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // 拼接64位雪花ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (machineId << machineIdShift)
                | sequence;
    }

    // 等待下一个毫秒（解决序列号溢出问题）
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    // 解析雪花ID：验证参数和生成时间
    public static void parseSnowflakeId(long id, long twepoch) {
        // 解析各部分参数
        long dataCenterId = (id >> 17) & 0x1F; // 数据中心ID：右移17位，取低5位
        long machineId = (id >> 12) & 0x1F;    // 机器ID：右移12位，取低5位
        long sequence = id & 0xFFF;            // 序列号：取低12位
        long timestamp = (id >> 22) + twepoch; // 生成时间戳：右移22位 + 起始时间戳

        // 输出解析结果
        System.out.println("===== 雪花ID解析结果 =====");
        System.out.println("生成的ID：" + id);
        System.out.println("数据中心ID（dataCenterId）：" + dataCenterId);
        System.out.println("机器ID（machineId）：" + machineId);
        System.out.println("序列号：" + sequence);
        System.out.println("生成时间：" + new java.util.Date(timestamp) + "（系统当前时间）");
    }

    // 测试方法：基于系统当前时间生成ID
    public static void main(String[] args) {
        // 1. 获取雪花算法实例（dataCenterId=0、machineId=0）
        SnowflakeIdGenerator generator = SnowflakeIdGenerator.getInstance();

        // 2. 生成基于系统当前时间的ID（简化调用，无需手动传时间戳）
        long snowflakeId = generator.nextId();

        // 3. 输出ID
        System.out.println("生成的雪花ID示例（dataCenterId=0、machineId=0）：" + snowflakeId);

        // 4. 解析验证（传入起始时间戳twepoch=1735622400000L）
        parseSnowflakeId(snowflakeId, 1735622400000L);
    }
}
