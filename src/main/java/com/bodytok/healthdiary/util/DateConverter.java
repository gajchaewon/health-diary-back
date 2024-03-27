package com.bodytok.healthdiary.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    private DateConverter() {}

    public static LocalDateTime[] convertDateToTimeRange(String date) {
        // "yyyy-MM-dd" 형식의 문자열을 LocalDate로 변환
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        // 클라이언트에서 전달된 날짜의 최소 시각과 최대 시각 계산
        LocalDateTime startDateTime = localDate.atStartOfDay();
        LocalDateTime endDateTime = localDate.atTime(LocalTime.MAX);

        return new LocalDateTime[] { startDateTime, endDateTime };
    }
}