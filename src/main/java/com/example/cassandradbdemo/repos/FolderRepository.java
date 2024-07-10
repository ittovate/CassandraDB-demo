package com.example.cassandradbdemo.repos;

import com.example.cassandradbdemo.models.Folder;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FolderRepository extends CassandraRepository<Folder, String> {
    List<Folder> findAllById(String id);
}
