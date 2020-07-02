package com.art.repository;

import com.art.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByLogin(String login);

    void deleteById(Long id);

    List<Users> findByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.art.model.Users(u.id, u.partnerId) FROM Users u WHERE u.partnerId IS NOT NULL ")
    List<Users> getForFindPartnerChild();
}
