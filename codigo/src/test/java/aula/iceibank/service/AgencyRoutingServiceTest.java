package aula.iceibank.service;

import aula.iceibank.config.BankProperties;
import aula.iceibank.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AgencyRoutingServiceTest {

    @Test
    void shouldPartitionAccountsBetweenThreeAgencies() {
        BankProperties properties = new BankProperties();
        properties.setAgencyId(0);
        AgencyRoutingService service = new AgencyRoutingService(properties);

        assertThat(service.ownerOf(0)).isZero();
        assertThat(service.ownerOf(4)).isEqualTo(1);
        assertThat(service.ownerOf(8)).isEqualTo(2);
        assertThat(service.ownerOf(-1)).isEqualTo(2);
        assertThatThrownBy(() -> service.requireLocal(1)).isInstanceOf(BusinessException.class);
    }
}
