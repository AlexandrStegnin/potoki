package com.art.specifications;

import com.art.model.AppRole_;
import com.art.model.AppUser;
import com.art.model.AppUser_;
import com.art.model.UserProfile_;
import com.art.model.supporting.filters.AppUserFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * @author Alexandr Stegnin
 */

@Component
public class AppUserSpecification extends BaseSpecification<AppUser, AppUserFilter> {

    @Override
    public Specification<AppUser> getFilter(AppUserFilter filter) {
        return (root, query, cb) -> where(
                loginEqual(filter.getLogin()))
                .and(isDeactivated(filter.isDeactivated()))
                .and(roleEqual(filter.getRole()))
                .toPredicate(root, query, cb);
    }

    private static Specification<AppUser> loginEqual(String login) {
        if (login == null || "Выберите инвестора".equalsIgnoreCase(login)) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AppUser_.login), login)
            );
        }
    }

    private static Specification<AppUser> isDeactivated(boolean deactivated) {
        if (!deactivated) {
            return null;
        }
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(AppUser_.profile).get(UserProfile_.locked), true)
        );
    }

    private static Specification<AppUser> roleEqual(String role) {
        if (role == null || "Выберите роль".equalsIgnoreCase(role)) {
            return null;
        } else {
            return ((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AppUser_.role).get(AppRole_.humanized), role)
            );
        }
    }

}
