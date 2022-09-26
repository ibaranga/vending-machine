package com.acme.product.infrastructure;

import com.acme.product.domain.Product;
import com.acme.product.domain.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepositoryJpaImpl implements ProductRepository {
    private final Repository repository;

    public ProductRepositoryJpaImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return repository.findById(id).map(ProductEntityAdapter::adapt);
    }

    @Override
    public UUID save(Product product) {
        return repository.save(ProductEntityAdapter.adapt(product)).getId();
    }

    @Override
    public List<Product> findAll(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "lastModifiedDate");
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return repository.findAll(pageRequest).stream().map(ProductEntityAdapter::adapt).toList();
    }

    @Override
    public List<Product> findAllBySellerId(UUID sellerId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "lastModifiedDate");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repository.findAllBySellerId(sellerId, pageRequest).stream().map(ProductEntityAdapter::adapt).toList();
    }

    @Override
    public void delete(Product product) {
        repository.delete(ProductEntityAdapter.adapt(product));
    }


    interface Repository extends JpaRepository<ProductEntity, UUID> {
        Page<ProductEntity> findAllBySellerId(UUID sellerId, Pageable pageable);
    }
}
