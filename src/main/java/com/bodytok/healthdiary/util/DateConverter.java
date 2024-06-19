package com.bodytok.healthdiary.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    private DateConverter() {}

    public static LocalDateTime[] convertToTimeRange(String date, boolean isMonthly) {
        if (isMonthly) {
            // "yyyy-MM" 형식의 문자열을 파싱하여 연도와 월을 얻음
            String[] yearMonth = date.split("-");
            int year = Integer.parseInt(yearMonth[0]);
            int month = Integer.parseInt(yearMonth[1]);

            // 입력된 년도와 월로 해당 월의 시작일과 종료일 계산
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());

            // 해당 월의 시작일과 종료일의 최소 시각과 최대 시각 계산
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            return new LocalDateTime[] { startDateTime, endDateTime };
        }else {
            // "yyyy-MM-dd" 형식의 문자열을 LocalDate로 변환
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

            // 클라이언트에서 전달된 날짜의 최소 시각과 최대 시각 계산
            LocalDateTime startDateTime = localDate.atStartOfDay();
            LocalDateTime endDateTime = localDate.atTime(LocalTime.MAX);

            return new LocalDateTime[] { startDateTime, endDateTime };
        }
    }
}