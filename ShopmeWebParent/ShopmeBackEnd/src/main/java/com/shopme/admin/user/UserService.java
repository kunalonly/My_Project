package com.shopme.admin.user;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {
public static final int USERS_PER_PAGES=5;
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private RoleRepository rolerepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	public List<User> listAll() {
		return (List<User>) userrepo.findAll();
	}
	public Page<User> listByPage(int pageNum ,String sortField, String sortDir,String keyword)
	{
		Sort sort=Sort.by(sortField);
		sort=sortDir.equals("asc")? sort.ascending():sort.descending();
		
		
		Pageable pageble=PageRequest.of(pageNum-1, USERS_PER_PAGES,sort);
		
		
		if(keyword!=null)
		{
			return userrepo.findAll(keyword, pageble);
		}
		return userrepo.findAll(pageble);
	}
	
public List<Role> listrole(){
	return (List<Role>)rolerepo.findAll();
}
public User save(User user) {
	boolean isUpdatingUser=(user.getId()!=null);
	if (isUpdatingUser)
	{
		User exitingUser=userrepo.findById(user.getId()).get();
		if (user.getPassword().isEmpty()) {
			user.setPassword(exitingUser.getPassword());
		}
		else {
	encodePassword(user);
		}
		}
	else {
	encodePassword(user);
	    }
	return userrepo.save(user);
	
	
}

private void encodePassword(User user)
{
	String encodedPassword=passwordEncoder.encode(user.getPassword());
	user.setPassword(encodedPassword);
}
	public boolean isEmailUnique(Integer id,String email) {
		User userByEmail=userrepo.getUserByEmail(email);
		if(userByEmail==null)
			return true;
		boolean isCreatingNew=(id==null);
		if(isCreatingNew)
		{
			if(userByEmail != null) 
				return false;
		}else
				{
				if(userByEmail.getId()!= id)
				{
					return false;
				}
				}
		
		return true;
	}
	public User get(Integer id) throws UserNotFoundException {
		// TODO Auto-generated method stub
		try
		{
			return userrepo.findById(id).get();
		}
		catch(NoSuchElementException ex)
		{
			throw new UserNotFoundException("Could not Find such User with ID"+id);
		}
	}
	public void delete(Integer id)throws UserNotFoundException
	{
		Long countById=userrepo.countById(id);
		if(countById == null || countById==0)
		{
			throw new UserNotFoundException("Could not Find such User with ID"+id);
		}
	userrepo.deleteById(id);
	}
public void updateUserEnaleStatus(Integer id,boolean enabled) {
	userrepo.updateEnableStatus(id,  enabled);
}

}
