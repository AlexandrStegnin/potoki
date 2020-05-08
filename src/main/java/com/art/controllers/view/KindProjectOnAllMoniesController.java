package com.art.controllers.view;

import com.art.model.supporting.view.KindProjectOnAllMonies;
import com.art.service.view.KindProjectOnAllMoniesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@RestController
public class KindProjectOnAllMoniesController {

    private final KindProjectOnAllMoniesService kindProjectOnAllMoniesService;

    public KindProjectOnAllMoniesController(KindProjectOnAllMoniesService kindProjectOnAllMoniesService) {
        this.kindProjectOnAllMoniesService = kindProjectOnAllMoniesService;
    }

    @GetMapping(path = "/kind-project-on-monies")
    public List<KindProjectOnAllMonies> findByInvestorLogin() {
        return kindProjectOnAllMoniesService.findByLogin("investor007");
    }

}
