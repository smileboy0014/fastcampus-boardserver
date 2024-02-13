package com.fastcampus.boardserver.service.impl;

import com.fastcampus.boardserver.dto.UserDTO;
import com.fastcampus.boardserver.exception.DuplicateIdException;
import com.fastcampus.boardserver.mapper.UserProfileMapper;
import com.fastcampus.boardserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.fastcampus.boardserver.utils.SHA256Util.encryptSHA256;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserProfileMapper userProfileMapper;

    @Override
    public void register(UserDTO userProfile) {

        boolean dupIdResult = isDuplicatedId(userProfile.getUserId());

        if (dupIdResult) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }

        userProfile.setCreateTime(new Date());
        userProfile.setPassword(encryptSHA256(userProfile.getPassword()));

        int insertCount = userProfileMapper.register(userProfile);

        if (insertCount != 1) {
            log.error("insert Error! {}", userProfile);
            throw new RuntimeException("회원가입 메서드를 확인해주세요" + userProfile);
        }

    }

    @Override
    public UserDTO login(String id, String password) {
        String cryptoPassword = encryptSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByUserIdAndPassword(id, cryptoPassword);

        return memberInfo;
    }

    @Override
    public boolean isDuplicatedId(String userId) {
        return userProfileMapper.idCheck(userId) == 1;
    }

    @Override
    public UserDTO getUserInfo(String userId) {
        return userProfileMapper.getUserProfile(userId);
    }

    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        String cryptoPassword = encryptSHA256(beforePassword);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);

        if (memberInfo != null) {
            memberInfo.setPassword(encryptSHA256(afterPassword));
            int insertCount = userProfileMapper.updatePassword(memberInfo);

        } else {
            log.error("updatePassword ERROR!!  {}", memberInfo);
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public void deleteId(String id, String password) {
        String cryptoPassword = encryptSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);

        if (memberInfo != null) {
            int deleteCount = userProfileMapper.deleteUserProfile(id);
        } else {
            log.error("delete ERROR!!  {}", memberInfo);
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }
}
