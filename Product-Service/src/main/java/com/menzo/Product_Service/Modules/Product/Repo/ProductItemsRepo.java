package com.menzo.Product_Service.Modules.Product.Repo;

import com.menzo.Product_Service.Modules.Product.Entity.ProductItem;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductItemsRepo extends JpaRepository<ProductItem, Long>, JpaSpecificationExecutor<ProductItem> {

    public List<ProductItem> findAllByProductId(Long productId);

    public Page<ProductItem> findAllByProductId(Long productId, Pageable pageable);

    public boolean existsBySuperSku(String superSku);

    public List<ProductItem> findAllBySuperSku(String superSku);

    @Query(value = """
            SELECT 
                    o.optionValue 
                FROM ProductItem i 
                JOIN i.configurations pc 
                JOIN pc.variationOption o 
                JOIN o.variation v 
                WHERE v.variationName = :variationName 
                    AND i.id = :itemId 
            """)
    public String findSizeByItemId(String variationName, Long itemId);

    //  returns list of available entities with provided idList
    List<ProductItem> findByIdIn(List<Long> selectionList);


//    ********* Fetching sequence *********

//    @Query(
//            nativeQuery = true,
//            value = "SELECT NEXT VALUE FOR item_sequence"
//    )
//    public Long getNextItemId();

}
