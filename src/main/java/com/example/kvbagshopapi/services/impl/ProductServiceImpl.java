package com.example.kvbagshopapi.services.impl;

import com.example.kvbagshopapi.dtos.CategoryDto;
import com.example.kvbagshopapi.dtos.ProductDto;
import com.example.kvbagshopapi.dtos.ProductPagination;
import com.example.kvbagshopapi.entities.Category;
import com.example.kvbagshopapi.entities.Product;
import com.example.kvbagshopapi.exceptions.NotFoundException;
import com.example.kvbagshopapi.repositories.ProductRepository;
import com.example.kvbagshopapi.services.ICategoryService;
import com.example.kvbagshopapi.services.IProductService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;

    ICategoryService iCategoryService;
    private ModelMapper modelMapper;

//    public ProductServiceImpl(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> Products = productRepository.findAll();
        return Products.stream()
                .map((Product) -> modelMapper.map(Product, ProductDto.class))
                .sorted(Comparator.comparing((ProductDto::getProductName)))
                .collect(Collectors.toList());
    }

    @Override
    public ProductPagination getAllPagingProducts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Create Pagenable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> Products = productRepository.findAll(pageable);

        // get content for page object
        List<Product> listOfProducts = Products.getContent();

        List<ProductDto> contents = listOfProducts.stream()
                .map((Product) -> modelMapper.map(Product, ProductDto.class))
                .collect(Collectors.toList());

        ProductPagination ProductPagination = new ProductPagination();
        ProductPagination.setContent(contents);
        ProductPagination.setPageNo(Products.getNumber());
        ProductPagination.setPageSize(Products.getSize());
        ProductPagination.setTotalElements(Products.getTotalElements());
        ProductPagination.setTotalPages(Products.getTotalPages());
        ProductPagination.setLast(Products.isLast());
        return ProductPagination;
    }

    @Override
    public ProductDto getProductById(Long ProductId) {
        Optional<Product> ProductOp = productRepository.findById(ProductId);
        if (!ProductOp.isPresent())
            throw new NotFoundException("Cant find Product!");
        return modelMapper.map(ProductOp.get(), ProductDto.class);
    }

    @Override
    public ProductDto createProduct(ProductDto ProductDto) {
        Product product = modelMapper.map(ProductDto, Product.class);
        Product savedProduct = productRepository.save(product);
        ProductDto saveProductDto = modelMapper.map(savedProduct, ProductDto.class);
        return saveProductDto;
    }
    


    // Cập nhật lại Product (chỉ cập nhật những thuộc tính muốn thay đổi)
    @Override
    public ProductDto patchProduct(Long id, Map<Object, Object> ProductDto) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (!existingProduct.isPresent()) throw new NotFoundException("Can not found product !Unable to update Product!");

        ProductDto.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Product.class, (String) key);
            field.setAccessible(true);

            if(key.equals("category")){
                // Instantiate a new Gson instance.
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                String json = gson.toJson(value, LinkedHashMap.class); // json is a json String
                JsonObject categoryDtoJsonObject = (JsonObject) parser.parse(json);
                CategoryDto categorydto = iCategoryService.getCategoryById(gson.fromJson(categoryDtoJsonObject, CategoryDto.class).getId());
                existingProduct.get().setCategory(modelMapper.map(categorydto, Category.class));
                return;
            }

            ReflectionUtils.setField(field, existingProduct.get(), (Object) value);
        });
        existingProduct.get().setUpdateAt(new Date(new Date().getTime()));
        Product updatedProduct = productRepository.save(existingProduct.get());
        ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);

        return updatedProductDto;

    }

    // Cập nhật lại Product (Cập nhật lại toàn bộ các thuộc tính)
    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct == null) throw new NotFoundException("Can not found product !Unable to update Product!");

        BeanUtils.copyProperties(productDto, existingProduct);

        CategoryDto newCategoryDto = iCategoryService.getCategoryById(productDto.getCategory().getId());
        Category newCategory = modelMapper.map(newCategoryDto, Category.class);
        existingProduct.setCategory(newCategory);

        existingProduct.setUpdateAt(new Date(new Date().getTime()));
        Product updatedProduct = productRepository.save(existingProduct);
        ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);
        return updatedProductDto;

    }
    @Override
    public void deleteProduct(Long ProductId) {
        Optional<Product> existingProduct = productRepository.findById(ProductId);
        if (!existingProduct.isPresent()) throw new NotFoundException("Unable to dalete Product!");

        existingProduct.get().setIsActive(false);
        existingProduct.get().setUpdateAt(new Date(new Date().getTime()));
        productRepository.save(existingProduct.get());
    }

    @Override
    public List<ProductDto> searchByProductName(String name) {
        List<Product> Products = productRepository.findByProductNameContainingIgnoreCase(name);
        List<ProductDto> ProductDtos = new ArrayList<>();
        for (Product Product : Products
        ) {
            ProductDto ProductDto = modelMapper.map(Product, ProductDto.class);
            ProductDtos.add(ProductDto);
        }
        if (ProductDtos.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find any Products with name: " + name);
        }
        return ProductDtos;
    }

    @Override
    public List<ProductDto> searchByProductNameAndCategory(String name, Category category) {
        List<Product> Products = productRepository.findByProductNameContainingAndCategoryAllIgnoreCase(name, category);
        List<ProductDto> ProductDtos = new ArrayList<>();
        for (Product Product : Products
        ) {
            ProductDto ProductDto = modelMapper.map(Product, ProductDto.class);
            ProductDtos.add(ProductDto);
        }
        if (ProductDtos.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find any Products with name: " + name);
        }
        return ProductDtos;
    }
    @Override
    public List<ProductDto> getTop10ProductBySold() {
        List<Product> Products = productRepository.findTop10ByOrderBySoldDesc();
        List<ProductDto> ProductDtos = new ArrayList<>();
        for (Product Product : Products
        ) {
            ProductDto ProductDto = modelMapper.map(Product, ProductDto.class);
            ProductDtos.add(ProductDto);
        }
        if (ProductDtos.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Do not find any product");
        }
        return ProductDtos;
    }


    @Override
    public List<ProductDto> getTop10NewProducts() {
        List<Product> Products = productRepository.findTop10ByOrderByCreateAtDesc();
        List<ProductDto> ProductDtos = new ArrayList<>();
        for (Product Product : Products
        ) {
            ProductDto ProductDto = modelMapper.map(Product, ProductDto.class);
            ProductDtos.add(ProductDto);
        }
        if (ProductDtos.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Do not find any product");
        }
        return ProductDtos;
    }

    @Override
    public List<ProductDto> getProductByCategoryId(long category) {
        List<Product> products = productRepository.findByCategoryId(category);
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product : products){
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            productDtos.add(productDto);
        }
        if (productDtos.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Do not find any product");
        }
        return productDtos;
    }

}
