package com.onlinemarket.OnlinemarketProjectBackend.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.onlinemarket.OnlinemarketProjectBackend.FileUploadUtil;

import com.onlinemarket.OnlinemarketProjectBackend.category.CategoryService;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Brand;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Category;

@Controller
public class BrandController {

    @Autowired
    private BrandService service;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listAll(Model model){
        List<Brand> listBrands = service.findAll();

        model.addAttribute("listBrands", listBrands);
        return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model){
        List<Category> listCategories = categoryService.listCategoriesInForm();
        
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Brand");
        return "brands/brand_form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand,
        @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException{
            
            if(!multipartFile.isEmpty()){
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                brand.setLogo(fileName);
                
                Brand savedObject = service.save(brand);
                String uploadDir = "brand-logos/" + savedObject.getId();

                FileUploadUtil.deleteFiles(uploadDir);
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            } else {
                service.save(brand);
            }
            redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully");
            return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable(name="id") Integer id, RedirectAttributes RedirectAttributes, Model model) {
		try {
			Brand brand = service.get(id);
			List<Category> listCategoriesInForm = categoryService.listCategoriesInForm();
			
			model.addAttribute("brand", brand);
			model.addAttribute("listCategoriesInForm", listCategoriesInForm);
			model.addAttribute("pageTitle", "Edit Brand");

			return "brands/brand_form";
		} catch (BrandNotFoundException e) {
			RedirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}

	}

    @GetMapping("/brands/delete/{id}")
	public String deleteCategoryEnabledStatus(@PathVariable("id") Integer id,
	Model model, RedirectAttributes redirectAttributes){
		
        try{
            service.delete(id);
            String categoryDir = "brand-logos/images/"+id;
            FileUploadUtil.deleteDir(categoryDir);

            String message = "The brand ID " +id+ " has been deleted successfully";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (BrandNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
		return "redirect:/brands";
	}
}
