package com.shopme.admin.user;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

@Autowired
	private RoleRepository repo;
@Test
public void testCreateFirstRole() {
	
	Role roleAdmin=new Role("Admin","manage everything");
	Role savedRole=repo.save(roleAdmin);
	assertThat(savedRole.getId()).isGreaterThan(0);
	
}
@Test
public void testRestRole()
{
Role roleSalesperson=new Role("Salesperson","manage product price, Shipping,order,sales report");
Role roleEditor= new Role("Editor","Manage Category,Brand,product,Article,Menues");
Role roleShipper=new Role("Shipper","View oders ,View product,and update order status");
Role roleAssistant=new Role("Assistant","Manage Custome and Review");
repo.saveAll(List.of(roleSalesperson,roleEditor,roleShipper,roleAssistant));

}

}