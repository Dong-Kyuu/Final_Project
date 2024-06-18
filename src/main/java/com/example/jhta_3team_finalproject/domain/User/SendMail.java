package com.example.jhta_3team_finalproject.domain.User;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import com.example.jhta_3team_finalproject.domain.User.MailVO;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Random;

@Component
public class SendMail {
    private static final Logger logger = LoggerFactory.getLogger(SendMail.class);
    private static final Random RANDOM = new Random(); // Random 객체를 클래스 변수로 선언
    private JavaMailSenderImpl mailSender;
    public static final String MEMBER_JOIN_MESSAGE = "mbti 회원이 되신 것을 환영합니다.";

    @Value("${jasypt.encryptor.password}")
    private String password;


    @Autowired
    public SendMail(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(MailVO vo) {
        try {
            String changeFrom = jasyptDecrypt(vo.getFrom());
            vo.setFrom(changeFrom);
            MimeMessagePreparator mp = new MimeMessagePreparator() {

                @Override
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    // 두 번째 인자 true는 멀티파트 메시지를 사용하겠다는 의미입니다.
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    helper.setFrom(vo.getFrom());
                    helper.setTo(vo.getTo());
                    helper.setSubject(vo.getSubject());
                    helper.setText(MEMBER_JOIN_MESSAGE);
                }
            };
            mailSender.send(mp);
            logger.info("메일 전송 완료: From={}, To={}, Subject={}", vo.getFrom(), vo.getTo(), vo.getSubject());
        } catch (Exception e) {
            logger.error("메일 전송 실패: {}", e.getMessage(), e);
        }
    }

    private String jasyptDecrypt(String input) {
        try {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setAlgorithm("PBEWithMD5AndDES");
            encryptor.setPassword(password);
            return encryptor.decrypt(input);
        } catch (Exception e) {
            logger.error("이메일 복호화 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    public String randomToken(int length, MailVO mailVO) {
        StringBuilder text = new StringBuilder();
        String token = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            text.append(token.charAt(RANDOM.nextInt(token.length())));
        }
        mailVO.setRandom(text.toString());
        return text.toString();
    }
}
