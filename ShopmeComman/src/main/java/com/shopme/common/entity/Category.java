package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="categories")

public class Category {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128,nullable = false,unique = true)
	private String name;
	
	@Column(length =64,nullable = false,unique = true)
	private String alias;
	
	@Column(length = 128,nullable = false)
	private String image;
	
	
	private boolean	enabled;
	
	@Column(name = "all_parent_ids", length = 256, nullable = true)
	private String allParentIDs;
	
	@OneToOne
	@JoinColumn(name="parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children=new HashSet<>();

	public Category() {
		
	}
	
	public Category(Integer id) {
		this.id=id;
	}
	
	public static Category copyNameAndId(Category category) {
	Category copyCatgory=new Category();
	copyCatgory.setId(category.getId());
	copyCatgory.setName(category.getName());
       return  copyCatgory;
	}


	public static Category copyNameAndId(Integer id,String name) {
	Category copyCatgory=new Category();
	copyCatgory.setId(id);
	copyCatgory.setName(name);
       return  copyCatgory;
	}

	public static Category copyFull(Category category) 
	{
		Category copyCatgory=new Category();
		copyCatgory.setId(category.getId());
		copyCatgory.setName(category.getName());
		copyCatgory.setImage(category.getImage());
		copyCatgory.setAlias(category.getAlias());
		copyCatgory.setEnabled(category.getEnabled());
		
	       return  copyCatgory;
		}
	
	public static Category copyFull(Category category, String name) 
	{
		Category copyCatgory=Category.copyFull(category);
		copyCatgory.setName(name);
		return copyCatgory;
	}	
	
	
	public Category(String name) {
		
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}

	
	public Category(String name,Category parent) {
		this(name);
		this.parent=parent;
	}
	
	
	public Category(Integer id, String name, String alias) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	@Transient
	public String getImagePath() {
		if(this.id==null)
			return "/images/image-thumbnail.png";
		
				return "/category-images/"+this.id+ "/"+this.image;
	}
	
	@Override
	public String toString(){
		return this.name;
	}

	
}
