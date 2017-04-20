import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.finaxys.slackbot.Utilities.ArgumentsVerifier;

public class ArgumentsVerifierTest {
	
	ArgumentsVerifier verifier;

	@Test
	public void fx_help_right_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "";
		String command = "/fx_help";
		
		assertEquals(true, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_help_wrong_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "userName";
		String command = "/fx_help";
		
		assertEquals(false, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_event_add_right_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "NewEvent group \"Une petite description\"";
		String command = "/fx_event_add";
		
		assertEquals(true, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_event_add_wrong_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "NewEvent <@U455265|julien> \"Une petite description\"";
		String command = "/fx_event_add";
		
		assertEquals(false, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_event_list_right_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "";
		String command = "/fx_event_list";
		
		assertEquals(true, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_event_list_wrong_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "NewEvent <@U455265|julien> \"Une petite description\"";
		String command = "/fx_event_list";
		
		assertEquals(false, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_manager_add_right_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "ThisEvent <@Utruc|machin>";
		String command = "/fx_manager_add";
		
		assertEquals(true, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_manager_add_wrong_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "";
		String command = "/fx_manager_add";
		
		assertEquals(false, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_manager_list_right_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "ThisEvent";
		String command = "/fx_manager_list";
		
		assertEquals(true, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_manager_list_wrong_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "ThisEvent <@Utruc|machin>";
		String command = "/fx_manager_list";
		
		assertEquals(false, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fxadmin_list_right_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "";
		String command = "/fxadmin_list";
		
		assertEquals(true, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fxadmin_list_wrong_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "ThisEvent <@Utruc|machin>";
		String command = "/fxadmin_list";
		
		assertEquals(false, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_event_score_add_right_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "<@Utruc|machin> 30 ThatEventRightThereYouIdiot";
		String command = "/fx_event_score_add";
		
		assertEquals(true, verifier.Verify(arguments, command));
	}
	
	@Test
	public void fx_event_score_add_wrong_Test() {
		verifier = new ArgumentsVerifier();
		
		String arguments = "";
		String command = "/fx_event_score_add";
		
		assertEquals(false, verifier.Verify(arguments, command));
	}
}
