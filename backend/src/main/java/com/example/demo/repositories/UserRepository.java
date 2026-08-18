package com.example.demo.repositories;

import com.example.demo.entities.BaseUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<BaseUser, Long> {


    @Query("SELECT bu FROM BaseUser bu WHERE bu.username = :username")
    Optional<BaseUser> findByUsername(@Param("username")String username);

}
