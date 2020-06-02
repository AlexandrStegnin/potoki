package com.art.repository.view;

import com.art.model.supporting.view.InvestorAnnex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface InvestorAnnexRepository extends JpaRepository<InvestorAnnex, Long> {
}
