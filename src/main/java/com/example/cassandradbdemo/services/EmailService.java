package com.example.cassandradbdemo.services;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.cassandradbdemo.models.Email;
import com.example.cassandradbdemo.models.EmailListItem;
import com.example.cassandradbdemo.models.EmailListItemKey;
import com.example.cassandradbdemo.repos.EmailListItemRepository;
import com.example.cassandradbdemo.repos.EmailRepository;
import com.example.cassandradbdemo.repos.UnreadEmailStatsRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.example.cassandradbdemo.constant.ModelConstant.INBOX_LABEL;
import static com.example.cassandradbdemo.constant.ModelConstant.SENT_LABEL;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final EmailListItemRepository emailListItemRepository;
    private final UnreadEmailStatsRepository unreadEmailStatsRepository;

    public EmailService(
            EmailRepository emailRepository,
            EmailListItemRepository emailListItemRepository,
            UnreadEmailStatsRepository unreadEmailStatsRepository
    ) {
        this.emailRepository = emailRepository;
        this.emailListItemRepository = emailListItemRepository;
        this.unreadEmailStatsRepository = unreadEmailStatsRepository;
    }

    public void sendEmail(String from, List<String> to, String subject, String body) {
        Email email = new Email();
        email.setFrom(from);
        email.setTo(to);
        email.setSubject(subject);
        email.setBody(body);
        email.setId(Uuids.timeBased());

        emailRepository.save(email);

        to.forEach(recepient -> {
            EmailListItem item = createEmailListItem(to, subject, email, recepient, INBOX_LABEL);
            emailListItemRepository.save(item);
            unreadEmailStatsRepository.incrementUnreadCount(recepient, INBOX_LABEL);
        });

        EmailListItem sentItemEntry = createEmailListItem(to, subject, email, from, SENT_LABEL);
        sentItemEntry.setUnRead(false);
        emailListItemRepository.save(sentItemEntry);

    }

    @NotNull
    private static EmailListItem createEmailListItem(List<String> to, String subject, Email email, String itemOwner, String folder) {
        EmailListItemKey key = new EmailListItemKey();
        key.setUserId(itemOwner);
        key.setTimeId(email.getId());
        key.setLabel(folder);


        EmailListItem item = new EmailListItem();
        item.setId(key);
        item.setSubject(subject);
        item.setUnRead(true);
        item.setTo(to);

        return item;
    }
}
