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

import static com.example.cassandradbdemo.constant.ModelConstant.*;

@Service
public class FolderService {

    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    public List<Folder> fetchDefaultUserFolders(String userId) {
        return Arrays.asList(
                new Folder(userId, INBOX_LABEL, BLUE_COLOR),
                new Folder(userId, SENT_LABEL, PURPLE_COLOR),
                new Folder(userId, IMPORTANT_LABEL, RED_COLOR),
                new Folder(userId, DONE_LABEL, GREEN_COLOR)
        );
    }

    public Map<String, Integer> getEmailStats(String userId) {
        List<UnreadEmailStats> stats = unreadEmailStatsRepository.findAllById(userId);   // unread email stats for a user folders
        return stats.stream().collect(Collectors.toMap(UnreadEmailStats::getLabel, UnreadEmailStats::getUnreadCount));
    }
}
