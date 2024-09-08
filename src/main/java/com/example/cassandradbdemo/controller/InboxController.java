package com.example.cassandradbdemo.controller;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.cassandradbdemo.models.EmailListItem;
import com.example.cassandradbdemo.models.Folder;
import com.example.cassandradbdemo.dataSeed.InitFolders;
import com.example.cassandradbdemo.models.UnreadEmailStats;
import com.example.cassandradbdemo.repos.EmailListItemRepository;
import com.example.cassandradbdemo.repos.FolderRepository;
import com.example.cassandradbdemo.repos.UnreadEmailStatsRepository;
import com.example.cassandradbdemo.services.FolderService;
import jakarta.annotation.PostConstruct;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class InboxController {

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    private EmailListItemRepository emailListItemRepository;


    @Autowired
    FolderService folderService;


    @GetMapping(value = "/folders")
    public String getHomePage(@RequestParam(required = false) String folder, Model model) {
        String userId = "randomUserId";// will be replaced by the signed-in user

        List<Folder> folderList = folderRepository.findAllById(userId); // Folders created by the user -> endpoint not created yet
        model.addAttribute("userFolders", folderList);


        List<Folder> defaultfolderList = folderService.fetchDefaultUserFolders(userId); // default folders for all users
        model.addAttribute("defaultFolders", defaultfolderList);

        model.addAttribute("stats", folderService.getEmailStats(userId));

        if (!StringUtils.hasText(folder)) {
            folder = "Inbox";
        }

        List<EmailListItem> emailList = emailListItemRepository.findAllById_UserIdAndId_Label(userId, folder);

        PrettyTime p = new PrettyTime();

        emailList.stream().forEach(emailItem -> {
            UUID timeuuid = emailItem.getId().getTimeId();
            Date date = new Date(Uuids.unixTimestamp(timeuuid));
            emailItem.setAgoTimeString(p.format(date));
        });

        model.addAttribute("emailList", emailList);
        model.addAttribute("folderName", folder);
        return "inbox-page";
    }


    @GetMapping(value = "/folders/{id}")
    public String getFolder(@PathVariable String id, Model model) {

        Optional<Folder> optionalFolder = folderRepository.findById(id);

        if (optionalFolder.isPresent()) {
            Folder folder = optionalFolder.get();

            String folderLabel = folder.getLabel();
            List<EmailListItem> emailList = emailListItemRepository.findAllById_UserIdAndId_Label("randomUserId", folderLabel);

            PrettyTime p = new PrettyTime();

            emailList.stream().forEach(emailItem -> {
                UUID timeuuid = emailItem.getId().getTimeId();
                Date date = new Date(Uuids.unixTimestamp(timeuuid));
                emailItem.setAgoTimeString(p.format(date));
            });

            model.addAttribute("emailList", emailList);
        }
        return "inbox-page";
    }


    @PostConstruct
    public void init() {
        List<Folder> folders = InitFolders.init("randomUserId");
        folderRepository.saveAll(folders);
    }
}
