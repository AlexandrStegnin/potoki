package com.art.repository.view;

import com.art.model.supporting.view.InvestorProfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface InvestorProfitRepository extends JpaRepository<InvestorProfit, Long> {

    List<InvestorProfit> findByLoginOrderByYearSale(String login);

}
