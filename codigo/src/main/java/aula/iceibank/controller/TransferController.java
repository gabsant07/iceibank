package aula.iceibank.controller;

import aula.iceibank.dto.MessageResponse;
import aula.iceibank.dto.RemoteCreditRequest;
import aula.iceibank.dto.TransactionResponse;
import aula.iceibank.dto.TransferRequest;
import aula.iceibank.service.TransferService;
import aula.iceibank.security.InternalAgencySecurityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;
    private final InternalAgencySecurityService internalAgencySecurityService;

    public TransferController(TransferService transferService,
                              InternalAgencySecurityService internalAgencySecurityService) {
        this.transferService = transferService;
        this.internalAgencySecurityService = internalAgencySecurityService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transferService.transfer(request));
    }

    @PostMapping("/internal/credit")
    public ResponseEntity<MessageResponse> receiveCredit(
            @RequestHeader(value = "X-Agency-Key", required = false) String internalApiKey,
            @Valid @RequestBody RemoteCreditRequest request) {
        internalAgencySecurityService.validate(internalApiKey);
        return ResponseEntity.ok(transferService.receiveRemoteCredit(request));
    }
}
