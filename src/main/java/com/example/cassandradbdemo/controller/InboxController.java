package com.example.cassandradbdemo.controller;


import com.example.cassandradbdemo.models.Folder;
import com.example.cassandradbdemo.models.InitFolders;
import com.example.cassandradbdemo.repos.FolderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class InboxController {

    @Autowired
    FolderRepository folderRepository;

    @GetMapping(value = "/")
    public String getHomePage(Model model){
        List<Folder> folderList = folderRepository.findAllById("randomUserId");
        model.addAttribute("folders" , folderList);
        for (int i = 0 ; i < folderList.size(); i ++ ){
            System.out.println("folder wth i " + folderList.get(i).getId());
        }
        return "inbox-page";
    }


    @PostConstruct
    public void init(){
        List<Folder> folders = InitFolders.init("randomUserId");
        folderRepository.saveAll(folders);
    }
}
