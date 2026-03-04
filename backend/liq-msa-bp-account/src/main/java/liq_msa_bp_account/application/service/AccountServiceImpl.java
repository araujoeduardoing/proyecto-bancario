package liq_msa_bp_account.application.service;

import liq_msa_bp_account.application.input.port.AccountService;
import liq_msa_bp_account.application.output.port.AccountOutputService;
import liq_msa_bp_account.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountOutputService accountOutputService;
    
    @Override
    public Account save(Account account) {
        return accountOutputService.save(account);
    }
    
    @Override
    public Optional<Account> findById(Long id) {
        return accountOutputService.findById(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        if (!accountOutputService.findById(id).isPresent()) {
            throw new IllegalArgumentException("Account not found with ID: " + id);
        }
        accountOutputService.deleteById(id);
    }

    @Override
    public List<Account> findAll() {
        return accountOutputService.findAll();
    }
}
