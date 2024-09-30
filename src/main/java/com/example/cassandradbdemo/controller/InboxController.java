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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.cassandradbdemo.constant.ModelConstant.*;

@Controller
public class InboxController {

    private FolderRepository folderRepository;
    private EmailListItemRepository emailListItemRepository;
    private FolderService folderService;

    public InboxController(FolderRepository folderRepository, EmailListItemRepository emailListItemRepository, FolderService folderService) {
        this.folderRepository = folderRepository;
        this.emailListItemRepository = emailListItemRepository;
        this.folderService = folderService;
    }


    @GetMapping(value = "/folders")
    public String getHomePage(
            @AuthenticationPrincipal OidcUser principal,
            @RequestParam(required = false) String folder,
            Model model) {
        String userId = principal.getEmail();
        List<Folder> folderList = folderRepository.findAllById(userId); // Folders created by the user -> endpoint not created yet
        model.addAttribute(USER_FOLDERS_ATTRIBUTE, folderList);


        List<Folder> defaultfolderList = folderService.fetchDefaultUserFolders(userId); // default folders for all users
        model.addAttribute(DEFAULT_FOLDERS_ATTRIBUTE, defaultfolderList);

        model.addAttribute(STATS_ATTRIBUTE, folderService.getEmailStats(userId));

        if (!StringUtils.hasText(folder)) {
            folder = INBOX_FOLDER;
        }

        List<EmailListItem> emailList = emailListItemRepository.findAllById_UserIdAndId_Label(userId, folder);

        PrettyTime p = new PrettyTime();

        emailList.stream().forEach(emailItem -> {
            UUID timeuuid = emailItem.getId().getTimeId();
            Date date = new Date(Uuids.unixTimestamp(timeuuid));
            emailItem.setAgoTimeString(p.format(date));
        });

        model.addAttribute(EMAIL_LIST_ATTRIBUTE, emailList);
        model.addAttribute(FOLDER_NAME_ATTRIBUTE, folder);
        return INBOX_MODEL;
    }


    @GetMapping(value = "/folders/{id}")
    public String getFolder(
            @AuthenticationPrincipal OidcUser principal,
            @PathVariable String id,
            Model model) {
        String userId = principal.getEmail();
        Optional<Folder> optionalFolder = folderRepository.findById(id);

        if (optionalFolder.isPresent()) {
            Folder folder = optionalFolder.get();

            String folderLabel = folder.getLabel();
            List<EmailListItem> emailList = emailListItemRepository.findAllById_UserIdAndId_Label(userId, folderLabel);

            PrettyTime p = new PrettyTime();

            emailList.stream().forEach(emailItem -> {
                UUID timeuuid = emailItem.getId().getTimeId();
                Date date = new Date(Uuids.unixTimestamp(timeuuid));
                emailItem.setAgoTimeString(p.format(date));
            });

            model.addAttribute(EMAIL_LIST_ATTRIBUTE, emailList);
        }
        return INBOX_MODEL;
    }


    @PostConstruct
    public void init() {

        List<Folder> folders = InitFolders.init("randomUserId");
        folderRepository.saveAll(folders);
    }
}
