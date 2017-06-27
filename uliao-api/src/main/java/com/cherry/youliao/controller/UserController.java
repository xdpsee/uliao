package com.cherry.youliao.controller;

import com.cherry.youliao.controller.misc.EmailVerifyToken;
import com.cherry.youliao.controller.ro.UserRegistry;
import com.cherry.youliao.controller.vo.ResponseData;
import com.cherry.youliao.data.enums.IdentityType;
import com.cherry.youliao.data.po.User;
import com.cherry.youliao.data.po.UserAuth;
import com.cherry.youliao.data.repository.Repository;
import com.cherry.youliao.data.service.AuthService;
import com.cherry.youliao.data.service.EmailService;
import com.cherry.youliao.data.service.exception.*;
import com.cherry.youliao.utils.CryptoUtils;
import com.cherry.youliao.utils.JSONUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.nutz.dao.Cnd;
import org.nutz.dao.FieldFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Repository repository;
    @Autowired
    private AuthService authService;
    @Autowired
    private EmailService emailService;
    @Value("${email.verify.url}")
    private String emailVerifyUrl;

    @RequestMapping(value = "/register"
            , method = RequestMethod.POST
            , produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseData<?> register(@RequestBody UserRegistry registry) {
        try {
            authService.register(registry);
        } catch ( EmailExistsException
                | UserNameExistsException
                | InviteCodeNotFoundException
                | InviteCodeWasUsedException
                | InvalidParamsException e) {
            return ResponseData.error(e.getMessage());
        } catch (Exception e) {
            return ResponseData.error("未知错误");
        }

        return ResponseData.success("注册成功.", true);
    }

    @RequestMapping(value = "/verify-email"
            , method = RequestMethod.GET
            , produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseData<?> verifyEmail(@RequestParam("token") String key) {

        try {
            final String content = CryptoUtils.decrypt(key);
            EmailVerifyToken token = JSONUtils.fromJsonString(content, new TypeReference<EmailVerifyToken>(){});
            if (token.getExpiresAt() <= System.currentTimeMillis()) {
                return ResponseData.error("验证已过期,请重新验证");
            }

            final User user = repository.fetch(User.class, Cnd.where("identifier", "=", token.getEmail()));
            if (repository.update(user, FieldFilter.create(User.class, "emailVerified")) > 0) {
                return ResponseData.success("验证通过", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseData.error("无效验证");
    }

    @RequestMapping(value = "/resend-verify-email"
            , method = RequestMethod.GET
            , produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseData<?> resendVerifyEmail(@RequestParam("email") String email) {

        final UserAuth user = repository.fetch(UserAuth.class, Cnd.where("identity_type", "", IdentityType.EMAIL.name())
                .and("identifier", "=", email));
        if (null == user) {
            return ResponseData.error("用户不存在");
        }

        if (user.isVerified()) {
            return ResponseData.error("已验证通过");
        }

        try {
            EmailVerifyToken key = new EmailVerifyToken(email, System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
            String encodedKey = CryptoUtils.encrypt(JSONUtils.toJsonString(key));
            if (emailService.sendVerifyEmail(user, String.format(emailVerifyUrl, encodedKey))) {
                return ResponseData.success(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseData.error("发送验证邮件失败", false);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<User> getUsers() {

        return repository.query(User.class, Cnd.NEW());

    }

}





