package com.art.repository;

import com.art.model.MailingGroups;
import com.art.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, BigInteger> {
    Users findByLogin(String login);
    Users findByEmailAndPassword(String email, String password);
    void deleteById(BigInteger id);
    List<Users> findByStuffIdOrderByLastName(BigInteger stuffId);
    List<Users> findByEmail(String email);
    List<Users> findByIdIn(List<BigInteger> idList);
    Users findByLastName(String lastName);
    List<Users> findByMailingGroups(MailingGroups mailingGroups);
}
