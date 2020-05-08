package com.art.service.view;

import com.art.model.supporting.view.KindProjectOnAllMonies;
import com.art.repository.view.KindProjectOnAllMoniesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class KindProjectOnAllMoniesService {

    private final KindProjectOnAllMoniesRepository repository;

    public KindProjectOnAllMoniesService(KindProjectOnAllMoniesRepository repository) {
        this.repository = repository;
    }

    public List<KindProjectOnAllMonies> findByLogin(String login) {
        return repository.findByLogin(login);
    }

}
