package com.example.cassandradbdemo.controller;


import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.cassandradbdemo.models.Email;
import com.example.cassandradbdemo.models.Folder;
import com.example.cassandradbdemo.repos.EmailRepository;
import com.example.cassandradbdemo.repos.FolderRepository;
import com.example.cassandradbdemo.services.EmailService;
import com.example.cassandradbdemo.services.FolderService;
import org.jetbrains.annotations.NotNull;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.cassandradbdemo.constant.ModelConstant.*;

@Controller
public class ComposeController {
    private final FolderRepository folderRepository;
    private final EmailService emailService;
    private final FolderService folderService;

    public ComposeController(
            FolderRepository folderRepository,
            FolderService folderService,
            EmailService emailService
    ) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.emailService = emailService;
    }

    @GetMapping("/compose")
    public String getEmail(
            @AuthenticationPrincipal OidcUser principal,
            Model model, @RequestParam(required = false) String to) {

        String userId = principal.getEmail();
        List<Folder> folderList = folderRepository.findAllById(userId);
        model.addAttribute(USER_FOLDERS_ATTRIBUTE, folderList);


        List<Folder> defaultfolderList = folderService.fetchDefaultUserFolders(userId);
        model.addAttribute(DEFAULT_FOLDERS_ATTRIBUTE, defaultfolderList);

        List<String> ids = splitIds(to);

        model.addAttribute(TO_ATTRIBUTE, String.join(", ", ids));
        model.addAttribute(STATS_ATTRIBUTE, folderService.getEmailStats(userId));

        return COMPOSE_MODEL;
    }

    @NotNull
    private static List<String> splitIds(String to) {
        if (!StringUtils.hasText(to)) {
            return new ArrayList<>();
        }
        String[] toIds = to.split(",");
        return Arrays.stream(toIds)
                .map(StringUtils::trimWhitespace)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();
    }

    @PostMapping("/sendEmail")
    public ModelAndView sendEmail(
            @AuthenticationPrincipal OidcUser principal,
            @RequestBody MultiValueMap<String, String> formData

    ) {
        String from = principal.getEmail();
        List<String> toIds = splitIds(formData.getFirst(TO));
        String subject = formData.getFirst(SUBJECT);
        String body = formData.getFirst(BODY);

        emailService.sendEmail(from, toIds, subject, body);

        return new ModelAndView("redirect:/" + FOLDERS_MODEL);

    }


}
