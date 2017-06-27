package com.cherry.youliao.service.manager;

import com.cherry.youliao.data.enums.IdentityType;
import com.cherry.youliao.data.po.Authority;
import com.cherry.youliao.data.po.InviteCode;
import com.cherry.youliao.data.po.User;
import com.cherry.youliao.data.po.UserAuth;
import com.cherry.youliao.data.repository.Repository;
import com.cherry.youliao.service.exception.EmailExistsException;
import com.cherry.youliao.service.exception.InviteCodeNotFoundException;
import com.cherry.youliao.service.exception.InviteCodeWasUsedException;
import com.cherry.youliao.service.exception.UserNameExistsException;
import com.cherry.youliao.utils.ExceptionUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.FieldFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class RegistryManager {

    @Autowired
    private Repository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public void registryByUsername(final String username, String password, String inviteCode)
            throws UserNameExistsException, InviteCodeNotFoundException, InviteCodeWasUsedException {

        User user = new User();
        user.setNickname(username);
        try {
            user = repository.insert(user);
        } catch (Exception e) {
            if (ExceptionUtils.hasDuplicateEntryException(e)) {
                throw new UserNameExistsException();
            }
        }
        Authority role = new Authority(user.getId(), "ROLE_USER");
        repository.insert(role);

        UserAuth userAuth = new UserAuth(IdentityType.USERNAME.name()
                , username
                , passwordEncoder.encode(password)
                , user.getId()
                , true);
        repository.insert(userAuth);

        checkoutInviteCode(inviteCode, user.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void registryByEmail(String email, String nickName, String password, String inviteCode) throws UserNameExistsException, EmailExistsException, InviteCodeNotFoundException, InviteCodeWasUsedException {
        User user = new User();
        user.setNickname(nickName);
        try {
            user = repository.insert(user);
        } catch (Exception e) {
            if (ExceptionUtils.hasDuplicateEntryException(e)) {
                throw new UserNameExistsException();
            }
        }

        Authority role = new Authority(user.getId(), "ROLE_USER");
        repository.insert(role);

        UserAuth userAuth = new UserAuth(IdentityType.EMAIL.name()
                , email
                , passwordEncoder.encode(password)
                , user.getId()
                , false);
        try {
            repository.insert(userAuth);
        } catch (Exception e) {
            if (ExceptionUtils.hasDuplicateEntryException(e)) {
                throw new EmailExistsException();
            }
        }

        checkoutInviteCode(inviteCode, user.getId());
    }

    public void registryByMobile(String mobile, String nickName, String password, String inviteCode) {

    }

    private void checkoutInviteCode(final String code, final long userId)
            throws InviteCodeNotFoundException, InviteCodeWasUsedException {
        if (StringUtils.isEmpty(code)) {
            return;
        }

        final InviteCode invite = repository.fetch(InviteCode.class, Cnd.where("invite_code", "=", code));
        if (null == invite) {
            throw new InviteCodeNotFoundException();
        }

        if (invite.getUsedUserId() > 0) {
            throw new InviteCodeWasUsedException();
        }

        invite.setUsedUserId(userId);
        repository.updateWithVersion(code, FieldFilter.create(InviteCode.class,"usedUserId"));
    }

}
