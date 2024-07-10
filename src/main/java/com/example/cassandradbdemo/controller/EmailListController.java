package com.example.cassandradbdemo.controller;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.cassandradbdemo.models.EmailListItem;
import com.example.cassandradbdemo.models.EmailListItemKey;
import com.example.cassandradbdemo.repos.EmailListItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class EmailListController {
    @Autowired
    private EmailListItemRepository emailListItemRepository;


    @PostConstruct
    public List<EmailListItem> generateListOfEmails() {
        ArrayList<EmailListItem> emailList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            EmailListItemKey key = new EmailListItemKey();
            key.setId("randomUserId");
            key.setLabel("Inbox");
            key.setTimeUUID(Uuids.timeBased());

            EmailListItem emailListItem = new EmailListItem();
            emailListItem.setId(key);
            emailListItem.setSubject("subject" + i);
            emailListItem.setUnRead(true);
            emailListItem.setTo(Arrays.asList("randomUserId"));

            emailListItemRepository.save(emailListItem);
        }
        return emailList;
    }
}
