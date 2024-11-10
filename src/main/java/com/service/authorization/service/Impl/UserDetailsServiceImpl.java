package com.service.authorization.service.Impl;

import com.service.authorization.entity.EntityCredential;
import com.service.authorization.repository.EntityCredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EntityCredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialRepository.findByStudentId(username)
                .orElseThrow(() -> new UsernameNotFoundException("NIM Not Found"));
    }

    public EntityCredential create(EntityCredential entityCredential) {
        log.info("[CREDENTIAL] Try to create credential with NIM: {}", entityCredential.getStudentId());

        return credentialRepository.save(entityCredential);
    }
}
