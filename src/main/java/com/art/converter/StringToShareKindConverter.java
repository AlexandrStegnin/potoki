package com.art.converter;

import com.art.model.ShareKind;
import com.art.service.ShareKindService;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToShareKindConverter implements Converter<String, ShareKind> {

    @Resource(name = "shareKindService")
    private ShareKindService shareKindService;

    public StringToShareKindConverter(ShareKindService shareKindService) {
        this.shareKindService = shareKindService;
    }

    public ShareKind convert(String id) {
        ShareKind shareKind;
        try {
            BigInteger IntId = new BigInteger(id);
            shareKind = shareKindService.findById(IntId);
        } catch (Exception ex) {
            shareKind = shareKindService.findByShareKind(id);
        }

        return shareKind;
    }

}
