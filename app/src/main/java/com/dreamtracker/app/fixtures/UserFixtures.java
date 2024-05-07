package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.entity.User;

import java.util.ArrayList;
import java.util.UUID;

public interface UserFixtures {

    default User.UserBuilder getSampleUser(UUID uuid){
        return User.builder()
                .uuid(uuid)
                .name("John")
                .surname("Doe")
                .categoriesCreatedByUser(new ArrayList<>());
    }

}
