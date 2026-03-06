package liq_msa_bp_account.infrastructure.output.adapter;

import liq_msa_bp_account.application.output.port.AccountOutputService;
import liq_msa_bp_account.domain.Account;
import liq_msa_bp_account.infrastructure.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountAdapter implements AccountOutputService {
    @Autowired
    private AccountRepository accountRepository;
    
    public Account save(Account account) {
        Account savedAccount = accountRepository.save(account);
        return savedAccount;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
    
    @Override
    public List<Account> findByClientId(Long clientId) {
        return accountRepository.findByClientId(clientId);
    }
}
