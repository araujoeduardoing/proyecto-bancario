package liq_msa_bp_account.infrastructure.persistence;

import liq_msa_bp_account.domain.Account;
import liq_msa_bp_account.infrastructure.persistence.jpa.AccountJpaRepository;
import liq_msa_bp_account.infrastructure.repository.AccountRepository;
import liq_msa_bp_account.infrastructure.repository.entity.AccountEntity;
import liq_msa_bp_account.infrastructure.repository.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountPersistence implements AccountRepository {
    private final AccountJpaRepository jpaRepository;
    private final AccountMapper accountMapper;
    
    @Override
    public Account save(Account account) {
        AccountEntity entity = accountMapper.accountToAccountEntity(account);
        AccountEntity savedEntity = jpaRepository.save(entity);
        return accountMapper.accountEntityToAccountDomain(savedEntity);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return jpaRepository.findById(id)
                .map(accountMapper::accountEntityToAccountDomain);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Account> findAll() {
        return jpaRepository.findAllWithPersonName()
                .stream()
                .map(this::mapToAccountWithName)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Account> findByClientId(Long clientId) {
        return jpaRepository.findByClientId(clientId)
                .stream()
                .map(accountMapper::accountEntityToAccountDomain)
                .collect(Collectors.toList());
    }
    
    private Account mapToAccountWithName(Object[] result) {
        Account account = new Account();
        account.setId((Long) result[0]);
        account.setAccountNumber((String) result[1]);
        account.setAccountType((String) result[2]);
        account.setInitialBalance((java.math.BigDecimal) result[3]);
        account.setStatus((Boolean) result[4]);
        account.setClientId((Long) result[5]);
        account.setName((String) result[6]);
        account.setCreatedAt((java.time.LocalDateTime) result[7]);
        account.setUpdatedAt((java.time.LocalDateTime) result[8]);
        return account;
    }
}
