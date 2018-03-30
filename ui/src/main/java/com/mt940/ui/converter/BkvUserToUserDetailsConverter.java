package com.mt940.ui.converter;

import com.mt940.domain.permission.BKVInstance;
import com.mt940.domain.permission.BKVUser;
import com.mt940.ui.domain.UserDetails;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class BkvUserToUserDetailsConverter implements Converter<BKVUser, UserDetails> {
    @Override
    public UserDetails convert(BKVUser source) {
        UserDetails result = new UserDetails();

        result.setId(source.getId());
        result.setLogin(source.getLogin());
        result.setDescription(source.getDescription());
        result.setSuperAdmin(source.isSuperAdmin());
        result.setDisabled(source.isDisabled());
        if (source.getRoles() != null) {
            result.setRoles(source.getRoles().stream()
                    .map(Enum::name)
                    .sorted()
                    .collect(Collectors.toSet()));
        }
        if (source.getInstances() != null) {
            result.setInstances(source.getInstances().stream()
                    .map(BKVInstance::getCode)
                    .sorted()
                    .collect(Collectors.toSet()));
        }

        return result;
    }
}
