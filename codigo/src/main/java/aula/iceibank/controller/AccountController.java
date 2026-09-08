package aula.iceibank.controller;

import aula.iceibank.dto.AccountResponse;
import aula.iceibank.dto.AmountRequest;
import aula.iceibank.dto.CreateAccountRequest;
import aula.iceibank.dto.TransactionResponse;
import aula.iceibank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> list() {
        return ResponseEntity.ok(accountService.listLocal());
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> find(@PathVariable long accountNumber) {
        return ResponseEntity.ok(accountService.find(accountNumber));
    }

    @PostMapping("/{accountNumber}/deposits")
    public ResponseEntity<AccountResponse> deposit(@PathVariable long accountNumber,
                                                   @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(accountService.deposit(accountNumber, request.amount()));
    }

    @PostMapping("/{accountNumber}/withdrawals")
    public ResponseEntity<AccountResponse> withdraw(@PathVariable long accountNumber,
                                                    @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(accountService.withdraw(accountNumber, request.amount()));
    }

    @GetMapping("/{accountNumber}/history")
    public ResponseEntity<List<TransactionResponse>> history(@PathVariable long accountNumber) {
        return ResponseEntity.ok(accountService.history(accountNumber).stream()
                .map(TransactionResponse::from)
                .toList());
    }
}
