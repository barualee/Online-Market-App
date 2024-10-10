package com.onlinemarket.OnlinemarketProjectBackend.product;

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
import com.onlinemarket.OnlinemarketProjectBackend.brand.BrandService;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Brand;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Product;

@Controller
public class ProductController {
    @Autowired
    private ProductService service;

    @Autowired
    private BrandService brandService;

    @GetMapping("/products")
    public String listAll(Model model){
        List<Product> listProducts = service.findAll();
        model.addAttribute("listProducts", listProducts);
        return "products/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model){
        List<Brand> listBrands = brandService.findAll();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("listBrands", listBrands);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("numberOfExistingExtraImages", 0);
        return "products/products_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product,
        @RequestParam(name = "detailNames", required = false) String[] detailNames,
        @RequestParam(name = "detailValues", required = false) String[] detailValues,
        @RequestParam("extraImages") MultipartFile[] extraImageMultipartFiles,
        @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException{
            
            setmainImage(multipartFile, product);
            setExtraImageNames(extraImageMultipartFiles, product);
            setProductDetails(detailNames, detailValues, product);
            
            Product savedObject = service.save(product);
            saveExtraImages(multipartFile, extraImageMultipartFiles, savedObject);

            redirectAttributes.addFlashAttribute("message", "The product has been saved successfully");
            return "redirect:/products";
    }

    private void saveExtraImages(MultipartFile multipartFile, MultipartFile[] extraImageMultipartFiles,
            Product savedObject) throws IOException {

        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        
            String uploadDir = "product-images/" + savedObject.getId();
            FileUploadUtil.deleteFiles(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        if(extraImageMultipartFiles.length > 0){
            String uploadDir = "product-images/" + savedObject.getId() + "/extras";
            for(MultipartFile multipartFileExtraImage : extraImageMultipartFiles){
                if(!multipartFileExtraImage.isEmpty()) continue;
                
                String fileName = StringUtils.cleanPath(multipartFileExtraImage.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFileExtraImage);
            }
        }
    }

    private void setExtraImageNames(MultipartFile[] extraImageMultipartFiles, Product product) {
        if(extraImageMultipartFiles.length > 0){
            for(MultipartFile multipartFile : extraImageMultipartFiles){
                if(!multipartFile.isEmpty()){
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    product.addExtraImages(fileName);
                }
            }
        }
    }

    private void setmainImage(MultipartFile multipartFile, Product product) {
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
        if(detailNames == null || detailValues == null) return;

        for(int i=0; i < detailNames.length; i++){
            String name = detailNames[i];
            String value = detailValues[i];

            if(!name.isEmpty() && !value.isEmpty()){
                product.addDetail(name, value);
            }
        }
    }

 @GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id,
	@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes){
		
		service.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product ID " +id+ " has been " + status;

		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/products";
	}

    @GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name="id") Integer id, RedirectAttributes RedirectAttributes, Model model) {
		try {
			service.delete(id);
            String productExtraImagesDir = "product-images/" + id + "/extras";
            String productImagesDir = "product-images/" + id;
            FileUploadUtil.deleteDir(productExtraImagesDir);
            FileUploadUtil.deleteDir(productImagesDir);

			RedirectAttributes.addFlashAttribute("message", "The Product ID "+id+" has been deleted successfully.");
		} catch (ProductNotFoundException e) {
			RedirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/products";

	}

    @GetMapping("/products/edit/{id}")
    public String editproduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Product product = service.get(id);
            List<Brand> listBrands = brandService.findAll();
            Integer numberExistingImages = product.getImages().size();

            model.addAttribute("numberExistingImages", numberExistingImages);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Edit Product");

        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/products";
    }
}
