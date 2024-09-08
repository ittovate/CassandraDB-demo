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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ComposeController {

    @Autowired
    private EmailRepository emailRepository;


    @Autowired
    FolderRepository folderRepository;

    @Autowired
    FolderService folderService;

    @Autowired
    EmailService emailService;

    @GetMapping(value = "/compose")
    public String getEmail(Model model, @RequestParam(required = false) String to) {
        String userId = "randomUserId";// will be replaced by the signed-in user

        List<Folder> folderList = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", folderList);


        List<Folder> defaultfolderList = folderService.fetchDefaultUserFolders(userId);
        model.addAttribute("defaultFolders", defaultfolderList);

        List<String> ids = splitIds(to);

        model.addAttribute("to", String.join(", ", ids));
        model.addAttribute("stats", folderService.getEmailStats(userId));

        return "/components/compose-page";
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
            @RequestBody MultiValueMap<String, String> formData
    ) {

        String from = "randomUserId";
        List<String> toIds = splitIds(formData.getFirst("to"));
        String subject = formData.getFirst("subject");
        String body = formData.getFirst("body");

        emailService.sendEmail(from, toIds, subject, body);

        return new ModelAndView("redirect:/folders");

    }


}
