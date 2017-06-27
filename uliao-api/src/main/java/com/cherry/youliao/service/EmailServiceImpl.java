package com.cherry.youliao.service;

import com.cherry.youliao.data.po.UserAuth;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailServiceImpl implements EmailService {

    @Value("${email.smtp.hostname}")
    private String hostName;

    @Value("${email.smtp.port}")
    private String port;

    @Value("${email.smtp.use-ssl}")
    private Boolean useSSL;

    @Value("${email.auth.user}")
    private String account;

    @Value("${email.auth.password}")
    private String password;

    @Override
    public boolean sendVerifyEmail(UserAuth user, String link) {

        HtmlEmail email = new HtmlEmail();
        email.setHostName(hostName);
        email.setStartTLSEnabled(true);
        email.setAuthenticator(new DefaultAuthenticator(account, password));
        try {
            email.setCharset("UTF-8");
            email.addTo(user.getIdentifier(), user.getIdentifier());
            email.setFrom(account, "奇妙运营");
            email.setSubject("请验证您的电子邮箱！");
            email.setHtmlMsg(htmlContent(user, link));

            email.send();
            System.out.println("发送成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private String htmlContent(UserAuth user, String link) throws IOException {

        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("template/");
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate("verify.email.html");

        Map<String, String> params = new HashMap<>();
        params.put("identifier", user.getIdentifier());
        params.put("email", user.getIdentifier());
        params.put("link", link);

        t.binding(params);

        return t.render();

    }
}


