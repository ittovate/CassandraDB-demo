package com.example.cassandradbdemo.controller;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.cassandradbdemo.models.Email;
import com.example.cassandradbdemo.models.EmailListItem;
import com.example.cassandradbdemo.models.Folder;
import com.example.cassandradbdemo.repos.EmailListItemRepository;
import com.example.cassandradbdemo.repos.EmailRepository;
import com.example.cassandradbdemo.repos.FolderRepository;
import com.example.cassandradbdemo.services.FolderService;
import jakarta.annotation.PostConstruct;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EmailController {

    @Autowired
    private EmailRepository emailRepository;


    @Autowired
    FolderRepository folderRepository;

    @Autowired
    FolderService folderService;
    @GetMapping(value = "/folders/message/{id}")
    public String getEmail(@PathVariable UUID id, Model model){
        Optional<Email> email = emailRepository.findById(id);

        PrettyTime p = new PrettyTime();

        if(email.isPresent()){
            Email presentEmail = email.get();
            presentEmail.setToIds(String.join(", " , presentEmail.getTo()));
            UUID timeuuid = presentEmail.getId();
            Date date= new Date(Uuids.unixTimestamp(timeuuid));
            presentEmail.setSentTime(p.format(date));
            model.addAttribute("email" , presentEmail);
        }

        List<Folder> folderList = folderRepository.findAllById("randomUserId");
        model.addAttribute("userFolders" , folderList);


        List<Folder> defaultfolderList = folderService.fetchDefaultUserFolders("randomUserId");
        model.addAttribute("defaultFolders" , defaultfolderList);


        return "inbox-page";
    }


}
