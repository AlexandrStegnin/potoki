package com.art.repository;

import com.art.model.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByLogin(String login);

    void deleteById(Long id);

    List<AppUser> findByIdIn(List<Long> idList);

    @Query(value = "SELECT new com.art.model.AppUser(u.id, u.partner) FROM AppUser u WHERE u.partner IS NOT NULL ")
    List<AppUser> getForFindPartnerChild();

    boolean existsByLogin(String login);

    Page<AppUser> findAll(Specification<AppUser> specification, Pageable pageable);

    @Query("SELECT au FROM AppUser au WHERE au.role.name = 'ROLE_INVESTOR'")
    List<AppUser> getInvestors();

    @Query("SELECT DISTINCT(m.investor) FROM Money m " +
            "WHERE m.givenCash > 0 " +
            "AND m.dateClosing IS NULL " +
            "AND m.typeClosing IS NULL")
    List<AppUser> getSellers();

}
