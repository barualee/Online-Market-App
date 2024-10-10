package com.onlinemarket.OnlinemarketProjectBackend.category;

import java.io.IOException;
import java.util.List;

import org.springframework.data.repository.query.Param;
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
import com.onlinemarket.OnlinemarketProjectCommon.entity.Category;


@Controller
public class CategoryController {
    
    @Autowired
    private CategoryService service;

    @GetMapping("/categories")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model){
        return listByPage(1, sortDir, null, model);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
        @Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model){

        if(sortDir == null || sortDir.isEmpty()){
            sortDir = "asc";
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        CategoryPageInfo categoryPageInfo = new CategoryPageInfo();
        List<Category> listCategories = service.listByPage(categoryPageInfo,pageNum,sortDir, keyword);

        long startCount = (pageNum - 1)*CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if(endCount > categoryPageInfo.getTotalItems()){
			endCount=categoryPageInfo.getTotalItems();
		}

        model.addAttribute("totalPages", categoryPageInfo.getTotalPages());
        model.addAttribute("totalItems", categoryPageInfo.getTotalItems());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
        
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("listCategories", listCategories);
        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model){
        List<Category> listCategoriesInForm = service.listCategoriesInForm();
        
        model.addAttribute("listCategoriesInForm", listCategoriesInForm);
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
        @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException{
            
            if(!multipartFile.isEmpty()){
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                category.setImage(fileName);
                Category savedObject = service.save(category);
                String uploadDir = "category-photos/" + savedObject.getId();

                FileUploadUtil.deleteFiles(uploadDir);
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            } else {
                service.save(category);
            }
            redirectAttributes.addFlashAttribute("message", "The category has been saved successfully");
            return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name="id") Integer id, RedirectAttributes RedirectAttributes, Model model) {
		try {
			Category category = service.get(id);
			List<Category> listCategoriesInForm = service.listCategoriesInForm();
			
			model.addAttribute("category", category);
			model.addAttribute("listCategoriesInForm", listCategoriesInForm);
			model.addAttribute("pageTitle", "Edit Category");

			return "categories/category_form";
		} catch (CategoryNotFoundException e) {
			RedirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/categories";
		}

	}

    @GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
	@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes){
		
		service.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category ID " +id+ " has been " + status;

		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/categories";
	}

    @GetMapping("/categories/delete/{id}")
	public String deleteCategoryEnabledStatus(@PathVariable("id") Integer id,
	Model model, RedirectAttributes redirectAttributes){
		
        try{
            service.delete(id);
            String categoryDir = "category-photos/images/"+id;
            FileUploadUtil.deleteDir(categoryDir);

            String message = "The category ID " +id+ " has been deleted successfully";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (CategoryNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
		return "redirect:/categories";
	}

}
