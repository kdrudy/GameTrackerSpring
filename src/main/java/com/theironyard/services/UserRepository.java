package com.theironyard.services;

import com.theironyard.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by kdrudy on 12/22/16.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    User findFirstByName(String userName);
}
