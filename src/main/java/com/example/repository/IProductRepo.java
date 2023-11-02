package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepo extends JpaRepository<Product, Integer> {

    public List<Product> findAllByOrderByProductNameAsc();

    //public List<Product> findAllByOrderByProductNameDesc();

    public List<Product> findByProductNameContainsAllIgnoreCase(String name);

    /* @Query("SELECT p FROM Product p WHERE p.productCode IN :productCodes")
     List<Product> findByProductCodes(@Param("productCodes") List<Integer> productCodes);*/

    public List<Product> findByProductCodeIn(List<Integer> collect);

    public List<Product> findByProductCodeInAndQuantityInStockGreaterThan(List<Integer> productCodes, int quantityInStock);
}
