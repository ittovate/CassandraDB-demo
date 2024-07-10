package com.example.cassandradbdemo.repos;

import java.util.UUID;

import com.example.cassandradbdemo.models.Vet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VetRepository extends CrudRepository<Vet, UUID> {
    Vet findByFirstName(String username);
}