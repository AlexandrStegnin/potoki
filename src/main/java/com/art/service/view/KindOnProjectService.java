package com.art.service.view;

import com.art.model.supporting.view.KindOnProject;
import com.art.repository.view.KindOnProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class KindOnProjectService {

    private final KindOnProjectRepository kindOnProjectRepository;

    public KindOnProjectService(KindOnProjectRepository kindOnProjectRepository) {
        this.kindOnProjectRepository = kindOnProjectRepository;
    }

    public List<KindOnProject> findByInvestorLogin(String login) {
        return kindOnProjectRepository.findByLoginOrderByBuyDate(login);
    }

}
