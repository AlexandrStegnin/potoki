package com.art.controllers.view;

import com.art.model.supporting.view.KindOnProject;
import com.art.service.view.KindOnProjectService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@RestController
public class KindOnProjectController {

    private final KindOnProjectService kindOnProjectService;

    public KindOnProjectController(KindOnProjectService kindOnProjectService) {
        this.kindOnProjectService = kindOnProjectService;
    }

    @PostMapping(path = "/kind-on-project")
    public List<KindOnProject> findByInvestorLogin(@RequestBody(required = false) String login) {
        return kindOnProjectService.findByInvestorLogin(login);
    }

}
