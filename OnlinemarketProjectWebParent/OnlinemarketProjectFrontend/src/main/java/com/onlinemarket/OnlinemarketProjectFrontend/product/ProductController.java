package com.onlinemarket.OnlinemarketProjectFrontend.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Category;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Product;
import com.onlinemarket.OnlinemarketProjectFrontend.category.CategoryService;


@Controller
public class ProductController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model){
        return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, @PathVariable("pageNum") int pageNum,Model model){
        Category category = categoryService.getCategory(alias);
        if(category == null){
            return "error/404";
        }

        List<Category> listCategoryParents = categoryService.getCategoryParents(category);
        Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
        List<Product> listProducts = pageProducts.getContent();
        
        long startCount = (pageNum - 1)*ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if(endCount > pageProducts.getTotalElements()){
			endCount=pageProducts.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("listCategoryParents", listCategoryParents);
        model.addAttribute("listProducts", listProducts);

        return "products_by_category";
    }
}
