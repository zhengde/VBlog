package org.sang.controller;

import org.sang.bean.RespBean;
import org.sang.bean.User;
import org.sang.service.UserService;
import org.sang.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/currentUserName")
    public String currentUserName() {
        return Util.getCurrentUser().getNickname();
    }

    @RequestMapping("/currentUserId")
    public Long currentUserId() {
        return Util.getCurrentUser().getId();
    }

    @RequestMapping("/currentUserEmail")
    public String currentUserEmail() {
        return Util.getCurrentUser().getEmail();
    }

    @RequestMapping("/isAdmin")
    public Boolean isAdmin() {
        List<GrantedAuthority> authorities = Util.getCurrentUser().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().contains("超级管理员")) {
                return true;
            }
        }
        return false;
    }


    @RequestMapping(value = "/updateUserEmail", method = RequestMethod.PUT)
    public RespBean updateUserEmail(String email) {
        if (userService.updateUserEmail(email) == 1) {
            return new RespBean("success", "开启成功!");
        }
        return new RespBean("error", "开启失败!");
    }

    /**
     * 是否关注某一问题，是则取消关注，否则关注
     *
     * @param username
     * @param aid
     * @return
     */
    @RequestMapping(value = "/isAttention", method = RequestMethod.GET)
    public String isAttention(String username, Long aid) {
        return userService.isAttention(username, aid);
    }


    @RequestMapping(value = "/loadAttention", method = RequestMethod.GET)
    public UserDetails loadAttention(String username) {
        return userService.loadUserByNickname(username);
    }

    @RequestMapping(value = "/attentionUser", method = RequestMethod.POST)
    public void attentionUser(@RequestParam Long id, @RequestParam Long uid) {
        userService.attentionUser(id, uid);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


}
