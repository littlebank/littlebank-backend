package com.littlebank.finance.global.business;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointPolicy {
    public final static int EXCHANGE_FEE_EXEMPTION_AMOUNT = 30000;
    public final static int CHILD_COMMISSION = 200;
}
