package com.onlinemarket.OnlinemarketProjectFrontend;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.onlinemarket.OnlinemarketProjectFrontend.setting.EmailSettingBag;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {

    public static String getSiteURL(HttpServletRequest request){
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        
        javaMailSender.setHost(settings.getHost());
        javaMailSender.setPort(settings.getPort());
        javaMailSender.setUsername(settings.getUsername());
        javaMailSender.setPassword(settings.getPassword());

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

        javaMailSender.setJavaMailProperties(mailProperties);

        return javaMailSender;
    }
}
