package com.example.kvbagshopapi.services.impl;

import com.example.kvbagshopapi.dtos.CategoryDto;
import com.example.kvbagshopapi.dtos.CategoryPagination;
import com.example.kvbagshopapi.entities.Category;
import com.example.kvbagshopapi.exceptions.NotFoundException;
import com.example.kvbagshopapi.repositories.CategoryRepository;
import com.example.kvbagshopapi.services.ICategoryService;
import com.example.kvbagshopapi.utils.MapperUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    //    public CategoryServiceImpl(CategoryRepository categoryRepository){
//        this.categoryRepository = categoryRepository;
//    }
    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
//                .filter(Category -> !Category.getIsDeleted())
                .map((Category) -> modelMapper.map(Category, CategoryDto.class))
                .sorted(Comparator.comparing(CategoryDto::getName))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryPagination getAllPagingCategories(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Create Pagenable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Category> categories = categoryRepository.findAll(pageable);

        // get content for page object
        List<Category> listOfCategories = categories.getContent();

        List<CategoryDto> contents = listOfCategories.stream()
//                .filter(Category -> !Category.getIsDeleted())
                .map((category) -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());

        CategoryPagination categoryPagination = new CategoryPagination();
        categoryPagination.setContent(contents);
        categoryPagination.setPageNo(categories.getNumber());
        categoryPagination.setPageSize(categories.getSize());
        categoryPagination.setTotalElements(categories.getTotalElements());
        categoryPagination.setTotalPages(categories.getTotalPages());
        categoryPagination.setLast(categories.isLast());
        return categoryPagination;
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Optional<Category> categoryOp = categoryRepository.findById(categoryId);
        if (!categoryOp.isPresent())
            throw new NotFoundException("Cant find category!");
        return modelMapper.map(categoryOp.get(), CategoryDto.class);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        CategoryDto saveCategoryDto = modelMapper.map(savedCategory, CategoryDto.class);
        return saveCategoryDto;
    }



    // Cập nhật lại category (chỉ cập nhật những thuộc tính muốn thay đổi)
    @Override
    public CategoryDto patchCategory(Long id, Map<Object, Object> categoryDto) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (!existingCategory.isPresent()) throw new NotFoundException("Unable to update category!");

        categoryDto.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Category.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, existingCategory.get(), (Object) value);
        });
        existingCategory.get().setUpdateAt(new Date(new Date().getTime()));
        Category updatedCategory = categoryRepository.save(existingCategory.get());
        CategoryDto updatedCategoryDto = modelMapper.map(updatedCategory, CategoryDto.class);

        return updatedCategoryDto;

    }

    // Cập nhật lại category (cập nhật lại toàn bộ các thuộc tính)
    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws NoSuchFieldException, IllegalAccessException {
        Category existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory == null) throw new NotFoundException("Unable to update category!");

//        BeanUtils.copyProperties(categoryDto, existingCategory);

        MapperUtils.toDto(categoryDto,existingCategory);

        existingCategory.setUpdateAt(new Date(new Date().getTime()));
        Category updatedCategory = categoryRepository.save(existingCategory);
        CategoryDto updatedCategoryDto = modelMapper.map(updatedCategory, CategoryDto.class);
        return updatedCategoryDto;

    }

    // Hàm deleteCategory chỉ delete bằng cách set thuộc tính IsDeleted = true chứ không xoá hẳn trong database
    @Override
    public void deleteCategory(Long categoryId) {
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        if (!existingCategory.isPresent()) throw new NotFoundException("Unable to dalete category!");

        existingCategory.get().setIsDeleted(true);
        existingCategory.get().setUpdateAt(new Date(new Date().getTime()));
        categoryRepository.save(existingCategory.get());
    }

    @Override
    public List<CategoryDto> searchByName(String name) {
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name);
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
            categoryDtos.add(categoryDto);
        }
        if (categoryDtos.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find any categorys with name: " + name);
        }
        return categoryDtos;
    }
}
