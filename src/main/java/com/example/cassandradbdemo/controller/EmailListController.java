package com.example.cassandradbdemo.controller;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.cassandradbdemo.models.Email;
import com.example.cassandradbdemo.models.EmailListItem;
import com.example.cassandradbdemo.models.EmailListItemKey;
import com.example.cassandradbdemo.repos.EmailListItemRepository;
import com.example.cassandradbdemo.repos.EmailRepository;
import com.example.cassandradbdemo.repos.UnreadEmailStatsRepository;
import com.example.cassandradbdemo.services.EmailService;
import com.example.cassandradbdemo.services.FolderService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class EmailListController {
    @Autowired
    private EmailListItemRepository emailListItemRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    FolderService folderService;
    private String[] labels = {"Inbox", "Sent", "Important", "Done"};

    private Random rand = new Random();


    @GetMapping(value = "/messages")
    public String getMessages(Model model) {
        String folderLabel = "Inbox";
        String userId = "randomUserId";// will be replaced by the signed-in user

        List<EmailListItem> emailList = emailListItemRepository.findAllById_UserIdAndId_Label(userId, folderLabel);
        model.addAttribute("emailList", emailList);
        model.addAttribute("stats", folderService.getEmailStats(userId));


        return "inbox-page";
    }

    @PostConstruct
    public List<EmailListItem> generateListOfEmails() {
        ArrayList<EmailListItem> emailList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            emailService.sendEmail("randomUserId", Arrays.asList("randomUserId2", "randomUserId", "asdfasdf"), "subject" + i, "subject" + i);

        }
        return emailList;
    }
}
