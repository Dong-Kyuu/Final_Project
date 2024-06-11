package com.example.jhta_3team_finalproject.domain.TourPackage;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;


@Component("tourPackageSendMail")
public class tripSendMail {
    private JavaMailSenderImpl mailSender;

    @Autowired
    public tripSendMail(JavaMailSenderImpl mailSender) {
        this.mailSender=mailSender;
    }

    private static final Logger logger = LoggerFactory.getLogger(tripSendMail.class);

    public void sendMail(tripMailVO vo)   {

        MimeMessagePreparator mp= new MimeMessagePreparator()  {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception{
                //두 번째 인자true는 멀티파트 메시지를 사용하겠다는 의미입니다.
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"UTF-8");
                helper.setFrom(vo.getFrom());
                helper.setTo(vo.getTo());
                helper.setSubject(vo.getSubject());

                //1.문자로만 전송하는경우
                //두번째 인자는html을 사용하겠다는 뜻 입니다
                helper.setText("회원가입을축하합니다");    //true);

            }
        };
        mailSender.send(mp);//메일 전송합니다.
        logger.info("메일 전송 했습니다");

    }
    public String randomToken(int length, tripMailVO tripMailVO){
        String text="";
        String ToKen="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for(int i=0; i< length;  i++){
            text += ToKen.charAt((int)(Math.random()*ToKen.length()));
        }
        tripMailVO.setRandom(text);
        return text;
    }

}
