package com.acme.vendingmachine.infrastructure;

import com.acme.vendingmachine.domain.VendingMachineAccount;
import com.acme.vendingmachine.domain.VendingMachineAccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class VendingMachineAccountRepositoryJpaImpl implements VendingMachineAccountRepository {
    private final Repository repository;

    public VendingMachineAccountRepositoryJpaImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<VendingMachineAccount> findByUserId(UUID userId) {
        return repository.findOneByUserId(userId).map(VendingMachineAccountEntityAdapter::adapt);
    }

    @Override
    public UUID save(VendingMachineAccount vendingMachineAccount) {
        return repository.save(VendingMachineAccountEntityAdapter.adapt(vendingMachineAccount)).getId();
    }

    interface Repository extends JpaRepository<VendingMachineAccountEntity, UUID> {
        Optional<VendingMachineAccountEntity> findOneByUserId(UUID userId);
    }
}
