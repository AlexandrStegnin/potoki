package com.art.service;

import com.art.model.Stuffs;
import com.art.repository.StuffRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Repository
public class StuffService {

    //@Autowired
    @Resource(name = "stuffRepository")
    private StuffRepository stuffRepository;

    public List<Stuffs> findAll() {
        return stuffRepository.findAll();
    }

    public Stuffs findById(BigInteger id){
        return stuffRepository.findOne(id);
    }

    public Stuffs update(Stuffs stuff){
        return stuffRepository.saveAndFlush(stuff);
    }

    public Stuffs findByStuff(String stuff){
        return stuffRepository.findByStuff(stuff);
    }

    public List<Stuffs> initializeStuffs(){
        Stuffs stuff = new Stuffs("0", "Все");
        List<Stuffs> stuffs = new ArrayList<>(0);
        stuffs.add(stuff);
        stuffs.addAll(stuffRepository.findAll());
        return stuffs;
    }
}
