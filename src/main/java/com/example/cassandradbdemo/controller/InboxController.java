package com.example.cassandradbdemo.controller;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.cassandradbdemo.models.EmailListItem;
import com.example.cassandradbdemo.models.Folder;
import com.example.cassandradbdemo.dataSeed.InitFolders;
import com.example.cassandradbdemo.repos.EmailListItemRepository;
import com.example.cassandradbdemo.repos.FolderRepository;
import com.example.cassandradbdemo.services.FolderService;
import jakarta.annotation.PostConstruct;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class InboxController {

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    private EmailListItemRepository emailListItemRepository;


    @Autowired
    FolderService folderService;

    @GetMapping(value = "/folders")
      public String getHomePage(Model model){
        List<Folder> folderList = folderRepository.findAllById("randomUserId");
        model.addAttribute("userFolders" , folderList);


        List<Folder> defaultfolderList = folderService.fetchDefaultUserFolders("randomUserId");
        model.addAttribute("defaultFolders" , defaultfolderList);


        String folderLabel = "Inbox";
        List<EmailListItem> emailList = emailListItemRepository.findAllById_UserIdAndId_Label("randomUserId", folderLabel);

        PrettyTime p = new PrettyTime();

        emailList.stream().forEach(emailItem -> {
            UUID timeuuid = emailItem.getId().getTimeId();
            Date date= new Date(Uuids.unixTimestamp(timeuuid));
            emailItem.setAgoTimeString(p.format(date));
        });

        model.addAttribute("emailList" , emailList);
        return "inbox-page";
    }


    @PostConstruct
    public void init(){
        List<Folder> folders = InitFolders.init("randomUserId");
        folderRepository.saveAll(folders);
    }
}
