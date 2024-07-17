package com.example.cassandradbdemo.controller;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.cassandradbdemo.models.EmailListItem;
import com.example.cassandradbdemo.models.EmailListItemKey;
import com.example.cassandradbdemo.repos.EmailListItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class EmailListController {
    @Autowired
    private EmailListItemRepository emailListItemRepository;


    @GetMapping(value = "/messages")
    public String getMessages(Model model){
        String folderLabel = "Inbox";
        List<EmailListItem> emailList = emailListItemRepository.findAllById_UserIdAndId_Label("randomUserId", folderLabel);
        model.addAttribute("emailList" , emailList);

        return "inbox-page";
    }

//    @PostConstruct
//    public List<EmailListItem> generateListOfEmails() {
//        ArrayList<EmailListItem> emailList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            EmailListItemKey key = new EmailListItemKey();
//            key.setUserId("randomUserId");
//            key.setLabel("Inbox");
//            key.setTimeId(Uuids.timeBased());
//
//            EmailListItem emailListItem = new EmailListItem();
//            emailListItem.setId(key);
//            emailListItem.setSubject("subject" + i);
//            emailListItem.setUnRead(true);
//            emailListItem.setTo(Arrays.asList("randomUserId2"));
//
//            emailListItemRepository.save(emailListItem);
//        }
//        return emailList;
//    }
}
