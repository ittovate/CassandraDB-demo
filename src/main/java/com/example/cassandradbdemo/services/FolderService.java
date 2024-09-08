package com.example.cassandradbdemo.services;


import com.example.cassandradbdemo.models.Folder;
import com.example.cassandradbdemo.models.UnreadEmailStats;
import com.example.cassandradbdemo.repos.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FolderService {

    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    public List<Folder> fetchDefaultUserFolders(String userId) {
        return Arrays.asList(
                new Folder(userId, "Inbox", "blue"),
                new Folder(userId, "Sent", "purple"),
                new Folder(userId, "Important", "red"),
                new Folder(userId, "Done", "green")
        );
    }

    public Map<String, Integer> getEmailStats(String userId) {
        List<UnreadEmailStats> stats = unreadEmailStatsRepository.findAllById(userId);   // unread email stats for a user folders
        return stats.stream().collect(Collectors.toMap(UnreadEmailStats::getLabel, UnreadEmailStats::getUnreadCount));
    }
}
