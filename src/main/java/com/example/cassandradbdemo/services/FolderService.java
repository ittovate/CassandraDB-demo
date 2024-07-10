package com.example.cassandradbdemo.services;


import com.example.cassandradbdemo.models.Folder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FolderService {

    public List<Folder> fetchDefaultUserFolders(String userId){
        return Arrays.asList(
                new Folder(userId, "Inbox", "blue"),
                new Folder(userId, "Sent", "purple"),
                new Folder(userId, "Important", "red"),
                new Folder(userId, "Done", "green")
        );
    }

}
