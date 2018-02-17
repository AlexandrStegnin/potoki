package com.art.service;

import com.art.model.AlphaCorrectTags;
import com.art.model.AlphaExtract;
import com.art.repository.AlphaCorrectTagsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
@Repository
public class AlphaCorrectTagsService {
    @Resource(name = "alphaCorrectTagsRepository")
    private AlphaCorrectTagsRepository alphaCorrectTagsRepository;

    @Resource(name = "alphaExtractService")
    private AlphaExtractService alphaExtractService;

    public void createList(List<AlphaCorrectTags> alphaCorrectTagsList) {
        alphaCorrectTagsRepository.save(alphaCorrectTagsList);
    }

    public AlphaCorrectTags create(AlphaCorrectTags alphaCorrectTags) {
        return alphaCorrectTagsRepository.saveAndFlush(alphaCorrectTags);
    }

    public List<AlphaCorrectTags> findAll() {
        return alphaCorrectTagsRepository.findAll();
    }

    public AlphaCorrectTags findById(BigInteger id) {
        return alphaCorrectTagsRepository.findById(id);
    }

    public AlphaCorrectTags update(AlphaCorrectTags alphaCorrectTags) {
        return alphaCorrectTagsRepository.saveAndFlush(alphaCorrectTags);
    }

    public void deleteById(BigInteger id) {
        List<AlphaExtract> alphaExtractList = alphaExtractService.findByTags(
                alphaCorrectTagsRepository.findById(id)
        );

        alphaExtractList.forEach(tag -> tag.setTags(null));
        alphaExtractService.updateList(alphaExtractList);
        alphaCorrectTagsRepository.delete(id);
    }

    public List<AlphaCorrectTags> findTags() {
        return alphaCorrectTagsRepository.findByCorrectTagIsNotNull();
    }

    public AlphaCorrectTags findByInnAndAccountAndCorrectTag(String inn, String account, String correctTag) {
        return alphaCorrectTagsRepository.findByInnAndAccountAndCorrectTag(inn, account, correctTag);
    }

    public AlphaCorrectTags findByCorrectTagAndFacilityId(String correctTag, BigInteger facilityId) {
        return alphaCorrectTagsRepository.findByCorrectTagAndFacilityId(correctTag, facilityId);
    }

    public List<AlphaCorrectTags> findByFacilityId(BigInteger facilityId) {
        return alphaCorrectTagsRepository.findByFacilityId(facilityId);
    }
}
