package com.art.converter;

import com.art.model.supporting.enums.ShareType;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Alexandr Stegnin
 */

public class ShareTypeConverter implements Converter<String, ShareType> {

    public ShareType convert(String id) {
        ShareType shareType;
        try {
            int intId = Integer.parseInt(id);
            shareType = ShareType.fromId(intId);
        } catch (Exception ex) {
            shareType = ShareType.fromTitle(id);
        }
        return shareType;
    }

}
