package org.sang.service;

import org.sang.bean.Role;
import org.sang.bean.User;
import org.sang.mapper.RolesMapper;
import org.sang.mapper.UserMapper;
import org.sang.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * Created by sang on 2017/12/17.
 */
@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RolesMapper rolesMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(s);
        if (user == null) {
            //避免返回null，这里返回一个不含有任何值的User对象，在后期的密码比对过程中一样会验证失败
            return new User();
        }
        //查询用户的角色信息，并返回存入user中
        List<Role> roles = rolesMapper.getRolesByUid(user.getId());
        user.setRoles(roles);
        return user;
    }

    /**
     * @param user
     * @return 0表示成功
     * 1表示用户名重复
     * 2表示失败
     */
    public int reg(User user) {
        User loadUserByUsername = userMapper.loadUserByUsername(user.getUsername());
        if (loadUserByUsername != null) {
            return 1;
        }
        //插入用户,插入之前先对密码进行加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setEnabled(true);//用户可用
        long result = userMapper.reg(user);
        //配置用户的角色，默认都是普通用户
        String[] roles = new String[]{"2"};
        int i = rolesMapper.addRoles(roles, user.getId());
        boolean b = i == roles.length && result == 1;
        if (b) {
            return 0;
        } else {
            return 2;
        }
    }

    public int updateUserEmail(String email) {
        return userMapper.updateUserEmail(email, Util.getCurrentUser().getId());
    }

    public List<User> getUserByNickname(String nickname) {
        List<User> list = userMapper.getUserByNickname(nickname);
        return list;
    }

    public List<Role> getAllRole() {
        return userMapper.getAllRole();
    }

    public int updateUserEnabled(Boolean enabled, Long uid) {
        return userMapper.updateUserEnabled(enabled, uid);
    }

    public int deleteUserById(Long uid) {
        return userMapper.deleteUserById(uid);
    }

    public int updateUserRoles(Long[] rids, Long id) {
        int i = userMapper.deleteUserRolesByUid(id);
        return userMapper.setUserRoles(rids, id);
    }

    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }


    public String isAttention(String username, Long aid) {
        User user = userMapper.loadUserByNickname(username);
        String aids = user.getAttention_qids();
        String[] aidArray = aids.split(",");

        String resultAids = "";
        boolean flag = false;
        // 判断是否关注过某问题
        for (int i = 0; i < aidArray.length; i++) {
            if (aid.toString().equals(aidArray[i])) {
                flag = true;
            } else {
                resultAids = resultAids.concat("," + aidArray[i]);
            }
        }
        if (flag) {
            // 取消关注
            if (resultAids.indexOf(",") == 0) {
                userMapper.attention(username, resultAids.substring(1, resultAids.length()));
            } else {
                userMapper.attention(username, resultAids);
            }

            return resultAids;
        } else {
            // 关注问题
            aids = aids.concat("," + aid.toString());
            if (aids.indexOf(",") == 0) {
                userMapper.attention(username, aids.substring(1, aids.length()));
            } else {
                userMapper.attention(username, aids);
            }

            return aids;
        }
    }

    public User loadUserByNickname(String nickname) {
        return userMapper.loadUserByNickname(nickname);
    }

    /**
     *
     * @param id 当前用户的id
     * @param uid 要关注的用户id
     */
    public void attentionUser(Long id, Long uid) {
        // 获取该用户已经关注的用户id
        String usersid = userMapper.getUserById(id).getAttention_ids();
        String[] userArray = usersid.split(",");

        String result = "";
        boolean flag = false;
        // 判断是否关注过此用户
        for (int i = 0; i < userArray.length; i++) {
            if (uid.toString().equals(userArray[i])) {
                flag = true;
            } else {
                result = result.concat("," + userArray[i]);
            }
        }
        if (flag) {
            // 取消关注用户
            if (result.indexOf(",") == 0) {
                userMapper.attentionUser(id, result.substring(1, result.length()));
            } else {
                userMapper.attentionUser(id, result);
            }
        } else {
            // 关注用户
            usersid = usersid.concat("," + uid.toString());
            if (usersid.indexOf(",") == 0) {
                userMapper.attentionUser(id, usersid.substring(1, usersid.length()));
            } else {
                userMapper.attentionUser(id, usersid);
            }
        }
    }

    public void attentionTopic(Long cid, Long uid) {
        // 获取该用户已经关注的话题 id
        String cids = userMapper.getUserById(uid).getAttention_cids();
        String[] cidArray = cids.split(",");

        String result = "";
        boolean flag = false;
        // 判断是否关注过此话题
        for (int i = 0; i < cidArray.length; i++) {
            if (cid.toString().equals(cidArray[i])) {
                flag = true;
            } else {
                result = result.concat("," + cidArray[i]);
            }
        }
        if (flag) {
            // 取消关注话题
            if (result.indexOf(",") == 0) {
                userMapper.attentionTopic(uid, result.substring(1, result.length()));
            } else {
                userMapper.attentionTopic(uid, result);
            }
        } else {
            // 关注话题
            cids = cids.concat("," + cid.toString());
            if (cids.indexOf(",") == 0) {
                userMapper.attentionTopic(uid, cids.substring(1, cids.length()));
            } else {
                userMapper.attentionTopic(uid, cids);
            }
        }

    }
}
