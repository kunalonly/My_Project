package com.shopme.admin.user;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.User;
public interface UserRepository extends PagingAndSortingRepository<User,Integer>{

	@Query("SELECT u FROM User u WHERE u.email=:email" )
	public User getUserByEmail(@Param("email")String email);
@Query("SELECT u FROM User u WHERE  CONCAT(u.id,' ',u.email,' ',u.firsrName,' ',u.lastName) LIKE %?1% ")
	public Page<User> findAll(String keyworld,Pageable pageable);
	
	
	
public long countById(Integer id);
@Query("UPDATE User u SET u.enable=?2 where u.id=?1")
@Modifying
public void updateEnableStatus(Integer id,boolean enabled);
}
