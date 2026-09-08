package aula.iceibank.security;

import aula.iceibank.config.BankProperties;
import aula.iceibank.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class InternalAgencySecurityService {

    private final BankProperties properties;

    public InternalAgencySecurityService(BankProperties properties) {
        this.properties = properties;
    }

    public void validate(String providedKey) {
        byte[] expected = properties.getInternalApiKey().getBytes(StandardCharsets.UTF_8);
        byte[] provided = providedKey == null ? new byte[0] : providedKey.getBytes(StandardCharsets.UTF_8);
        if (!MessageDigest.isEqual(expected, provided)) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid inter-agency credential");
        }
    }
}
