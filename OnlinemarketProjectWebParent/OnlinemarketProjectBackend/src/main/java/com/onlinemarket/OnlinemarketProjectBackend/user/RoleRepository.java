package com.onlinemarket.OnlinemarketProjectBackend.user;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{

}
