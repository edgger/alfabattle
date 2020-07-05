package com.github.edgger.alfabattle.task4.repository;

import com.github.edgger.alfabattle.task4.model.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface PersonRepository extends ElasticsearchRepository<Person, String> {

    Optional<Person> findByDocId(String documentId);

}
