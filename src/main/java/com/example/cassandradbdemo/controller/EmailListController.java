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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

import static com.example.cassandradbdemo.constant.ModelConstant.*;

@Controller
public class EmailListController {
    private EmailListItemRepository emailListItemRepository;
    private FolderService folderService;
    public EmailListController(EmailListItemRepository emailListItemRepository, FolderService folderService) {
        this.emailListItemRepository = emailListItemRepository;
        this.folderService = folderService;
    }

    @GetMapping(value = "/messages")
    public String getMessages(
            @AuthenticationPrincipal OidcUser principal,
            Model model) {

        String userId = principal.getEmail();
        String folderLabel = INBOX_LABEL;

        List<EmailListItem> emailList = emailListItemRepository.findAllById_UserIdAndId_Label(userId, folderLabel);
        model.addAttribute(EMAIL_LIST_ATTRIBUTE, emailList);
        model.addAttribute(STATS_ATTRIBUTE, folderService.getEmailStats(userId));


        return INBOX_MODEL;
    }
}
