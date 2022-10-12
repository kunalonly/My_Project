package com.shopme.admin.category;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {

	@Autowired
	private CategoryRepository repo;
	
	
	@Test
	public void testCreateRootCategory() {
		
		Category category=new Category("electronics");
	Category savecategory	=repo.save(category);
		assertThat(savecategory.getId()).isGreaterThan(0);
	
	}
	@Test
	public void testCreateSubCategory() {
		
		Category parent =new Category(5);
		Category subCategory=new Category("memory", parent);
		Category saveCategory= repo.save(subCategory);
		assertThat(saveCategory.getId()).isGreaterThan(0);
		
		
	}
	
	@Test
	public void testGetCategory() {
		
		Category category=repo.findById(1).get();
		System.out.println(category.getName());
		
		Set<Category> children=category.getChildren();
		for(Category subCategory :children)
		{
			System.out.println(subCategory.getName());
		}
	assertThat(children.size()).isGreaterThan(0);
	
	}
	@Test
	public void testListRootCategories() {
		List<Category> rootCategories = repo.findRootCategories(Sort.by("name").ascending());
		rootCategories.forEach(cat -> System.out.println(cat.getName()));
	}

	@Test
	public void testFindByName() {
		String name = "computers"; // Computers1
		Category category = repo.findByName(name);

		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
}
	@Test
	public void testFindByAlias() {
		String alias = "electronics";
		Category category = repo.findByAlias(alias);

		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(alias);
	}
	
	}
