package com.rish889.micro.collector.repository;

import com.rish889.micro.collector.entity.CollectedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectedMessageRepository extends JpaRepository<CollectedMessage, Long> {
}
