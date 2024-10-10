package com.onlinemarket.OnlinemarketProjectBackend.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	
	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role adminRole = new Role("Admin", "manage everything");
		Role savedRole = repo.save(adminRole);
		
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void createRestRoles() {
		Role salesPersonRole = new Role("SalesPerson", "manage product price, customer, shipping, orders and sales reports");
		Role editorRole = new Role("Editor", "manage categories, brands, products, articles and menus");
		Role shipperRole = new Role("Shipper", "view products, orders and update order status");
		Role assistantRole = new Role("Assistant", "manage questions and reviews");
		
		repo.saveAll(List.of(salesPersonRole, editorRole, shipperRole, assistantRole));	
	}
}


