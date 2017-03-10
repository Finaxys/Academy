import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.finaxys.slackbot.BUL.Classes.SCORE_GRID;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Utilities.WebApiFactory;

import allbegray.slack.type.User;
import allbegray.slack.webapi.SlackWebApiClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class InnovateRewardTest {
	@Autowired
	private Repository<FinaxysProfile, String> finaxysProfileRepository;

	@Autowired
	InnovateService innovateService;
	

	@Test
	public void testFileAdded() {
		SlackWebApiClient webApiClient = WebApiFactory.getSlackWebApiClient();
		// Arrange
		webApiClient.createChannel("tribu-*");
		User u = new User();
		u.setId("U4B8LQPU3");
		int preivousScore=finaxysProfileRepository.findById(u.getId()).getScore();
		innovateService.addInnovateScore(u);
		int resultat=finaxysProfileRepository.findById(u.getId()).getScore();

		// Act
		// final int resultat = innovateService.addInnovateScore(u);

		// Assert
		 //assertEquals("test",preivousScore+SCORE_GRID.WAS_INNOVATIVE.value(),resultat);
	}
}