package com.example.cassandradbdemo.dataSeed;

import com.example.cassandradbdemo.models.Folder;

import java.util.Arrays;
import java.util.List;

public class InitFolders {
    public static List<Folder> init(String userId) {
        return Arrays.asList(
                new Folder(userId, "Inbox", "blue"),
                new Folder(userId, "Sent", "purple"),
                new Folder(userId, "Important", "red"),
                new Folder(userId, "Done", "green")
        );
    }

}
