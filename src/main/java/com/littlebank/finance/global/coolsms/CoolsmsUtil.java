package com.littlebank.finance.global.coolsms;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoolsmsUtil {
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecret;
    @Value("${coolsms.from-number}")
    private String fromNumber;
    private DefaultMessageService messageService;

    @PostConstruct
    public void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    /**
     * 단일 메시지 전송
     * @param toNumber 수신자 번호
     * @param certificationCode 인증코드
     */
    public void sendCertificationCode(String toNumber, String certificationCode){
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(toNumber);
        message.setText("[리틀뱅크] 본인확인 인증번호는 [" + certificationCode + "] 입니다.");

        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

}
