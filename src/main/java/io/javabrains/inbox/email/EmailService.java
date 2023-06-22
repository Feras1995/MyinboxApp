package io.javabrains.inbox.email;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.javabrains.inbox.emailslist.EmailListItem;
import io.javabrains.inbox.emailslist.EmailListItemRepository;
import io.javabrains.inbox.emailslist.EmailsListPrimaryKey;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailListItemRepository emailListItemRepository;

    public void SendEmail(String from, List<String> to, String subject, String body) {

        Email email = new Email();
        email.setTo(to);
        email.setBody(body);
        email.setFrom(from);
        email.setSubject(subject);
        email.setId(Uuids.timeBased());
        emailRepository.save(email);

        to.forEach(toId -> {

          EmailListItem item=createEmailListItem(to, subject, email, toId ,"Inbox");
            emailListItemRepository.save(item);

        });


       EmailListItem sentItemsEntry= createEmailListItem(to, subject, email, from ,"Sent Items");
       emailListItemRepository.save(sentItemsEntry);
    }



    private   EmailListItem createEmailListItem(List<String> to,String subject,Email email,String itemOwner,String folder){
          EmailsListPrimaryKey key = new EmailsListPrimaryKey();
            key.setUserId(itemOwner);
            key.setTimeId(email.getId());
            key.setLabel(folder);

            EmailListItem item = new EmailListItem();
            item.setId(key);
            item.setSubject(subject);
            item.setUnread(true);

            return item;
    }
}
