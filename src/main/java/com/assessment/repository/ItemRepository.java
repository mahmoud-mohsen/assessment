package com.assessment.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.assessment.entity.Items;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Items, Integer>, JpaSpecificationExecutor<Items> {
}
