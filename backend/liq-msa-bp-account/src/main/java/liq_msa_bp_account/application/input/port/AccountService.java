package liq_msa_bp_account.application.input.port;


import liq_msa_bp_account.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account save(Account account);
    Optional<Account> findById(Long id);
    void deleteById(Long id);
    List<Account> findAll();

}
