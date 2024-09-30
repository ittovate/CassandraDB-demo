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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.cassandradbdemo.constant.ModelConstant.*;

@Controller
public class EmailController {
    private final EmailRepository emailRepository;
    private final FolderRepository folderRepository;
    private final FolderService folderService;
    private final EmailListItemRepository emailListItemRepository;
    private final UnreadEmailStatsRepository unreadEmailStatsRepository;

    public EmailController(
            EmailRepository emailRepository,
            FolderRepository folderRepository,
            FolderService folderService,
            EmailListItemRepository emailListItemRepository,
            UnreadEmailStatsRepository unreadEmailStatsRepository
    ) {
        this.emailRepository = emailRepository;
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.emailListItemRepository = emailListItemRepository;
        this.unreadEmailStatsRepository = unreadEmailStatsRepository;
    }

    @GetMapping(value = "/folders/message/{id}")
    public String getEmail(
            @PathVariable UUID id,
            @RequestParam String folder,
            @AuthenticationPrincipal OidcUser principal,
            Model model) {
        Optional<Email> email = emailRepository.findById(id);

        PrettyTime p = new PrettyTime();
        String userId = principal.getEmail();

        if (!email.isPresent()) {
            return INBOX_MODEL;
        }
        Email presentEmail = email.get();
        presentEmail.setToIds(String.join(", ", presentEmail.getTo()));
        UUID timeuuid = presentEmail.getId();
        Date date = new Date(Uuids.unixTimestamp(timeuuid));
        presentEmail.setSentTime(p.format(date));
        model.addAttribute(EMAIL_ATTRIBUTE, presentEmail);


        List<Folder> folderList = folderRepository.findAllById(userId);
        model.addAttribute(USER_FOLDERS_ATTRIBUTE, folderList);


        List<Folder> defaultfolderList = folderService.fetchDefaultUserFolders(userId);
        model.addAttribute(DEFAULT_FOLDERS_ATTRIBUTE, defaultfolderList);


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

        model.addAttribute(STATS_ATTRIBUTE, folderService.getEmailStats(userId));

        return INBOX_MODEL;
    }


}
