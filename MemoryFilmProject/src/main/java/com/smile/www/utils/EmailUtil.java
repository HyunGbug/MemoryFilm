package com.smile.www.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
    public static void sendEmail(String recipientEmail, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        final String sendEmail = ""; // 발신자 이메일
        final String password = ""; // 앱 비밀번호

        //1. 프로퍼티 생성
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // 이메일 발송을 처리해 줄 SMTP 서버를 나타냄
        props.put("mail.smtp.port", "587"); // 서버와 통신하는 포트를 의미
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 추가
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        //2. 세션 설정
        // SMTP 서버 정보와 사용자 정보를 기반으로 Session 클래스의 인스턴스 생성
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendEmail, password);
            }
        });

        //3. 메시지 작성
        try {
        	MimeMessage message = new MimeMessage(session);
            try {
				message.setFrom(new InternetAddress(sendEmail, "관리자", "utf-8"));
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			}
            //수신자 메일 주소
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            //메일 제목
            message.setSubject(subject);
            //메일 내용
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message); // 메일 전송

            System.out.println("임시 비밀번호가 메일로 성공적으로 전송되었습니다.");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new MessagingException("메일 전송 중 오류가 발생했습니다.");
        }
    }
}
