package com.menzo.Product_Service.Modules.Category.Repo;

import com.menzo.Product_Service.Category.Dto.ParentCategoryView;
import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Category.Repository.CategoriesRepository;
import com.menzo.Product_Service.GlobalComponents.Service.UtilityService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class CategoriesRepoTest {

    @Autowired
    private CategoriesRepository categoriesRepo;

    @Autowired
    private UtilityService utilityService;

    @PersistenceContext
    private EntityManager entityManager;


//    ********* existence check *********

    @Test
    public void testExistsById() {
        boolean exists = categoriesRepo.existsById(5L);
        System.out.println("ID exists: " + exists);
    }

    @Test
    public void testExistsByCategoryName() {
        boolean exists = categoriesRepo.existsByCategoryName("Pants");
        System.out.println("CategoryName exists: " + exists);
    }

    @Test
    public void testExistsByAbbreviation() {
        boolean exists = categoriesRepo.existsByAbbreviation("J1");
        System.out.println("Abbreviation exists: " + exists);
    }

    @Test
    public void testExistsByCategoryNameAndParentCategoryId() {
        boolean exists = categoriesRepo.existsByCategoryNameAndParentCategoryId(
                "Formal Shirts",
                1l
        );
        System.out.println("CategoryName & Parent ID exists: " + exists);
    }



//    ********* Save methods *********

    @Test
    public void testSaveParent() {
        ProductCategory parent = ProductCategory.builder()
                .parentCategoryId(null)
                .categoryName("Sample parent1")
                .abbreviation(null)
                .isActive(true)
                .isDeleted(false)
                .build();
        ProductCategory savedParent = categoriesRepo.save(parent);
        System.out.println(savedParent);
    }

    @Test
    public void testSaveSub() {
        ProductCategory sub = ProductCategory.builder()
                .parentCategoryId(156L)
                .categoryName("Sample sub1")
                .abbreviation(utilityService.generateAbbreviation(
                        "sub-category",
                        "Sample sub1"
                ))
                .isDeleted(false)
                .isActive(true)
                .build();
        ProductCategory savedSub = categoriesRepo.save(sub);
        System.out.println(savedSub);
    }



    //  ********* find methods *********

    @Test
    @Transactional
    public void testFindAll() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("activeFilter").setParameter("isDeleted", false);
        List<ProductCategory> allCategories = categoriesRepo.findAll();
        System.out.println(allCategories);
        System.out.println(allCategories.size());
    }

    @Test
    public void testFindByParentCategoryIdIsNull() {
        List<ProductCategory> list = categoriesRepo.findByParentCategoryIdIsNull();
        System.out.println(list);
    }

    @Test
    public void testFindByIdAndParentCategoryIdIsNull() {
        Optional<ProductCategory> parent = categoriesRepo.findByIdAndParentCategoryIdIsNull(1L);
        System.out.println("Parent: " + parent.get());
    }

    @Test
    public void testFindAllParentWithSub() {
        List<Object[]> allParentWithSub = categoriesRepo.findAllParentWithSub();
        for (Object[] obj : allParentWithSub) {
            System.out.println(Arrays.toString(obj));
        }
    }

    @Test
    public void testFindParentByIdWithSub() {
        List<Object[]> parentByIdWithSub = categoriesRepo.findParentByIdWithSub(1L);
        for (Object[] obj : parentByIdWithSub) {
            System.out.println(Arrays.toString(obj));
        }
    }

    @Test
    public void testFindParentCategoryBySubId() {
        ParentCategoryView parent = categoriesRepo.findParentCategoryBySubId(121L);
        System.out.println(
                "id: " + parent.getId() +
                        " categoryName: " + parent.getCategoryName()
        );
    }

    @Test
    public void testFindParentByProductId() {
        ParentCategoryView category = categoriesRepo.findParentByProductId(83L);
        System.out.println(category.getId() + " - " + category.getCategoryName());
    }



//    ********* Sub-categories *********

    @Test
    public void testFindByIdAndParentCategoryIdIsNotNull() {
        Optional<ProductCategory> sub = categoriesRepo.findByIdAndParentCategoryIdIsNotNull(121L);
        System.out.println(sub);
    }

    @Test
    public void testFindAllByParentCategoryId() {
        List<ProductCategory> subList = categoriesRepo.findAllByParentCategoryId(1L);
        System.out.println(subList);
    }






//    @Test
//    public void testFindIt() {
//        List<ProductCategory> it = categoriesRepo.findIt(true);
//        for (ProductCategory c : it) {
//            System.out.println(c.getId() + " - " + c.getParentCategoryId() + " - " + c.getCategoryName());
//        }
//        System.out.println(it.size());
//    }

//    @Test
//    public void testFindByIdAndParentCategoryIdIsNotNullWithoutVariation() {
//        Optional<ProductCategory> sub = categoriesRepo.findByIdAndParentCategoryIdIsNotNull(121L);
//        System.out.println(sub);
//        System.out.println(sub.get().getVariations());
//    }
}





//    ********* Delete methods *********
//
//    @Test
//    public void testDeleteParentById() {
//        categoriesRepo.deleteParentById(3L);
//    }
//
//    @Test
//    public void testDeleteSubById() {
//        categoriesRepo.deleteSubById(2L);
//    }