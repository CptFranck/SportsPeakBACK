package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetSetRepository extends CrudRepository<TargetSetEntity, Long> {
}
