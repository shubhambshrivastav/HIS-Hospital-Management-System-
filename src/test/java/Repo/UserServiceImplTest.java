package Repo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mfp.api.dao.UserDao;
import com.mfp.api.entity.User;
import com.mfp.api.exception.ResourceNotFoundException;
import com.mfp.api.exception.SomethingWentWrongException;
import com.mfp.api.serviceimpl.UserServiceImpl;
import com.mfp.api.utility.UserFieldChecker;
import com.mfp.api.validation.ValidateRole;
import com.mfp.api.validation.ValidateUser;

class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserDao userDao;
    private BCryptPasswordEncoder passwordEncoder;
    private UserFieldChecker userFieldChecker;
    private ValidateUser validateUser;
    private ValidateRole validateRole;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        userFieldChecker = mock(UserFieldChecker.class);
        validateUser = mock(ValidateUser.class);
        validateRole = mock(ValidateRole.class);

        userService = new UserServiceImpl();
        userService.setDao(userDao);
        userService.passwordEncoder = passwordEncoder;
        userService.setChecker(userFieldChecker);
        userService.setValidateUser(validateUser);
        userService.setValidateRole(validateRole);
    }

    @Test
    void testAddUser_Success() {
        User user = new User();
        when(userFieldChecker.userAlreadyExists(user)).thenReturn(false);
        when(validateUser.validateUser(user)).thenReturn(Collections.emptyMap());
        when(userFieldChecker.duplicateFields(user)).thenReturn(Collections.emptyMap());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userDao.addUser(user)).thenReturn(true);

        assertTrue(userService.addUser(user));
    }

    @Test
    void testAddUser_UserAlreadyExists() {
        User user = new User();
        when(userFieldChecker.userAlreadyExists(user)).thenReturn(true);

        SomethingWentWrongException exception = assertThrows(SomethingWentWrongException.class,
                () -> userService.addUser(user));

        assertEquals("Something Went Wrong\nUser Already Exist, UserName : " + user.getUsername(), exception.getMessage());
    }

    @Test
    void testAddUser_InvalidUser() {
        User user = new User();
        when(userFieldChecker.userAlreadyExists(user)).thenReturn(false);
        when(validateUser.validateUser(user)).thenReturn(Collections.singletonMap("field", "error"));

        SomethingWentWrongException exception = assertThrows(SomethingWentWrongException.class,
                () -> userService.addUser(user));

        assertEquals("{\"field\"=\"error\"}\nPlease Enter Valid Details", exception.getMessage());
    }


    @Test
    void testUpdateUser_Success() {
        String username = "john_doe";
        User user = new User();
        when(userDao.getUserByUserName(username)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userDao.updateUser(user)).thenReturn(user);

        User updatedUser = userService.updateUser(username, user);

        assertEquals(user, updatedUser);
        assertEquals("encodedPassword", updatedUser.getPassword());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        String username = "non_existing_user";
        when(userDao.getUserByUserName(username)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(username, new User()));

        assertEquals("NO USER FOUND FOR USERNAME : non_existing_user", exception.getMessage());
    }

    @Test
    void testGetUserByUserName_Success() {
        String username = "john_doe";
        User user = new User();
        when(userDao.getUserByUserName(username)).thenReturn(user);

        User retrievedUser = userService.getUserByUserName(username);

        assertEquals(user, retrievedUser);
    }

    @Test
    void testGetUserByUserName_UserNotFound() {
        String username = "non_existing_user";
        
        try {
            userService.getUserByUserName(username);
            fail("Expected ResourceNotFoundException, but no exception was thrown.");
        } catch (ResourceNotFoundException e) {
            assertEquals("NO USER FOUND FOR USERNAME : " + username, e.getMessage());
        }
    }

    @Test
    void testGetAllUsers_Success() {
        User user1 = new User();
        User user2 = new User();
        List<User> userList = Arrays.asList(user1, user2);
        when(userDao.getAllUsers()).thenReturn(userList);

        List<User> retrievedUsers = userService.getAllUsers();

        assertEquals(userList, retrievedUsers);
    }

    @Test
    void testGetAllUsers_NoUsersFound() {
        when(userDao.getAllUsers()).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getAllUsers());

        assertEquals("user not found", exception.getMessage());
    }
   

}
