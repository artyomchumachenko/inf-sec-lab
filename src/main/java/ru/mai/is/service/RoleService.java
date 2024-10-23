package ru.mai.is.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.mai.is.model.Role;
import ru.mai.is.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Transactional
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
