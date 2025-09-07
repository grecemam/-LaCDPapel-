package com.example.lacdpapel;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Date;

import static org.junit.Assert.*;


import com.example.lacdpapel.Activity.Decoration;
import com.example.lacdpapel.Activity.DetailActor;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    // Инициализация mock-объектов
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    // Освобождение ресурсов после каждого теста
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    // Тест на создание нового пользователя
    @Test
    @DisplayName("Тест: Успешное создание нового пользователя")
    void testCreateUser_Success() {
        User user = new User("John", "Doe", "john.doe@example.com");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
        assertEquals("john.doe@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    // Тест на валидацию - попытка создания пользователя с невалидным email
    @Test
    @DisplayName("Тест: Ошибка при создании пользователя с невалидным email")
    void testCreateUser_InvalidEmail() {
        User user = new User("Jane", "Doe", "invalid-email");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Некорректный email", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    // Тест на получение пользователя по ID
    @Test
    @DisplayName("Тест: Поиск существующего пользователя по ID")
    void testFindUserById_UserExists() {
        User user = new User("Alice", "Smith", "alice.smith@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("Alice", foundUser.get().getFirstName());
        verify(userRepository, times(1)).findById(1L);
    }

    // Тест на попытку поиска пользователя, которого нет в базе данных
    @Test
    @DisplayName("Тест: Пользователь не найден по ID")
    void testFindUserById_UserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findUserById(2L);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(2L);
    }

    // Тест на обновление пользователя
    @Test
    @DisplayName("Тест: Успешное обновление пользователя")
    void testUpdateUser_Success() {
        User existingUser = new User("John", "Doe", "john.doe@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        User updatedData = new User("Johnny", "Doe", "johnny.doe@example.com");
        when(userRepository.save(any(User.class))).thenReturn(updatedData);

        User updatedUser = userService.updateUser(1L, updatedData);

        assertEquals("Johnny", updatedUser.getFirstName());
        assertEquals("Doe", updatedUser.getLastName());
        assertEquals("johnny.doe@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).save(updatedData);
    }

}