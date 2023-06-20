package io.javabrains.inbox.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.javabrains.inbox.emailslist.EmailListItem;
import io.javabrains.inbox.emailslist.EmailListItemRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.FoldersService;

@Controller
public class InboxController {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FoldersService folderService;
    @Autowired
    private EmailListItemRepository emailListItemRepository;

    @GetMapping(value = "/")
    public String homePage(
            @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }
        // fetch folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = folderService.fetchDefaultFoders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        // fetch messages

        String folderLabel = "Inbox";
        List<EmailListItem> emailList = emailListItemRepository.findAllById_UserIdAndId_Label(userId, folderLabel);

        PrettyTime p = new PrettyTime();


        emailList.stream().forEach(emailItem-> {
            UUID timeUuid= emailItem.getId().getTimeId();
            Date emailDateTime=new Date(Uuids.unixTimestamp(timeUuid));
            emailItem.setAgoTimeString( p.format(emailDateTime));
        });

        model.addAttribute("emailList", emailList);

        return "inbox-page";

    }
}