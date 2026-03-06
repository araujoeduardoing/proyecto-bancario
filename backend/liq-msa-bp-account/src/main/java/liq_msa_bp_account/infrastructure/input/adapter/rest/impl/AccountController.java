package liq_msa_bp_account.infrastructure.input.adapter.rest.impl;

import liq_msa_bp_account.application.input.port.AccountService;
import liq_msa_bp_account.domain.Account;
import liq_msa_bp_account.infrastructure.exception.AccountNotFoundException;
import liq_msa_bp_account.infrastructure.input.adapter.rest.bean.AccountRequest;
import liq_msa_bp_account.infrastructure.repository.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/business/retail/v1/accounts")
@Tag(name = "Account", description = "Account management API")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountMapper accountMapper;


    @PostMapping("/create")
    @Operation(summary = "Create a new account")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountRequest request) {
        Account account = accountMapper.accountRequestToAccountDomain(request);
        Account savedAccount = accountService.save(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    @GetMapping("/{id:[0-9]+}")
    @Operation(summary = "Get account by ID")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Optional<Account> account = accountService.findById(id);
        if (account.isEmpty()) {
            throw new AccountNotFoundException("Account not found with ID: " + id);
        }
        return ResponseEntity.ok(account.get());
    }
    
    @PutMapping("/{id:[0-9]+}")
    @Operation(summary = "Update account")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id,
                                                   @Valid @RequestBody AccountRequest request) {
        // Primero verificar que la cuenta existe
        Optional<Account> existingAccountOpt = accountService.findById(id);
        if (existingAccountOpt.isEmpty()) {
            throw new AccountNotFoundException("Account not found with ID: " + id);
        }
        
        Account existingAccount = existingAccountOpt.get();
        
        // Actualizar los campos de la cuenta existente con los datos del request
        existingAccount.setAccountNumber(request.getAccountNumber());
        existingAccount.setAccountType(request.getAccountType());
        existingAccount.setInitialBalance(request.getInitialBalance());
        existingAccount.setStatus(request.getStatus());
        existingAccount.setClientId(request.getClientId());
        
        Account updatedAccount = accountService.save(existingAccount);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id:[0-9]+}")
    @Operation(summary = "Delete account")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
   
    @GetMapping("/all")
    @Operation(summary = "Get all accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return accounts.isEmpty()
                ? ResponseEntity.ok(List.of())
                : ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/client/{clientId:[0-9]+}")
    @Operation(summary = "Get accounts by client ID")
    public ResponseEntity<List<Account>> getAccountByClientId(@PathVariable Long clientId) {
        List<Account> accounts = accountService.findByClientId(clientId);
        return accounts.isEmpty()
                ? ResponseEntity.ok(List.of()) 
                : ResponseEntity.ok(accounts);
    }

}
