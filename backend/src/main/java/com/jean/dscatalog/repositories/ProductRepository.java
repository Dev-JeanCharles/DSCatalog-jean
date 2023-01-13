package com.jean.dscatalog.repositories;

import com.jean.dscatalog.entities.Category;
import com.jean.dscatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
}
