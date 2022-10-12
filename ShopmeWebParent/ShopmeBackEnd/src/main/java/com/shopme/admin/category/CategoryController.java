package com.shopme.admin.category;

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

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
@Autowired
private CategoryService service;

@GetMapping("/categories")
public String listAll(Model model)
{
	List<Category> listCategories=service.listAll();
	model.addAttribute("listCategories",listCategories);
	return "categories/categories";
}

@GetMapping("/categories/new")
public String newCatagory(Model model)
{
List<Category> listCategories	=service.listCategoriesUsedInForm();
	model.addAttribute("category",new Category());
	model.addAttribute("listCategories",listCategories);
	model.addAttribute("pageTitle","Create New Category");
	return "categories/category_form";
}
	
@PostMapping("/categories/save")
public String saveCategory(Category category,
		@RequestParam("fileImage") MultipartFile multipartfile,
		RedirectAttributes ra) throws IOException
{
	if(!multipartfile.isEmpty()) {
		String fileName=StringUtils.cleanPath(multipartfile.getOriginalFilename());
		category.setImage(fileName);
	Category saveCategory=service.save(category);
	String uploadDir="../category-image/"+ saveCategory.getId();
	FileUploadUtil.saveFile(uploadDir, fileName, multipartfile);
		
	}
	else {
		service.save(category);
	}

ra.addFlashAttribute("messege","The new category added successfully! ");
 return "redirect:/categories";
}


@GetMapping("/categories/edit/{id}")
public String editCatagory(@PathVariable(name="id")Integer id, Model model,RedirectAttributes ra)
{
try {
	
	Category category=service.get(id);
	List<Category> listCategories=service.listCategoriesUsedInForm();
	model.addAttribute("category",category);
	model.addAttribute("listCategories", listCategories);
	model.addAttribute("pageTitle","Edit Category (ID:"+id+")");
	
	return "categories/category_form";
}	
catch(CategoryNotFoundException ex) {
	ra.addFlashAttribute("message",ex.getMessage());
	return "redirect:/categories";
}

}

@GetMapping("/categories/{id}/enabled/{status}")
public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
		@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
	
	
	service.updateCategoryEnabledStatus(id, enabled);
	String status = enabled ? "enabled" : "disabled";
	String message = "The category ID " + id + " has been " + status;
	
	
	redirectAttributes.addFlashAttribute("messageSuccess", message);

	return "redirect:/categories";
}

}
