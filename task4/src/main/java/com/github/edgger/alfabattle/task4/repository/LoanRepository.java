package com.github.edgger.alfabattle.task4.repository;

import com.github.edgger.alfabattle.task4.model.Loan;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface LoanRepository extends ElasticsearchRepository<Loan, String> {

    List<Loan> findAllByDocument(String docId);

}
