package com.example.serviceimpl;

import com.example.entity.Product;
import com.example.exception.ProductNotFoundException;
import com.example.repository.IProductRepo;
import com.example.service.IProductService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepo iProductRepo;

    @Override
    public Product createProduct(Product product) {
        return iProductRepo.save(product);
    }


    @Override
    public List<Product> createProductList(List<Product> product) {
        return iProductRepo.saveAll(product);
    }


    @Override
    public Product updateProduct(Integer productCode, Product product) {
        Product existingProduct = getProductById(productCode);
        if (existingProduct != null) {
            existingProduct.setProductName(product.getProductName());
            existingProduct.setProductDescription(product.getProductDescription());
            existingProduct.setQuantityInStock(product.getQuantityInStock());
            existingProduct.setPrice(product.getPrice());
            return iProductRepo.save(existingProduct);
        } else {
            throw new ProductNotFoundException("product cannot be created ");
        }
    }


    @Override
    public Product partialUpdate(Integer productCode, Map<String, Object> updates) {
        Product product = iProductRepo.findById(productCode).orElseThrow(() -> new ProductNotFoundException("Product cannot be created: " + productCode));
        ReflectionUtils.doWithFields(Product.class, field -> {
            String fieldName = field.getName();
            if (updates.containsKey(fieldName)) {
                field.setAccessible(true);
                Object newValue = updates.get(fieldName);
                field.set(product, newValue);
            }
        });
        return iProductRepo.save(product);
    }


    @Override
    public String deleteProductById(Integer productCode) {
        try {
            if (productCode != null) {
                iProductRepo.deleteById(productCode);
            }
            return "One product deleted " + productCode;
        } catch (RuntimeException e) {
            throw new ProductNotFoundException(" Product not found " + productCode+e.getMessage());
        }
    }


    @Override
    public void createProductsList() {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i < 10000; i++) {
            Faker faker = new Faker(new Locale("en-IND"));
            Product product = new Product();
            product.setProductName(faker.commerce().productName());
            product.setProductDescription(faker.lorem().sentence());
            product.setQuantityInStock(faker.number().numberBetween(1, 100));
            product.setPrice(faker.number().randomDouble(2, 102, 10000));
            products.add(product);
        }
        iProductRepo.saveAll(products);
    }


    @Override
    public String deleteAllProducts() {
        if (iProductRepo.count() != 0) {
            iProductRepo.deleteAll();
            return "All products are deleted ";
        } else {
            throw new ProductNotFoundException(" Products not found ");
        }
    }

    @Override
    public Product getProductById(Integer productCode) {
        return iProductRepo.findById(productCode).orElseThrow(() -> new ProductNotFoundException("product not exist with : " + productCode));
    }


    @Override
    public List<Product> getAllProducts() {
        if (iProductRepo.count() != 0) {
            return iProductRepo.findAll();
        } else {
            throw new ProductNotFoundException(" Products not found ");
        }
    }

    @Override
    public List<Product> getAllSortedByProduct() {
        if (iProductRepo.count() != 0) {
            return iProductRepo.findAllByOrderByProductNameAsc();
        } else {
            throw new ProductNotFoundException(" Products not found ");
        }
    }

    @Override
    public List<Product> searchByProductName(String name) {
        List<Product> products = null;
        if (name != null && (name.trim().length() > 0)) {
            return iProductRepo.findByProductNameContainsAllIgnoreCase(name);
        } else {
            return iProductRepo.findAllByOrderByProductNameAsc();       //return getAllSortedByProduct();
        }
    }

    //update quantity in stock for order creation
    @Transactional
    @Override
    public Product updateProductQuantityInStock(Integer productCode, Integer quantity) {
        Product existingProduct = getProductById(productCode);
        Integer originalQuantityInStock = existingProduct.getQuantityInStock();
        try {
            Integer updateProductQuantity = originalQuantityInStock - quantity;
            existingProduct.setQuantityInStock(updateProductQuantity);
            return iProductRepo.save(existingProduct);
        } catch (Exception e) {
            throw new ProductNotFoundException("product not available");
        }
    }

    @Transactional
    @Override
    public Product cancelOrderedProductUpdateQuantityInStock(Integer productCode, Integer quantity) {
        Product existingProduct = getProductById(productCode);
        Integer originalQuantityInStock = existingProduct.getQuantityInStock();
        try {
            Integer updateProductQuantity = originalQuantityInStock + quantity;
            existingProduct.setQuantityInStock(updateProductQuantity);
            return iProductRepo.save(existingProduct);
        } catch (Exception e) {
            throw new ProductNotFoundException("product not available");
        }
    }

    //fetch list of product based on list of productCode
    @Override
    public List<Product> getlistbyproductCode(List<Integer> productCode) {
        return iProductRepo.findByProductCodeIn(productCode);
    }

    @Override
    public Product incrementDecrementProductQuantityInStock(Integer productCode, String value, Integer quantity) {
        Product existingProduct = getProductById(productCode);
        Integer originalQuantityInStock = existingProduct.getQuantityInStock();
        if ((existingProduct != null) && (value.equalsIgnoreCase("increment")) && (1 <= quantity)) {
            Integer increaseProductPriceQuantity = existingProduct.getQuantityInStock() + quantity;
            //Integer newQuantity =  Quantity;    //existingProduct.setQuantityInStock(newQuantity);    //to set new Quantity
            existingProduct.setQuantityInStock(increaseProductPriceQuantity);
            return iProductRepo.save(existingProduct);
        }
        if ((existingProduct != null) && (value.equalsIgnoreCase("decrement")) && (originalQuantityInStock >= quantity)) {
            Integer decreaseProductPriceQuantity = existingProduct.getQuantityInStock() - quantity;
            //Integer newQuantity =  Quantity;    //existingProduct.setQuantityInStock(newQuantity);    //to set new Quantity
            existingProduct.setQuantityInStock(decreaseProductPriceQuantity);
            return iProductRepo.save(existingProduct);
        } else {
            throw new ProductNotFoundException("for increment quantity cannot be less than zero & for decrement quantity " + "cannot be greater than original quantity");
        }
    }

    @Override
    public Product incrementDecrementProductPrice(Integer productCode, String value, Double amount) {
        Product existingProduct = getProductById(productCode);
        Double originalPrice = existingProduct.getPrice();
        if ((existingProduct != null) && (value.equalsIgnoreCase("increment")) && (1 <= amount)) {
            Double increaseProductPriceAmount = existingProduct.getPrice() + amount;
            //Double newPrice =  amount;    //existingProduct.setPrice(newPrice);    //to set new price
            existingProduct.setPrice(increaseProductPriceAmount);
            return iProductRepo.save(existingProduct);
        }
        if ((existingProduct != null) && (value.equalsIgnoreCase("decrement")) && (originalPrice >= amount)) {
            Double decreaseProductPriceAmount = existingProduct.getPrice() - amount;
            //Double newPrice =  amount;    //existingProduct.setPrice(newPrice);    //to set new price
            existingProduct.setPrice(decreaseProductPriceAmount);
            return iProductRepo.save(existingProduct);
        } else {
            throw new ProductNotFoundException("for increment amount cannot be less than zero & for decrement amount cannot be " + "greater than original price");
        }
    }
}
