package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(length=128,nullable=false,unique=true)
	private String email;
	@Column(length=64,nullable=false)
	private String password;
	@Column(name="first_name",length=128,nullable=false)
	private String firsrName;
	@Column(name="last_name",length=128,nullable=false)
	private String lastName;
	@Column(length=64)
	private String photo;
	private boolean enable;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="user_roles",
			joinColumns=@JoinColumn(name="user_id"),
			inverseJoinColumns=@JoinColumn(name="user_role")
			)
	private Set<Role>roles=new HashSet<>();

	
	
	
	public User() {
	}

	public User(String email, String password, String firsrName, String lastName) {
		
		this.email = email;
		this.password = password;
		this.firsrName = firsrName;
		this.lastName = lastName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirsrName() {
		return firsrName;
	}

	public void setFirsrName(String firsrName) {
		this.firsrName = firsrName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role)
	{
		this.roles.add(role);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firsrName=" + firsrName + ", lastName=" + lastName
				+ ", roles=" + roles + "]";
	}
  @Transient
  
  public String getPhotosImagePath()
  {
	  if(id==null||photo==null) return "/images/default-user.png";
		  return "/users-photos/"+this.id+"/"+this.photo;
  }
}
