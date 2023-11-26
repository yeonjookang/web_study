package com.example.web_service.web.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    //특별한 쿼리 메서드를 사용하기 위해서는 인터페이스에 따로 선언(정의는 필요 없음)을 해야한다!
    Optional<User> findByEmail(String email);
}
