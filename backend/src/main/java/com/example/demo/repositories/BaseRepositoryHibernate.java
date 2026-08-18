package com.example.demo.repositories;

import com.example.demo.entities.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseRepositoryHibernate extends JpaRepository<BaseUser, Long> {

    Optional<List<BaseUser>> findByFirstName(String firstName);
}
