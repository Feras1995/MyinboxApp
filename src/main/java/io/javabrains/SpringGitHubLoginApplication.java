package io.javabrains;

import java.nio.file.Path;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.emailslist.EmailListItemRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.UnreadEmailStatusRepository;

@SpringBootApplication
@RestController
public class SpringGitHubLoginApplication {
	@Autowired
	FolderRepository folderRepository;
	@Autowired
	EmailListItemRepository emailsListRepository;
	@Autowired
	EmailRepository emailRepository;

	@Autowired
	UnreadEmailStatusRepository unreadEmailStatusRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringGitHubLoginApplication.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init() {
		folderRepository.save(new Folder("Feras1995", "Inbox", "blue"));
		folderRepository.save(new Folder("Feras1995", "sent", "green"));
		folderRepository.save(new Folder("Feras1995", "important", "yellow"));
		unreadEmailStatusRepository.incrementUreadCount("Feras1995", "Inbox");
		unreadEmailStatusRepository.incrementUreadCount("Feras1995", "Inbox");
		unreadEmailStatusRepository.incrementUreadCount("Feras1995", "Inbox");

		// for (int i = 0; i <10; i++) {
		// EmailsListPrimaryKey key = new EmailsListPrimaryKey();
		// key.setUserId("Feras1995");
		// key.setLabel("Inbox");
		// key.setTimeId(Uuids.timeBased());
		// EmailListItem emailsListEntry = new EmailListItem();
		// emailsListEntry.setId(key);
		// emailsListEntry.setSubject("Hello " + i);
		// emailsListEntry.setUnread(true);
		// emailsListEntry.setTo(Arrays.asList("Feras1995","abc","efj"));
		// emailsListRepository.save(emailsListEntry);

		// Email email=new Email();
		// email.setId(key.getTimeId());
		// email.setFrom("Feras1995");
		// email.setSubject(emailsListEntry.getSubject());
		// email.setBody("body"+i);
		// email.setTo(emailsListEntry.getTo());

		// emailRepository.save(email);
		// }

	}

}
