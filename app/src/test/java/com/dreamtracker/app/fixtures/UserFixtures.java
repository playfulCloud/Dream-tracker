package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.user.domain.model.User;

import java.util.UUID;

public interface UserFixtures {

    default User.UserBuilder getSampleUser(UUID uuid){
        return User.builder()
                .uuid(uuid);

    }


    default User.UserBuilder getSampleUserWithCategories(UUID uuid){
        return User.builder()
                .uuid(uuid);
    }

}
