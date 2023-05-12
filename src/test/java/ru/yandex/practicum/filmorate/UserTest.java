package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest

public class UserTest {
    private User user;
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        user = User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
    }
    @Test
    public void addUserOkTest() {
        User user1 = userController.addUser(user);
        assertEquals(user, user1);
        assertEquals(1, userController.allUsers().size());
    }

 //   @Test
  //  public void addUserFailLoginTest() {

   //     user.setLogin("dolore ullamco");
   //    userController.addUser(user) ;
   //      assertThrows(ValidationException.class ,()-> userController.addUser(user));
   //    assertEquals(0, userController.allUsers().size());
   // }
}

