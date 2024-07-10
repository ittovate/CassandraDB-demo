package com.example.cassandradbdemo.repos;


import com.example.cassandradbdemo.models.EmailListItem;
import com.example.cassandradbdemo.models.EmailListItemKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailListItemRepository  extends CassandraRepository<EmailListItem, EmailListItemKey> {

    // This is because the primary key is composed and will need to be looked up in the primary key field on the EmailListKey
    List<EmailListItem> findAllById_IdAndId_label(String id, String label);
}
