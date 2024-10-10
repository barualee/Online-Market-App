package com.onlinemarket.OnlinemarketProjectBackend.user;

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

import com.onlinemarket.OnlinemarketProjectCommon.entity.Role;
import com.onlinemarket.OnlinemarketProjectCommon.entity.User;

@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		
		User firstUser = new User("lee", "barua", "barua.nishant97@gmail.com", "abcd@123");
		firstUser.addRole(roleAdmin);
		
		User savedUser = repo.save(firstUser);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUserTwoRoles() {
		User newUser = new User("lisa", "barua", "rikshitabarua56@gmail.com", "lee@1997");
		
		Role editor = new Role(3);
		Role assistant = new Role(5);
		
		newUser.addRole(editor);
		newUser.addRole(assistant);
		
		User savedUser = repo.save(newUser);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> list = repo.findAll();
		list.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User user = repo.findById(1).get();
		System.out.println(user);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User user = repo.findById(17).get();
		user.setEnabled(true);
		
		repo.save(user);
	}
	
	@Test
	public void testUpdateUserRoles() {
		
		User user = repo.findById(16).get();
		
		Role admin1 = new Role(1);
		Role salesperson = new Role(2);
		
		user.getRoles().remove(admin1);
		user.addRole(salesperson);
		
		repo.save(user);
		
		//second user test
		// User user2 = repo.findById(16).get();
		
		// Role admin = new Role(1);
		// user2.addRole(admin);
		// repo.save(user2);	
	}
	
	@Test
	public void testDeleteUser() {
		repo.deleteById(15);
		// repo.deleteById(14);
		//repo.deleteById(12);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "a@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}

	@Test
	public void testCountById() {
		Integer id = 100;
		Long countById = repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testDisableUser(){
		Integer id = 16;
		repo.updateEnabledStatus(id, false);
	}

	@Test
	public void testEnableUser(){
		Integer id = 16;
		repo.updateEnabledStatus(id, true);
	}

	@Test
	public void testListFirstPage(){
		int pageNumber = 0;
		int pageSize = 2;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);

		List<User> listUser = page.getContent();
		listUser.forEach(user -> System.out.println(user));
		assertThat(listUser.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUser(){
		String keyword="lisa";

		int pageNumber = 0;
		int pageSize = 2;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);

		List<User> listUser = page.getContent();
		listUser.forEach(user -> System.out.println(user));
		assertThat(listUser.size()).isGreaterThan(0);
	}
}
