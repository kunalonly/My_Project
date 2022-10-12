package com.shopme.admin.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller

public class UserController {
@Autowired
public UserService service;
@GetMapping("/users")
public String listFirstpage( Model model) {
	
	return listByPage(1, model,"firsrName","asc",null);

}
@GetMapping("/users/page/{pageNum}")
public String listByPage(@PathVariable(name="pageNum") int pageNum,Model model, @Param("sortField"
		) String sortField ,@Param("sortDir") String sortDir ,@Param("keyword") String keyword) {
	System.out.println("Sort Field "+sortField);
	System.out.println("Sort Order "+sortDir);
	Page<User> page=service.listByPage(pageNum,sortField,sortDir,keyword);
	
	List<User>listUsers=page.getContent();
	long startCount=(pageNum-1)*UserService.USERS_PER_PAGES+1;
	long endCount=startCount+UserService.USERS_PER_PAGES-1;
if(endCount>page.getTotalElements())
{
endCount=page.getTotalElements();
}
String reversSortDir=sortDir.equals ("asc")?"desc":"asc";
model.addAttribute("Currentpage",pageNum);	
model.addAttribute("Totalpages",page.getTotalPages());
model.addAttribute("Startcount",startCount);
model.addAttribute("Endcount",endCount);
model.addAttribute("Totalitems",page.getTotalElements());
model.addAttribute("sortField",sortField);
model.addAttribute("sortDir",sortDir);
model.addAttribute("reversSortDir", reversSortDir);
model.addAttribute("keyword", keyword);
model.addAttribute("listUsers", listUsers);
	
	return"users";
}
@GetMapping("/users/newuser")
public String newUser(Model model) {
	List<Role> listRole=service.listrole();
	User user =new User();
	user.setEnable(true);
	model.addAttribute("user", user);
	model.addAttribute("listRole", listRole);
	model.addAttribute("pageTitle","Create New User");
	return "user_form";
}
@PostMapping("/users/save")	
public String saveUser(User user, RedirectAttributes redirectAttributes,@RequestParam("image")
	MultipartFile multipartFile) throws IOException 
{
	if(!multipartFile.isEmpty())
{
	System.out.println(user);
	System.out.println(multipartFile.getOriginalFilename());
	String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
	user.setPhoto(fileName);
	User saveUser=service.save(user);
	
	String uploadDir="users-photos/"+saveUser.getId();
	FileUploadUtil.CleanDir(uploadDir);
	FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	}	
	else {
		if(user.getPhoto().isEmpty())user.setPhoto(null);
		service.save(user);
	}
	
	
	redirectAttributes.addFlashAttribute("message", "User Create Successfully");
	
	return "redirect:/users";
}

@GetMapping("/users/edit/{id}")

public String editUser(@PathVariable(name="id")Integer id,RedirectAttributes 
		redirectAttributes,Model model)

{

	try {
		
		User user=service.get(id);
		List<Role> listRole=service.listrole();
		model.addAttribute("user",user);
		model.addAttribute("pageTitle","Edit User(ID:"+id+")");
		model.addAttribute("listRole", listRole);
		return "user_form";
	} catch (UserNotFoundException ex) {
		// TODO Auto-generated catch block

		redirectAttributes.addFlashAttribute("message", ex.getMessage());
		return "redirect:/users";
	}
}	

@GetMapping("/users/delete/{id}")

	public String deleteUser(@PathVariable(name="id")Integer id,RedirectAttributes redirectAttributes,Model model)
	{
try {	
	service.delete(id);
	redirectAttributes.addFlashAttribute("message","The User with User ID "+id+" Deleted successfully");
	
	} catch (UserNotFoundException ex) {
		
		redirectAttributes.addFlashAttribute("message", ex.getMessage());
	
	}
return "redirect:/users";
	}
@GetMapping("/users/{id}/enable/{status}")

public String updateuserStatus(@PathVariable("id")Integer id,@PathVariable("status") boolean enable,RedirectAttributes redirectAttributes
		,Model model)
{
service.updateUserEnaleStatus(id, enable);
String status=enable?"enable":"disable";
String message="the user ID "+id+" Has been" +status;
redirectAttributes.addFlashAttribute(message, message);
return "redirect:/users";
}

}
