package com.art.repository.view;

import com.art.model.supporting.view.KindProjectOnAllMonies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface KindProjectOnAllMoniesRepository extends JpaRepository<KindProjectOnAllMonies, Long> {

    List<KindProjectOnAllMonies> findByLogin(String login);

}
