package com.art.service;

import com.art.model.ToshlExtract;
import com.art.repository.ToshlExtractRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Repository
public class ToshlExtractService {
    @Resource(name = "toshlExtractRepository")
    private ToshlExtractRepository toshlExtractRepository;

    public List<ToshlExtract> createList(List<ToshlExtract> toshlExtracts){
        return toshlExtractRepository.save(toshlExtracts);
    }

    public ToshlExtract create(ToshlExtract toshlExtract){
        return toshlExtractRepository.
                saveAndFlush(toshlExtract);
    }

    public List<ToshlExtract> findAll(){
        return toshlExtractRepository.findAll();
    }

    public void delete(){
        toshlExtractRepository.deleteAll();
    }
}
