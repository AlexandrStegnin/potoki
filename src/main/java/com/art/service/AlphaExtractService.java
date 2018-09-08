package com.art.service;

import com.art.model.AlphaCorrectTags;
import com.art.model.AlphaExtract;
import com.art.repository.AlphaExtractRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class AlphaExtractService {
    @Resource(name = "alphaExtractRepository")
    private AlphaExtractRepository alphaExtractRepository;

    public List<AlphaExtract> createList(List<AlphaExtract> alphaExtractList) {
        return alphaExtractRepository.save(alphaExtractList);
    }

    public void updateList(List<AlphaExtract> alphaExtractList) {
        alphaExtractRepository.save(alphaExtractList);
    }

    public AlphaExtract create(AlphaExtract alphaExtract) {
        return alphaExtractRepository.saveAndFlush(alphaExtract);
    }

    public List<AlphaExtract> findAll() {
        return alphaExtractRepository.findAll();
    }

    public void delete() {
        alphaExtractRepository.deleteAll();
    }

    public List<AlphaExtract> finByInnOrAccountOrDescriptionContaining(
            String inn, String account, String description) {
        return alphaExtractRepository.findByInnOrAccountOrPurposePaymentContaining(
                inn, account, description
        );
    }

    public AlphaExtract findById(BigInteger id) {
        return alphaExtractRepository.findOne(id);
    }

    public AlphaExtract update(AlphaExtract alphaExtract) {
        return alphaExtractRepository.saveAndFlush(alphaExtract);
    }

    public void deleteById(BigInteger id) {
        alphaExtractRepository.delete(id);
    }

    public void deleteAllByPIdIsNull() {
        alphaExtractRepository.deleteAllByPIdIsNull();
    }

    public List<AlphaExtract> findByTagsIsNotNull() {
        return alphaExtractRepository.findByTagsIsNotNull();
    }

    public List<AlphaExtract> findByTags(AlphaCorrectTags tags) {
        return alphaExtractRepository.findByTags(tags);
    }

}
