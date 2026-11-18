package com.eazybytes.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() { // this class will give you who created and updated the data
        return Optional.of("ACCOUNTS_MS"); // hardcoded value added here later we can change
    }
}
