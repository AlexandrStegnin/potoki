package com.art.service;

import com.art.model.ToshlCorrectTags;
import com.art.repository.ToshlCorrectTagsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class ToshlCorrectTagsService {

    @Resource(name = "toshlCorrectTagsRepository")
    private ToshlCorrectTagsRepository toshlCorrectTagsRepository;

    public List<ToshlCorrectTags> createList(List<ToshlCorrectTags> toshlCorrectTagss) {
        return toshlCorrectTagsRepository.save(toshlCorrectTagss);
    }

    public ToshlCorrectTags create(ToshlCorrectTags toshlCorrectTags) {
        return toshlCorrectTagsRepository.
                saveAndFlush(toshlCorrectTags);
    }

    public List<ToshlCorrectTags> findAll() {
        return toshlCorrectTagsRepository.findAll();
    }

    public ToshlCorrectTags find(BigInteger id) {
        return toshlCorrectTagsRepository.findOne(id);
    }

    public ToshlCorrectTags update(ToshlCorrectTags toshlCorrectTags) {
        return toshlCorrectTagsRepository.saveAndFlush(toshlCorrectTags);
    }

    public void deleteById(BigInteger id) {
        toshlCorrectTagsRepository.delete(id);
    }

    public void delete() {
        toshlCorrectTagsRepository.deleteAll();
    }

    public List<ToshlCorrectTags> findOrderBy() {
        return toshlCorrectTagsRepository.findByCorrectTagNotNullOrderByDateStTag();
    }

}
