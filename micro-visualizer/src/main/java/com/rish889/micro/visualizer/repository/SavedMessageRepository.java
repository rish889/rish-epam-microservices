package com.rish889.micro.visualizer.repository;

import com.rish889.micro.visualizer.entity.SavedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedMessageRepository extends JpaRepository<SavedMessage, Long> {
}
