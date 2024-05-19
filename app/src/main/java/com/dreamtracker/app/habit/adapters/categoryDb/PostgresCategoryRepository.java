package com.dreamtracker.app.habit.adapters.categoryDb;


import com.dreamtracker.app.habit.domain.model.Category;
import com.dreamtracker.app.habit.domain.ports.CategoryRepositoryPort;
import com.dreamtracker.app.infrastructure.repository.SpringDataCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Component
public class PostgresCategoryRepository implements CategoryRepositoryPort {
   private final SpringDataCategoryRepository springDataCategoryRepository;


   @Override
   public Category save(Category category) {
      return springDataCategoryRepository.save(category);
   }

   @Override
   public Boolean existsById(UUID id) {
      return springDataCategoryRepository.existsById(id);
   }

   @Override
   public void deleteById(UUID id) {
      springDataCategoryRepository.deleteById(id);
   }

   @Override
   public Optional<Category> findById(UUID id) {
      return springDataCategoryRepository.findById(id);
   }

   @Override
   public List<Category> findByUserUUID(UUID userUUID) {
      return springDataCategoryRepository.findByUserUUID(userUUID);
   }
}
