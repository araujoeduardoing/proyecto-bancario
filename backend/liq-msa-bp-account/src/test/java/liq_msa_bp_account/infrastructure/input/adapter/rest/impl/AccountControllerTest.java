package liq_msa_bp_account.infrastructure.input.adapter.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import liq_msa_bp_account.application.input.port.AccountService;
import liq_msa_bp_account.domain.Account;
import liq_msa_bp_account.infrastructure.exception.AccountNotFoundException;
import liq_msa_bp_account.infrastructure.input.adapter.rest.bean.AccountRequest;
import liq_msa_bp_account.infrastructure.repository.mapper.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountMapper accountMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Account testAccount;
    private AccountRequest testRequest;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("1000000001");
        testAccount.setAccountType("SAVINGS");
        testAccount.setInitialBalance(new BigDecimal("1000.00"));
        testAccount.setStatus(true);
        testAccount.setClientId(1L);
        testAccount.setCreatedAt(LocalDateTime.now());
        testAccount.setUpdatedAt(LocalDateTime.now());

        testRequest = new AccountRequest();
        testRequest.setAccountNumber("1000000001");
        testRequest.setAccountType("SAVINGS");
        testRequest.setInitialBalance(new BigDecimal("1000.00"));
        testRequest.setStatus(true);
        testRequest.setClientId(1L);
    }

    @Test
    void createAccount_ShouldReturnCreated_WhenValidRequestProvided() throws Exception {
        when(accountMapper.accountRequestToAccountDomain(any(AccountRequest.class))).thenReturn(testAccount);
        when(accountService.save(any(Account.class))).thenReturn(testAccount);

        mockMvc.perform(post("/business/retail/v1/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("1000000001"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.status").value(true));

        verify(accountMapper, times(1)).accountRequestToAccountDomain(any(AccountRequest.class));
        verify(accountService, times(1)).save(any(Account.class));
    }

    @Test
    void getAccountById_ShouldReturnAccount_WhenAccountExists() throws Exception {
        Long accountId = 1L;
        when(accountService.findById(accountId)).thenReturn(Optional.of(testAccount));

        mockMvc.perform(get("/business/retail/v1/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("1000000001"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));

        verify(accountService, times(1)).findById(accountId);
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount_WhenAccountExists() throws Exception {
        Long accountId = 1L;
        when(accountService.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(accountService.save(any(Account.class))).thenReturn(testAccount);

        mockMvc.perform(put("/business/retail/v1/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("1000000001"));

        verify(accountService, times(1)).findById(accountId);
        verify(accountService, times(1)).save(any(Account.class));
    }

    @Test
    void deleteAccount_ShouldReturnNoContent_WhenAccountDeleted() throws Exception {
        Long accountId = 1L;
        doNothing().when(accountService).deleteById(accountId);

        mockMvc.perform(delete("/business/retail/v1/accounts/{id}", accountId))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteById(accountId);
    }

    @Test
    void getAllAccounts_ShouldReturnListOfAccounts_WhenAccountsExist() throws Exception {
        Account account2 = new Account();
        account2.setId(2L);
        account2.setAccountNumber("2000000001");
        account2.setAccountType("CHECKING");
        account2.setInitialBalance(new BigDecimal("2000.00"));
        account2.setStatus(true);
        account2.setClientId(2L);

        List<Account> accounts = Arrays.asList(testAccount, account2);
        when(accountService.findAll()).thenReturn(accounts);

        mockMvc.perform(get("/business/retail/v1/accounts/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(accountService, times(1)).findAll();
    }
}