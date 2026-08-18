package com.example.demo.repositories;

import com.example.demo.entities.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseRepositoryJPA extends JpaRepository<BaseUser, Long> {


    @Query("INSERT INTO BaseUser (firstName, lastName) VALUES (:firstName,:lastName)")
    List<BaseUser> save(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT bu FROM BaseUser bu WHERE bu.firstName = :firstName")
    Optional<BaseUser> findByFirstName(@Param("firstName") String firstName);

    @NativeQuery("SELECT * FROM base_users bu LIMIT ?1")
    List<BaseUser> listUsersScope(@Param("scope") Integer scope);
}
