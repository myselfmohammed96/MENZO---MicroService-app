package com.menzo.Product_Service.Product.Repository;

import com.menzo.Product_Service.Product.Entity.ProductItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductItemsRepository extends JpaRepository<ProductItem, UUID>, JpaSpecificationExecutor<ProductItem> {

    public List<ProductItem> findAllByProduct_ProductId(UUID productId);

    public Page<ProductItem> findAllByProduct_ProductId(UUID productId, Pageable pageable);

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
    public String findSizeByItemId(String variationName, UUID itemId);

    //  returns list of available entities with provided idList
    List<ProductItem> findByItemIdIn(List<UUID> selectionList);


//    ********* Fetching sequence *********

//    @Query(
//            nativeQuery = true,
//            value = "SELECT NEXT VALUE FOR item_sequence"
//    )
//    public UUID getNextItemId();

}
