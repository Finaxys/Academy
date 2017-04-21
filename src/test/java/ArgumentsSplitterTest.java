import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.finaxys.slackbot.Utilities.ArgumentsSplitter;

public class ArgumentsSplitterTest {

	ArgumentsSplitter splitter;
	
	@Test
	@Ignore
	public void fx_help_right_Test() {
		
		String arguments = "";
		String command = "/fx_help";
		
		splitter = new ArgumentsSplitter(arguments, command);
		
		assertEquals(true, splitter.verifier.Verify(arguments, command));
	}

	@Test
	@Ignore
	public void fx_event_score_add_right_Test() {
		
		String arguments = "<@Utruc|machin> 30 ThatEventRightThereYouIdiot";
		String command = "/fx_event_score_add";
		
		splitter = new ArgumentsSplitter(arguments, command);
		
		assertEquals("Utruc", splitter.getUserId());
		assertEquals("machin", splitter.getUserName());
		assertEquals("30", splitter.getIntegers(0));
		assertEquals("ThatEventRightThereYouIdiot", splitter.getString(0));
	}
	
	@Test
	@Ignore
	public void fx_event_add_right_Test() {
		
		String arguments = "ANewEventDeMierda individual \"A Description De Mierda\"";
		String command = "/fx_event_add";
		
		splitter = new ArgumentsSplitter(arguments, command);
		
		assertEquals("ANewEventDeMierda", splitter.getString(0));
		assertEquals("individual", splitter.getEventType());
		assertEquals("A Description De Mierda", splitter.getString(1));
	}
	
	@Test
	@Ignore
	public void fx_event_add_wrong_Test() {
		
		String arguments = "<@Utruc|machin> individual \"A Description De Mierda\"";
		String command = "/fx_event_add";
		
		splitter = new ArgumentsSplitter(arguments, command);
		
		assertEquals(false, splitter.verifier.Verify(arguments, command));
	}
	
	@Test
	@Ignore
	public void fx_manager_add_right_Test() {
		
		String arguments = "eventName <@U4ULGNAAE|arnaudb>";
		String command = "/fx_manager_add";
		
		splitter = new ArgumentsSplitter(arguments, command);
		
		String profileId   = splitter.getUserId();
		String profileName = splitter.getUserName();
		String eventName   = splitter.getString(0);
		
		assertEquals(true, splitter.verifier.Verify(arguments, command));
		assertEquals("U4ULGNAAE", profileId);
		assertEquals("arnaudb", profileName);
		assertEquals("eventName", eventName);
	}
	
	@Test
	public void fx_event_action_add_Test() {
		String arguments = "OUI 3";
		String command = "/fx_event_action_add";
		
		splitter = new ArgumentsSplitter(arguments, command);
		
		String code   = splitter.getIntegers(0);
		String event = splitter.getString(0);
		
		assertEquals(true, splitter.verifier.Verify(arguments, command));
		assertEquals("OUI", event);
		assertEquals("3", code);
	}
	
}
