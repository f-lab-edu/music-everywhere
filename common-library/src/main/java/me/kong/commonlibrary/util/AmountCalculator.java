package me.kong.commonlibrary.util;

import java.math.BigDecimal;

public class AmountCalculator {


    /**
     * 1인 증가 당 100원
     * 금액 설정을 별도의 파일로 분리 필요
     * @param additionalMembers
     * @return
     */
    public static BigDecimal getGroupIncreaseAmount(Integer additionalMembers) {
        return new BigDecimal(additionalMembers * 100);
    }
}
