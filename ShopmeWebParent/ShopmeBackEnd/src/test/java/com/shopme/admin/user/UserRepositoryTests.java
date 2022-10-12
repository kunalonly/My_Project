package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
@Autowired
private UserRepository repo;
@Autowired
private TestEntityManager entityManager;
@Test
public void testCreateUserWithOneUserRole() {
Role roleAdmin=	entityManager.find(Role.class,1);
	User userkunal=new User("kunal1@gmail.com","kunal@123","Kunal","Raipurkar");
	userkunal.addRole(roleAdmin);
	User saveUser=repo.save(userkunal);
	assertThat(saveUser.getId()).isGreaterThan(0);
	
}
@Test
public void testCreateUserWithTwoUser() {
	User userabhi=new User("abhi@gmail.com","abhi8446","Abhijeet","Raipurkar");
	Role roleEditor=new Role(3);
	Role roleAssitant=new Role(5);
	
	userabhi.addRole(roleEditor);
	userabhi.addRole(roleAssitant);
	User savedUser=repo.save(userabhi);
	assertThat(savedUser.getId()).isGreaterThan(0);
	
}
@Test
public void testListAllUser() {
Iterable<User> listUsers=repo.findAll();
listUsers.forEach(user->System.out.println(user));
}
@Test
public void testGetUserbyID() {
	User Kunal=repo.findById(1).get();
	System.out.println(Kunal);
	assertThat(Kunal).isNotNull();
	
}
@Test 
public void testUpadateUser()
{
	User Kunal=repo.findById(1).get();
Kunal.setEnable(true);
Kunal.setEmail("kunalraipurkar31@gmail.com");
repo.save(Kunal);
}
@Test
public void testUserUpdateRole()
{
User abhi=repo.findById(2).get();
Role Editor=new Role(3);
Role salePerson=new Role(2);
abhi.getRoles().remove(Editor);
abhi.addRole(salePerson);
repo.save(abhi);
}
@Test
public void testDeleteUser() {
Integer ID =2;
repo.deleteById(ID);
}

@Test 
public void testGetUserByEmailID() {
	
	String email="kunalraipurkar31@gmail.com";
		User user=repo.getUserByEmail(email);
		assertThat(user).isNotNull();
}
@Test
public void testCountById() {
	Integer id =10;
	long countById=repo.countById(id);
	assertThat(countById).isNotNull().isGreaterThan(0);
}
@Test
public void testUSerDisable() {
	Integer id=1;
	repo.updateEnableStatus(id, false);
}
@Test
public void testuserEnable()
{
	Integer id=1;
	repo.updateEnableStatus(id, true);
}
@Test
public void pageListFirstPage()
{
	int pageNumber=1;
	int pagesize=4;
	Pageable pageable=PageRequest.of(pageNumber, pagesize);
	Page<User>page=repo.findAll(pageable);
	List<User>listUsers=page.getContent();
	listUsers.forEach(user->System.out.println(user));
	assertThat(listUsers.size()).isEqualTo(pagesize);
	
	
	
}


}

