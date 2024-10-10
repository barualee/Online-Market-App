package com.onlinemarket.OnlinemarketProjectBackend.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Role;
import com.onlinemarket.OnlinemarketProjectCommon.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	public static final int USERS_PER_PAGE = 2;
	@Autowired
	private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
	
	public List<User> listAll() {
		return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
	}

    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum-1,USERS_PER_PAGE, sort);
        if (keyword != null){
            return userRepo.findAll(keyword, pageable);
        }

        return userRepo.findAll(pageable);
    }

    public User getByEmail(String email){
        return userRepo.getUserByEmail(email);
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepo.findAll();
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            User existingUser = userRepo.findById(user.getId()).get();

            if(user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }
        
    	return userRepo.save(user);
    }

    public User updateAccount(User userInForm){
        User userInDb = userRepo.findById(userInForm.getId()).get();

        if(!userInForm.getPassword().isEmpty()){
            userInDb.setPassword(userInForm.getPassword());
            encodePassword(userInDb);
        }
        if(userInForm.getPhotos() != null){
            userInDb.setPhotos(userInForm.getPhotos());
        }
        userInDb.setFirstName(userInForm.getFirstName());
        userInDb.setLastName(userInForm.getLastName());

        return userRepo.save(userInDb);
    }
    
    private void encodePassword(User user) {
    	String encodedPassword = passwordEncoder.encode(user.getPassword());
    	user.setPassword(encodedPassword);
    }
    
    public boolean isEmailUnique(Integer id, String email) {
    	User userByEmail = userRepo.getUserByEmail(email);

        if (userByEmail == null) return true;

        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {
            if (userByEmail != null) return false;
        }
        else {
            if (userByEmail.getId() != id) {
                return false;
            }
        }
    	return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("Could not find user with ID: "+id);
        }
        
    }

    public void delete(Integer id) throws UserNotFoundException{
        
        //we only get the count of user in the db, without calling the entire user object.
        Long countById = userRepo.countById(id);
        if(countById == null || countById == 0){
            throw new UserNotFoundException("Could not find user with ID: "+id);
        }
        userRepo.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean status){
        userRepo.updateEnabledStatus(id, status);
    }
}