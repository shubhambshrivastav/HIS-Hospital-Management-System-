package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import com.mfp.api.entity.Role;
import com.mfp.api.entity.User;
import com.mfp.api.security.CustomUserDetail;
import com.mfp.api.service.UserService;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
    }

    @Test
    void testLoadUserByUserId_Success() {
        String userId = "123";
        CustomUserDetail customUserDetail = new CustomUserDetail(new User());
        when(userService.loadUserByUserId(userId)).thenReturn(customUserDetail);

        UserDetails userDetails = userService.loadUserByUserId(userId);

        assertNotNull(userDetails);
        assertEquals(customUserDetail, userDetails);
    }

    @Test
    void testAddUser_Success() {
        User user = new User();
        when(userService.addUser(user)).thenReturn(true);

        boolean result = userService.addUser(user);

        assertTrue(result);
    }

    @Test
    void testLoginUser_Success() {
        User user = new User();
        when(userService.loginUser(user)).thenReturn(user);

        User result = userService.loginUser(user);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testDeleteUser_Success() {
        String userName = "john_doe";
        when(userService.deleteUser(userName)).thenReturn("success");

        String result = userService.deleteUser(userName);

        assertEquals("success", result);
    }

    @Test
    void testGetUserByUserName_Success() {
        String username = "john_doe";
        User user = new User();
        when(userService.getUserByUserName(username)).thenReturn(user);

        User result = userService.getUserByUserName(username);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetAllUsers_Success() {
        List<User> userList = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(userList);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(userList, result);
    }

    @Test
    void testUpdateUser_Success() {
        String username = "john_doe";
        User user = new User();
        when(userService.updateUser(username, user)).thenReturn(user);

        User result = userService.updateUser(username, user);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetUsersTotalCounts_Success() {
        Long totalCounts = 10L;
        when(userService.getUsersTotalCounts()).thenReturn(totalCounts);

        Long result = userService.getUsersTotalCounts();

        assertNotNull(result);
        assertEquals(totalCounts, result);
    }

    @Test
    void testGetUsersTotalCountsByType_Success() {
        String type = "admin";
        Long totalCounts = 5L;
        when(userService.getUsersTotalCounts(type)).thenReturn(totalCounts);

        Long result = userService.getUsersTotalCounts(type);

        assertNotNull(result);
        assertEquals(totalCounts, result);
    }

    @Test
    void testGetUserCountByDateAndType_Success() {
        Date registeredDate = Date.valueOf("2022-01-01");
        String type = "user";
        Long count = 3L;
        when(userService.getUserCountByDateAndType(registeredDate, type)).thenReturn(count);

        Long result = userService.getUserCountByDateAndType(registeredDate, type);

        assertNotNull(result);
        assertEquals(count, result);
    }

    @Test
    void testGetUserByFirstName_Success() {
        String firstName = "John";
        List<User> userList = new ArrayList<>();
        when(userService.getUserByFirstName(firstName)).thenReturn(userList);

        List<User> result = userService.getUserByFirstName(firstName);

        assertNotNull(result);
        assertEquals(userList, result);
    }

    @Test
    void testAddRole_Success() {
        Role role = new Role();
        when(userService.addRole(role)).thenReturn(role);

        Role result = userService.addRole(role);

        assertNotNull(result);
        assertEquals(role, result);
    }

    @Test
    void testGetRoleById_Success() {
        int roleId = 1;
        Role role = new Role();
        when(userService.getRoleById(roleId)).thenReturn(role);

        Role result = userService.getRoleById(roleId);

        assertNotNull(result);
        assertEquals(role, result);
    }

 
}
