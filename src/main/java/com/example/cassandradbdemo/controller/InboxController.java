package com.example.cassandradbdemo.controller;


import com.example.cassandradbdemo.models.Folder;
import com.example.cassandradbdemo.dataSeed.InitFolders;
import com.example.cassandradbdemo.repos.FolderRepository;
import com.example.cassandradbdemo.services.FolderService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InboxController {

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    FolderService folderService;

    @GetMapping(value = "/")
    public String getHomePage(Model model){
        List<Folder> folderList = folderRepository.findAllById("randomUserId");
        model.addAttribute("userFolders" , folderList);


        List<Folder> defaultfolderList = folderService.fetchDefaultUserFolders("randomUserId");
        model.addAttribute("defaultFolders" , defaultfolderList);

        return "inbox-page";
    }


    @PostConstruct
    public void init(){
        List<Folder> folders = InitFolders.init("randomUserId");
        folderRepository.saveAll(folders);
    }
}
