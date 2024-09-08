package com.example.cassandradbdemo.controller;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.cassandradbdemo.models.Email;
import com.example.cassandradbdemo.models.EmailListItem;
import com.example.cassandradbdemo.models.EmailListItemKey;
import com.example.cassandradbdemo.models.Folder;
import com.example.cassandradbdemo.repos.EmailListItemRepository;
import com.example.cassandradbdemo.repos.EmailRepository;
import com.example.cassandradbdemo.repos.FolderRepository;
import com.example.cassandradbdemo.repos.UnreadEmailStatsRepository;
import com.example.cassandradbdemo.services.FolderService;
import jakarta.annotation.PostConstruct;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    EmailListItemRepository emailListItemRepository;

    @Autowired
    UnreadEmailStatsRepository unreadEmailStatsRepository;

    @GetMapping(value = "/folders/message/{id}")
    public String getEmail(
            @PathVariable UUID id,
            @RequestParam String folder,
            Model model) {
        Optional<Email> email = emailRepository.findById(id);

        PrettyTime p = new PrettyTime();
        String userId = "randomUserId";// will be replaced by the signed-in user

        if (!email.isPresent()) {
            return "inbox-page";
        }
        Email presentEmail = email.get();
        presentEmail.setToIds(String.join(", ", presentEmail.getTo()));
        UUID timeuuid = presentEmail.getId();
        Date date = new Date(Uuids.unixTimestamp(timeuuid));
        presentEmail.setSentTime(p.format(date));
        model.addAttribute("email", presentEmail);


        List<Folder> folderList = folderRepository.findAllById("randomUserId");
        model.addAttribute("userFolders", folderList);


        List<Folder> defaultfolderList = folderService.fetchDefaultUserFolders("randomUserId");
        model.addAttribute("defaultFolders", defaultfolderList);


        EmailListItemKey key = new EmailListItemKey();
        key.setLabel(folder);
        key.setUserId(userId);
        key.setTimeId(presentEmail.getId());

        Optional<EmailListItem> optionalEmailListItem = emailListItemRepository.findById(key);
        if (optionalEmailListItem.isPresent()) {
            EmailListItem emailListItem = optionalEmailListItem.get();
            if (emailListItem.isUnRead()) {
                emailListItem.setUnRead(false);
                emailListItemRepository.save(emailListItem);
                unreadEmailStatsRepository.decrementUnreadCount(userId, folder);
            }
        }

        model.addAttribute("stats", folderService.getEmailStats(userId));

        return "inbox-page";
    }


}
