package com.gilgamesh.common.utils;

import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.exceptions.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 时间工具类
 * @createDate 2024/10/5 11:06
 * @since 1.0.0
 */
public class TimeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtil.class);

    private TimeUtil() {
    }

    /**
     * 日期时间 年, 月, 日, 时, 分, 秒, 毫秒 域
     */
    private static final List<ChronoField> DATE_TIME_FIELD_LIST = List.of(ChronoField.YEAR,
            ChronoField.MONTH_OF_YEAR,
            ChronoField.DAY_OF_MONTH,
            ChronoField.HOUR_OF_DAY,
            ChronoField.MINUTE_OF_HOUR,
            ChronoField.SECOND_OF_MINUTE,
            ChronoField.MILLI_OF_SECOND);

    /**
     * 判断日期时间是否有效
     *
     * @param strDateTime    String 日期时间 如 2022-12-01 17:26:17, 2022-12-01, 17:53:24, 17:37
     * @param dateTimeFormat String 时间格式 如 yyyy-MM-dd HH:mm:ss, yyyy-MM-dd, HH:mm:ss, HH:mm
     * @return boolean True-日期时间有效 False-日期时间无效
     */
    public static boolean isDateTimeValid(String strDateTime, String dateTimeFormat) {
        try {
            if (StringUtil.isEmpty(strDateTime)
                    || StringUtil.isEmpty(dateTimeFormat)) {
                return false;
            }
            // // 使用了 uuuu 而不是 yyyy 即 YEAR而不是 YEAR_OF_ERA, 从而避免 将 2022-02-30 这样无效的日期 自动更正为一个有效日期
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat.replaceAll("[yY]", "u"))
                    .withResolverStyle(ResolverStyle.STRICT);
            final TemporalAccessor dateTimeFieldAccessor = dateTimeFormatter.parse(strDateTime);
            // 日期时间仅有 年, 月, 日, 时, 分, 秒, 毫秒时，校验是否在有效范围内
            return DATE_TIME_FIELD_LIST
                    .stream()
                    .filter(dateTimeFieldAccessor::isSupported)
                    .allMatch(field -> field.range().isValidValue(dateTimeFieldAccessor.get(field)));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断日期时间是否无效
     *
     * @param strDateTime    String 日期时间 如 2022-12-01 17:26:17, 2022-12-01, 17:53:24, 17:37
     * @param dateTimeFormat String 时间格式 如 yyyy-MM-dd HH:mm:ss, yyyy-MM-dd, HH:mm:ss, HH:mm
     * @return boolean True-日期时间无效 False-日期时间有效
     */
    public static boolean isDateTimeInValid(String strDateTime, String dateTimeFormat) {
        return !isDateTimeValid(strDateTime, dateTimeFormat);
    }

    /**
     * 校验日期范围是否无效
     *
     * @param startDate      开始日期 如 2022-12-01
     * @param endDate        结束日期 如 2022-12-30
     * @param dateTimeFormat String 字符串日期格式 如 yyyyMMdd
     * @param bothRequired   boolean True- 日期范围开始日期与结束日期都必须指定 False-日期范围开始日期与结束日期二者可选
     * @return boolean True- 日期范围参数无效 False- 日期范围参数有效
     */
    public static boolean isRangeDateInvalid(String startDate,
                                             String endDate,
                                             String dateTimeFormat,
                                             boolean bothRequired) {
        try {
            if (StringUtil.isEmpty(dateTimeFormat)) {
                LOGGER.error("error message: 待校验日期范围<字符串日期格式> <{}> 无效", dateTimeFormat);
                return true;
            }
            // 开始日期与结束日期两者都必须指定的场合
            if (bothRequired) {
                return isRangeDateInvalid(startDate, endDate, dateTimeFormat);
            }
            // 开始日期与结束日期两者两者可选
            else {
                if (StringUtil.isEmpty(startDate)
                        && StringUtil.isEmpty(endDate)) {
                    return false;
                } else if (!StringUtil.isEmpty(startDate)
                        && StringUtil.isEmpty(endDate)) {
                    return !isDateTimeValid(startDate, dateTimeFormat);
                } else if (StringUtil.isEmpty(startDate)
                        && !StringUtil.isEmpty(endDate)) {
                    return !isDateTimeValid(endDate, dateTimeFormat);
                } else {
                    return isRangeDateInvalid(startDate, endDate, dateTimeFormat);
                }
            }
        } catch (Exception e) {
            LOGGER.error("error message: 待校验期范围异常, 原因是:", e);
            return true;
        }
    }

    /**
     * 校验日期范围
     *
     * @param startDate  String 开始日期
     * @param endDate    String 结束日期
     * @param dateFormat 字符串日期格式 如 yyyyMMdd
     * @return boolean True-时间间隔无效 False—时间间隔有效
     */
    private static boolean isRangeDateInvalid(String startDate,
                                              String endDate,
                                              String dateFormat) {
        if (StringUtil.isEmpty(startDate)
                || StringUtil.isEmpty(endDate)
                || StringUtil.isEmpty(dateFormat)) {
            LOGGER.error("error message: 待校验的日期范围<开始日期> <{}>, <结束日期> <{}>, <字符串日期格式> <{}>", startDate, endDate, dateFormat);
            return true;
        }
        try {
            if (!isDateTimeValid(startDate, dateFormat)
                    || !isDateTimeValid(endDate, dateFormat)) {
                LOGGER.error("error message: 待校验的日期范围<开始日期> <{}>, <结束日期> <{}> 不是对应的日期格式 <字符串日期格式> <{}>", startDate, endDate, dateFormat);
                return true;
            }
            return dateCompare(startDate, endDate, dateFormat) > 0;
        } catch (Exception e) {
            LOGGER.error("error message: 待校验校验校验日期范围异常,原因:", e);
            return true;
        }
    }

    /**
     * str 类型的日期比较
     *
     * @param str1           String 日期1
     * @param str2           String 日期2
     * @param dateTimeFormat String 日期格式
     * @return int <0: 日期1 < 日期2; 0: 日期1 = 日期2; >0: 日期1 > 日期2
     * @throws BizException 自定义异常
     */
    public static int dateCompare(String str1, String str2, String dateTimeFormat) throws BizException {
        if (StringUtil.isEmpty(str1) || StringUtil.isEmpty(str2)) {
            throw new BizException(BizCodeMsg.DATE_CONVERT_FAILED);
        }
        try {
            LocalDate date1 = stringToLocalDate(str1, dateTimeFormat);
            LocalDate date2 = stringToLocalDate(str2, dateTimeFormat);
            return date1.compareTo(date2);
        }
        // 异常
        catch (Exception e) {
            LOGGER.error("error message: 日期比较异常,原因是:", e);
            throw new BizException(BizCodeMsg.DATE_CONVERT_FAILED);
        }
    }

    /**
     * 将特定格式的字符串日期转为日期形式
     *
     * @param str        Date
     * @param dateFormat String
     * @return String
     * @throws BizException 自定义异常
     */
    public static LocalDate stringToLocalDate(String str, String dateFormat) throws BizException {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
            return LocalDate.parse(str, dateTimeFormatter);
        } catch (Exception e) {
            LOGGER.error("error message: String 格式日期 <{}> 转换为 LocalDate 格式 <{}> 日期时间异常,原因是:", str, dateFormat, e);
            throw new BizException(BizCodeMsg.DATE_CONVERT_FAILED);
        }
    }
}
