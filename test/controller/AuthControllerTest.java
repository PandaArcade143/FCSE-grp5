package controller;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import control.AuthController;
import entity.Applicant;
import entity.HDBManager;
import entity.HDBOfficer;
import entity.User;

public class AuthControllerTest {
	 	private AuthController authController;

	    @Before
	    public void setUp() {
	        authController = new AuthController();
	    }

	    @Test
	    public void testValidUserLogin() {
	    	// Check applicant
	        User user = authController.login("S1234567A", "password");
	        assertNotNull(user);
	        assertEquals("John", user.getName());
	        assertTrue(user instanceof Applicant);
	        
	        // Check officer
	        user = authController.login("T2109876H", "password");
	        assertNotNull(user);
	        assertEquals("Daniel", user.getName());
	        assertTrue(user instanceof HDBOfficer);
	        
	        // Check manager
	        user = authController.login("T8765432F", "password");
	        assertNotNull(user);
	        assertEquals("Michael", user.getName());
	        assertTrue(user instanceof HDBManager);
	    }

	    @Test
	    public void testInvalidNricFormat() {
	        String invalidNric = "1234567"; // Missing prefix
	        User user = authController.login(invalidNric, "password");
	        assertNull(user); // Should not find user with malformed NRIC
	    }

	    @Test
	    public void testIncorrectPassword() {
	        User user = authController.login("S1234567A", "wrongpass");
	        assertNull(user); // Login fails with wrong password
	    }

	    @Test
	    public void testPasswordChange() {
	        User user = authController.login("S1234567A", "password");
	        assertNotNull(user);

	        user.setPassword("newpass321");

	        // Old password should fail
	        assertNull(authController.login("S1234567A", "password"));

	        // New password should succeed
	        assertEquals(user, authController.login("S1234567A", "newpass321"));
	        
	        // Change back to old password
	        user.setPassword("password");
	    }

	

}
