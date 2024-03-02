package controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mfp.api.controller.AdminController;
import com.mfp.api.entity.Role;
import com.mfp.api.entity.User;
import com.mfp.api.exception.InvalidInputException;
import com.mfp.api.exception.ResourceNotFoundException;
import com.mfp.api.exception.SomethingWentWrongException;
import com.mfp.api.service.UserService;
import com.mfp.api.utility.ValidateInputData;

class AdminControllerTest {

    private AdminController adminController;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        adminController = new AdminController();
        adminController.setUserService(userService);
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User();
        when(userService.addUser(user)).thenReturn(true);

        ResponseEntity<Boolean> response = adminController.registerUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testRegisterUser_Failure() {
        User user = new User();
        when(userService.addUser(user)).thenReturn(false);

        SomethingWentWrongException exception = assertThrows(SomethingWentWrongException.class,
                () -> adminController.registerUser(user));

        assertEquals("User Not Saved...", exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        String userName = "john_doe";
        mockStatic(ValidateInputData.class);
        when(ValidateInputData.validateUserName(userName)).thenReturn(true);
        when(userService.deleteUser(userName)).thenReturn("success");

        ResponseEntity<String> response = adminController.deleteUser(userName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User with userName john_doe deleted successfully.", response.getBody());
    }

    @Test
    void testDeleteUser_InvalidUserName() {
        String userName = "";
        mockStatic(ValidateInputData.class);
        when(ValidateInputData.validateUserName(userName)).thenReturn(false);

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> adminController.deleteUser(userName));

        assertEquals("User Name Should Not Be Empty : ", exception.getMessage());
    }

    @Test
    void testDeleteUser_UserNotFound() {
        String userName = "non_existing_user";
        when(ValidateInputData.validateUserName(userName)).thenReturn(true);
        when(userService.deleteUser(userName)).thenReturn("not_found");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminController.deleteUser(userName));

        assertEquals("USER NOT FOUND....|USERNAME: non_existing_user", exception.getMessage());
    }


    @Test
    void testGetAllAdmin_Success() {
        List<User> userList = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = adminController.getAllAdmin();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
    }

    @Test
    void testGetAllAdmin_UserNotFound() {
        when(userService.getAllUsers()).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminController.getAllAdmin());

        assertEquals("user not found", exception.getMessage());
    }

    @Test
    void testAddRole_Success() {
        Role role = new Role();
        when(userService.addRole(role)).thenReturn(role);

        ResponseEntity<Object> response = adminController.addRole(role);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    void testAddRole_Failure() {
        Role role = new Role();
        when(userService.addRole(role)).thenReturn(null);

        SomethingWentWrongException exception = assertThrows(SomethingWentWrongException.class,
                () -> adminController.addRole(role));

        assertEquals("Role Not Saved...", exception.getMessage());
    }

    @Test
    void testGetRoleById_Success() {
        int roleId = 1;
        Role role = new Role();
        when(userService.getRoleById(roleId)).thenReturn(role);

        ResponseEntity<Role> response = adminController.getRoleById(roleId);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    void testGetRoleById_RoleNotFound() {
        int roleId = 1;
        when(userService.getRoleById(roleId)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminController.getRoleById(roleId));

        assertEquals("RESOURCE NOT FOUND FOR ID : 1", exception.getMessage());
    }
   
}

