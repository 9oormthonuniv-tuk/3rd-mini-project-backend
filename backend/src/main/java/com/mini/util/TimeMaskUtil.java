package com.mini.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeMaskUtil {

    /**
     * 문자열 비트마스크를 long으로 변환.
     * @param timeMask 비트마스크 (문자열, 예: "000000011000...")
     * @return long 형태의 비트마스크
     */
    public static long convertStringToLong(String timeMask) {
        if (timeMask.length() > 48) {
            throw new IllegalArgumentException("비트마스크는 최대 48비트를 초과할 수 없습니다.");
        }
        // 비트마스크가 48비트로 주어진 경우 그대로 사용
        return Long.parseUnsignedLong(timeMask, 2);
    }

    /**
     * 비트마스크로부터 시작 시간과 종료 시간을 추출.
     * @param timeMask long 타입의 비트마스크
     * @return 시작 시간과 종료 시간
     */
    public static LocalDateTime[] getStartAndEndTime(long timeMask) {
        // 비트마스크를 문자열로 변환 (48비트 패딩)
        String binaryMask = Long.toBinaryString(timeMask);
        binaryMask = String.format("%048d", Long.parseUnsignedLong(binaryMask));

        // 디버깅 출력
        System.out.println("Binary TimeMask: " + binaryMask);

        // 시작 인덱스와 종료 인덱스 계산
        int startIndex = binaryMask.indexOf("1"); // 첫 번째 활성화된 비트의 인덱스
        int endIndex = binaryMask.lastIndexOf("1"); // 마지막 활성화된 비트의 인덱스

        // 디버깅 출력
        System.out.println("Start Index: " + startIndex);
        System.out.println("End Index: " + endIndex);

        if (startIndex == -1 || endIndex == -1) {
            throw new IllegalArgumentException("timeMask에 활성화된 비트가 없습니다.");
        }

        LocalTime startTime = LocalTime.of(startIndex / 2, (startIndex % 2) * 30);

        LocalTime endTime;
        if ((endIndex + 1) / 2 == 24) {
            endTime = LocalTime.of(23, 59, 59);
        } else {
            endTime = LocalTime.of((endIndex + 1) / 2, ((endIndex + 1) % 2) * 30);
        }

        return new LocalDateTime[] {
                LocalDateTime.now().with(startTime),
                LocalDateTime.now().with(endTime)
        };
    }


    /**
     * 시작 시간과 종료 시간을 비트마스크로 변환.
     * @param startTime 시작 시간
     * @param endTime 종료 시간
     * @return 비트마스크 (문자열)
     */
    public static String convertToTimeMask(LocalDateTime startTime, LocalDateTime endTime) {
        int startIndex = startTime.getHour() * 2 + (startTime.getMinute() >= 30 ? 1 : 0);
        int endIndex;

        // 종료 시간이 23:59:59인 경우 마지막 비트를 설정
        if (endTime.getHour() == 23 && endTime.getMinute() == 59) {
            endIndex = 47; // 마지막 비트
        } else {
            endIndex = endTime.getHour() * 2 + (endTime.getMinute() > 0 ? 1 : 0) - 1;
        }

        // 비트마스크 생성 (48비트)
        StringBuilder timeMask = new StringBuilder("0".repeat(48));
        for (int i = startIndex; i <= endIndex; i++) {
            timeMask.setCharAt(i, '1');
        }

        // 디버깅 출력
        System.out.println("Start Index: " + startIndex);
        System.out.println("End Index: " + endIndex);
        System.out.println("Generated TimeMask: " + timeMask.toString());

        return timeMask.toString();
    }
}
