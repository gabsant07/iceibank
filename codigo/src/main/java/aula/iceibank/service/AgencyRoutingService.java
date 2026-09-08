package aula.iceibank.service;

import aula.iceibank.config.BankProperties;
import aula.iceibank.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AgencyRoutingService {

    private static final int AGENCY_COUNT = 3;
    private final BankProperties properties;

    public AgencyRoutingService(BankProperties properties) {
        this.properties = properties;
    }

    public int ownerOf(long accountNumber) {
        return Math.floorMod(accountNumber, AGENCY_COUNT);
    }

    public void requireLocal(long accountNumber) {
        int owner = ownerOf(accountNumber);
        if (owner != properties.getAgencyId()) {
            throw new BusinessException(HttpStatus.CONFLICT,
                    "Account " + accountNumber + " belongs to agency " + owner);
        }
    }
}
