package com.example.springdatarest;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "person", path = "person")
@RestResource
public interface PersonRepo
    extends
        CrudRepository<Person, Long>,
        PagingAndSortingRepository<Person,Long>,
        QuerydslPredicateExecutor<Person>
{
}
