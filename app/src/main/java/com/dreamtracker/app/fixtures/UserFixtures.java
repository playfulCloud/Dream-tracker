package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.entity.Category;
import com.dreamtracker.app.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface UserFixtures {

    default User.UserBuilder getSampleUser(UUID uuid){
        return User.builder()
                .uuid(uuid)
                .name("John")
                .surname("Doe")
                .categoriesCreatedByUser(new ArrayList<>()
                );
    }


    default User.UserBuilder getSampleUserWithCategories(UUID uuid){
        return User.builder()
                .uuid(uuid)
                .name("John")
                .surname("Doe")
                .categoriesCreatedByUser(List.of(
                        Category.builder()
                                .id(UUID.fromString("f13c542a-9303-4bfd-bddb-ec32de5c0cc5"))
                                .name("bar")
                                .build()
                ));
    }

}
