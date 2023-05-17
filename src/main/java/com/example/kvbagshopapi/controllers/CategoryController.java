package com.example.kvbagshopapi.controllers;

import com.example.kvbagshopapi.dtos.CategoryDto;
import com.example.kvbagshopapi.services.ICategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Category")
public class CategoryController {
    @Autowired
    ICategoryService iCategoryService;


    @GetMapping("")
    @ApiOperation(value = "Get all category")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> listCategory = iCategoryService.getAllCategories();
        if (listCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listCategory, HttpStatus.OK);
    }
//    @GetMapping("/pagination")
//    public ResponseEntity<List<CategoryDto>> getAllCategoriesPagination() {
//        List<CategoryDto> listCategory = iCategoryService.getAllPagingCategories();
//        if (listCategory.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(listCategory, HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get category by id")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") long id) {
        CategoryDto category = iCategoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @ApiOperation(value = "Create new category")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = iCategoryService.createCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

//    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<CategoryDto> patchCategory(@PathVariable("id") Long categoryId,
//                                                     @RequestBody Map<Object, Object> categoryDto) {
//        CategoryDto updatedCategory = iCategoryService.patchCategory(categoryId , categoryDto);
//        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
//    }

    @ApiOperation(value = "Update category by id (Update only the fields you want to change)")
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") Long categoryId,
                                                     @RequestBody CategoryDto categoryDto) throws NoSuchFieldException, IllegalAccessException {
        CategoryDto updatedCategory = iCategoryService.updateCategory(categoryId , categoryDto);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete category by id")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId){
        iCategoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category successfully deleted !!", HttpStatus.OK);
    }

    @ApiOperation(value = "Search category by name")
    @GetMapping("/search/{name}")
    public ResponseEntity<List<CategoryDto>> searchByName(@RequestParam("name") String name){
        List<CategoryDto> categoryDtos = iCategoryService.searchByName(name);
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }
}













