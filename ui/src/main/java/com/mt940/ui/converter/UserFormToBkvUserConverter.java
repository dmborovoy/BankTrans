package com.mt940.ui.converter;

import com.mt940.dao.repository.BkvInstanceRepository;
import com.mt940.domain.enums.BKVRoles;
import com.mt940.domain.permission.BKVUser;
import com.mt940.ui.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;

/**
 * Created by vmayorov on 22/05/2015.
 */
public class UserFormToBkvUserConverter implements Converter<UserForm, BKVUser> {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BkvInstanceRepository instanceRepository;

    @Override
    public BKVUser convert(UserForm source) {
        BKVUser result = new BKVUser();

        result.setLogin(source.getUsername());
        if (source.getPassword() != null) {
            result.setPassword(passwordEncoder.encode(source.getPassword()));
        }
        result.setDescription(source.getDescription());
        result.setDisabled(false);
        if (source.getRoles() != null) {
            result.setRoles(source.getRoles()
                    .stream()
                    .map(BKVRoles::valueOf)
                    .collect(Collectors.toSet()));
        }
        if (source.getInstances() != null) {
            result.setInstances(source.getInstances()
                    .stream()
                    .map(instanceRepository::findByCode)
                    .collect(Collectors.toSet()));
        }
        result.setId(source.getUserId());

        return result;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setInstanceRepository(BkvInstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }
}
