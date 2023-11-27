package com.example.web_service.web.dto;

import com.example.web_service.web.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    /**
     * 세션에 User 정보를 저장하기 위해서는 자식까지 직렬화 대상에 포함될 수 있는 엔티티가 아닌
     * 직렬화 기능을 가진 세션 Dto를 하나 만들기로 하자.
     * SessionUser에는 인증된 사용자 정보만 필요하다.
     */

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name=user.getName();
        this.email=user.getEmail();
        this.picture=user.getPicture();
    }
}
