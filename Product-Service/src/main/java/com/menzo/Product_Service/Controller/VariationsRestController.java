package com.menzo.Product_Service.Controller;

import com.menzo.Product_Service.Entity.Variations;
import com.menzo.Product_Service.Service.VariationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("variations")
public class VariationsRestController {

    @Autowired
    private VariationsService variationsService;

//    Variations

    @GetMapping("get-all")
    public void getAllVariations(){}

    @GetMapping("get")
    public void getVariationById(@RequestParam Long variationId){}

    @PostMapping("add")
    public void addVariation(@RequestBody Variations newVariation){
        variationsService.addNewVariation(newVariation);
    }

    @PutMapping("update")
    public void updateVariation(){}

    @DeleteMapping("delete")
    public void deleteVariation(){}

//    Variation options

    public void getAllOptionsByVariationId(){}

    public void getOptionById(){}

    public void addOption(){}

    public void updateOption(){}

    public void deleteOption(){}
}
