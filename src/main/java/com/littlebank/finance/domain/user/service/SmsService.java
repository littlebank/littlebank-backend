package com.littlebank.finance.domain.user.service;

import com.littlebank.finance.domain.user.dto.request.CertificationNumberSendRequest;
import com.littlebank.finance.global.coolsms.CoolsmsUtil;
import com.littlebank.finance.global.redis.RedisDao;
import com.littlebank.finance.global.redis.RedisPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final CoolsmsUtil coolsmsUtil;
    private final RedisDao redisDao;

    public void sendCertificationCode(CertificationNumberSendRequest request) {
        String toNumber = request.getToNumber();
        String certificationCode = createRandomNumberSixDigit();
        registerCertificationCodeToRedis(toNumber, certificationCode);
        coolsmsUtil.sendCertificationCode(toNumber, certificationCode);
    }

    private String createRandomNumberSixDigit() {
        return Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
    }

    private void registerCertificationCodeToRedis(String toNumber, String certificationCode) {
        redisDao.setValues(
                RedisPolicy.CERTIFICATION_CODE_KEY_PREFIX + toNumber,
                certificationCode,
                Duration.ofSeconds(RedisPolicy.CERTIFICATION_CODE_TTL)
        );
    }

}
