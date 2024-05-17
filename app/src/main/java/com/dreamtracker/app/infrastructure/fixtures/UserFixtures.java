package com.dreamtracker.app.infrastructure.fixtures;

import com.dreamtracker.app.domain.entity.User;

import java.util.UUID;

public interface UserFixtures {

    default User.UserBuilder getSampleUser(UUID uuid){
        return User.builder()
                .uuid(uuid)
                .name("John")
                .surname("Doe");

    }


    default User.UserBuilder getSampleUserWithCategories(UUID uuid){
        return User.builder()
                .uuid(uuid)
                .name("John")
                .surname("Doe");
    }

}
