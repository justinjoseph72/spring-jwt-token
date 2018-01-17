package com.yoti.app.oauthdemojwt.repos;

import com.yoti.app.oauthdemojwt.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
}
