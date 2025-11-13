//package com.menzo.Product_Service.Service;
//
//import com.menzo.Product_Service.Dto.ProductDto.ProductListingDto;
//import com.menzo.Product_Service.Dto.ProductDto.ProductMinimalListingDto;
//import com.menzo.Product_Service.Entity.Product;
//import com.menzo.Product_Service.Entity.ProductItem;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.criteria.*;
//import org.hibernate.validator.internal.engine.messageinterpolation.el.RootResolver;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ProductSpecificationServiceV2 {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public void method() {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<ProductMinimalListingDto> cq = cb.createQuery(ProductMinimalListingDto.class);
//        Root<Product> product = cq.from(Product.class);
//
//        cq.multiselect(
//                product.get("id"),
//                product.get("productName"),
//                product.get("itemWeight")
//        );
//
////        Predicate idPredicate = product.get("id").in(80, 81);
////        cq.where(idPredicate);
//    }
//
//    public static Specification<Product> getByIds(List<Long> ids) {
//        return (root, query, cb) -> root.get("id").in(ids);
//    }
//}
