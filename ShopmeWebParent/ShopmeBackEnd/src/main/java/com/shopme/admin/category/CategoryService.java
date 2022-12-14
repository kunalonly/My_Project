package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.shopme.common.entity.Category;

@Service
@Transactional
public class CategoryService {
@Autowired
private CategoryRepository repo;

public List<Category> listAll()
{
	List<Category> rootCategories =repo.findRootCategories(Sort.by("name").ascending());
	return listHierarchicalCategories(rootCategories);
}

private List<Category> listHierarchicalCategories(List<Category> rootCategories)
{
List<Category> 	hierarchicalCategories=new ArrayList<>();

for(Category rootCategory:rootCategories) {
	
	hierarchicalCategories.add(Category.copyFull(rootCategory));
	
	Set<Category> children =sortSubCategories(rootCategory.getChildren());
	
	for(Category subCategory:children)
	{
		String name="--"+subCategory.getName();
		hierarchicalCategories.add(Category.copyFull(subCategory,name));
		listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
	}
		
}
return hierarchicalCategories;
}

private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,Category parent ,
int subLevel)
{
	Set<Category> children=sortSubCategories(parent.getChildren());
	int newSubLevel=subLevel+1;
	
	for(Category subCategory:children)

	{
	String name="";
	for(int i=0;i<newSubLevel;i++)
	{
		
		name+="--";
	}
		name+=subCategory.getName();
		hierarchicalCategories.add(Category.copyFull(subCategory,name));
		listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
	}
	

}


public Category save(Category category)
{
return repo.save(category);
}

public List<Category> listCategoriesUsedInForm()
{
	List<Category> CategoriesUsedInForm=new ArrayList<>();
	Iterable<Category> CategoriesInDB=repo.findRootCategories(Sort.by("name").ascending());
	
	for( Category category :CategoriesInDB) {
		
		if(category.getParent()==null)
		{
			CategoriesUsedInForm.add(Category.copyNameAndId(category));	
			
		System.out.println(category.getName());
		Set<Category> children=sortSubCategories(category.getChildren());
		
		for(Category subCategory :children)
		{
			String name="--"+ subCategory.getName();
			CategoriesUsedInForm.add(Category.copyNameAndId(subCategory.getId(), name));
			
			
			listSubCategoriesUsedInForm(CategoriesUsedInForm,subCategory,1);
		
		}
		
		}
	}
	return CategoriesUsedInForm;
}

private void listSubCategoriesUsedInForm(List<Category> CategoriesUsedInForm,Category parent,int subLevel)
{
	int newSubLevel=subLevel+1;
	Set<Category> children=sortSubCategories(parent.getChildren());
	
	for(Category subCategory:children)
	{
		String name="";
		for(int i=0;i<newSubLevel;i++)
		{
			name+="--";
		}
		name+=subCategory.getName();
		CategoriesUsedInForm.add(Category.copyNameAndId(subCategory.getId(), name));	
		listSubCategoriesUsedInForm(CategoriesUsedInForm,subCategory, newSubLevel);
	}
}

public Category get(Integer id) throws CategoryNotFoundException
{
	try {
		return repo.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category with ID"+id); 
		}
}

public String checkUnique(Integer id,String name,String alias)
{
	
boolean isCreatingNew=(id==null||id==0);
Category categaryByName=repo.findByName(name);
 if(isCreatingNew)
 {
	 if(categaryByName!=null){
return "DuplicateName"	;	 
	 }
	 else {
		 
		 Category  categoryByAlias=repo.findByAlias(alias);
		 if(categoryByAlias!=null)
		 {
			 return "DuplicateAlias";
		 }
		  }	
 }
	 else {
		 if(categaryByName!=null &&categaryByName.getId()!=id) {
			return "DuplicateName" ;
			 		 }
		 Category  categoryByAlias=repo.findByAlias(alias);
		if(categoryByAlias!=null && categoryByAlias.getId()!=id) 
		{
			return "DuplicateAlias";
		}
		 
	 }
 return "OK";
}


private SortedSet<Category> sortSubCategories(Set<Category> children) {
	SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
		@Override
		public int compare(Category cat1, Category cat2) {
			
				return cat1.getName().compareTo(cat2.getName());
			} 
		
	});

	sortedChildren.addAll(children);

	return sortedChildren;
}

public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
repo.updateEnabledStatus(id, enabled);
	
}
}
