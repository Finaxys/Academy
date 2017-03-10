
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import com.finaxys.slackbot.BUL.Classes.SCORE_GRID;
import com.finaxys.slackbot.BUL.Listeners.ReactionAddedListener;
import com.finaxys.slackbot.Configuration.Classes.SpringContext;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Utilities.SlackBot;
import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringContext.class })
@TransactionConfiguration(defaultRollback = true, transactionManager = "transactionManager")
public class ReactionAddedListenerTest {
	@Autowired
	Repository<FinaxysProfile, String> myGenericRepo1;

	@Test
	public void messageAppreciatedListenerTest() {
		try (AbstractApplicationContext context = new AnnotationConfigApplicationContext(SpringContext.class)) {
			ReactionAddedListener reactionAddedListener = (ReactionAddedListener) context
					.getBean("reactionAddedListener");
			SlackRealTimeMessagingClient slackRealTimeMessagingClient = SlackBot
					.getSlackRealTimeMessagingClient();
			SlackWebApiClient slackWebApiClient = SlackBot.getSlackWebApiClient();
			slackRealTimeMessagingClient.addListener(Event.REACTION_ADDED, reactionAddedListener);
			slackRealTimeMessagingClient.connect();
			FinaxysProfile finaxysProfile ;
			finaxysProfile = myGenericRepo1.findById("U4BH7ADRB");
			if(finaxysProfile == null)
			{
				finaxysProfile = myGenericRepo1.addEntity(new FinaxysProfile("U4BH7ADRB",false,0));
			}
			int previousScore = finaxysProfile.getScore();
			slackWebApiClient.addReactionToMessage("clap", "C4AK74EKS", "1488983139.152140");
			finaxysProfile.incrementScore(SCORE_GRID.APPRECIATED_MESSAGE.value());
			myGenericRepo1.updateEntity(finaxysProfile);
			FinaxysProfile newFinaxysProfileUpdated = myGenericRepo1.findById("U4BH7ADRB");
			assertEquals("Score updated successfully", previousScore+SCORE_GRID.APPRECIATED_MESSAGE.value(),
					newFinaxysProfileUpdated.getScore());
		}

	}
}

