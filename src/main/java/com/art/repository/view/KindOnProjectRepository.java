package com.art.repository.view;

import com.art.model.supporting.view.KindOnProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface KindOnProjectRepository extends JpaRepository<KindOnProject, Long> {

    List<KindOnProject> findByLoginOrderByGivenCashDesc(String login);

}
