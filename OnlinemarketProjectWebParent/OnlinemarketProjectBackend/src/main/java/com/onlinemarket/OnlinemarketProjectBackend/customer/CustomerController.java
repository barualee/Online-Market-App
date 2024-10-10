package com.onlinemarket.OnlinemarketProjectBackend.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Customer;

@Controller
public class CustomerController {
    @Autowired
	private CustomerService service;
	
	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		return listByPage(1, model,"firstName","asc", null);
	}

    @GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model,
	@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword){
		
		Page<Customer> page = service.listByPage(pageNum,sortField, sortDir, keyword);
		List<Customer> listCustomers = page.getContent();

		long startCount = (pageNum - 1)*CustomerService.CUSTOMERS_PER_PAGE + 1;
		long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;
		
        if(endCount > page.getTotalElements()){
			endCount=page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listCustomers", listCustomers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "customers/customers";
	}

    @GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
	@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes){
		
		service.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Customer ID " +id+ " has been " + status;

		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/customers";
	}

    @GetMapping("/customers/details/{id}")
    public String viewCustomer(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes, Model model){
        try{
            Customer customer = service.get(id);
            model.addAttribute("customer", customer);
            return "customers/customer_detail_modal";
        } catch(CustomerNotFoundException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/save")
	public String saveCustomer(Customer customer, RedirectAttributes RedirectAttributes, Model model) {
        service.save(customer);
		RedirectAttributes.addFlashAttribute("message", "The Customer ID "+ customer.getId() +" has been saved successfully!");
		return "redirect:/customers";
	}

    @GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable(name="id") Integer id, RedirectAttributes RedirectAttributes, Model model) {
		try {
			Customer customer = service.get(id);
			List<Country> listCountries = service.listAllCountries();
			
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", "Edit Customer");
			model.addAttribute("listCountries", listCountries);

			return "customers/customer_form";
		} catch (CustomerNotFoundException e) {
			RedirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
	}

    @GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable(name="id") Integer id, RedirectAttributes RedirectAttributes, Model model) {
		try {
			service.delete(id);
			RedirectAttributes.addFlashAttribute("message", "The Customer ID "+id+" has been deleted successfully.");
		} catch (CustomerNotFoundException e) {
			RedirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/customers";

	}
}
