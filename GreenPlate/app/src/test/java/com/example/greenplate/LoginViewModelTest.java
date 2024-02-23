import org.junit.Test;
import static org.junit.Assert.*;

public class LoginViewModelTest {

    @Test
    public void testValidUsernameAndPassword() {
        assertTrue(LoginViewModel.getInstance().isValidUsernameOrPassword("user123", "password123"));
    }

    @Test
    public void testNullInputs() {
        assertFalse(LoginViewModel.getInstance().isValidUsernameOrPassword(null, null));
    }

    @Test
    public void testEmptyInputs() {
        assertFalse(LoginViewModel.getInstance().isValidUsernameOrPassword("", ""));
    }

    @Test
    public void testWhitespaceOnlyInputs() {
        assertFalse(LoginViewModel.getInstance().isValidUsernameOrPassword("   ", "   "));
    }
}
