package com.fastcampus.boardserver.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserDTO {

    public static boolean hasNullDataBeforeRegister(UserDTO userDTO) {
        return userDTO.getUserID() == null || userDTO.getPassword() == null || userDTO.getNickName() == null;
    }

    public enum Status {
        DEFAULT, ADMIN, DELETED
    }


    private int id;

    private String userID;
    private String password;
    private String nickName;
    private boolean isAdmin;
    private Date createTime;
    private boolean isWithDraw;
    private Status status;
    private Date updateTime;

}
