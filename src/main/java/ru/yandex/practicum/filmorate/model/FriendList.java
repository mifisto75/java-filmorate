package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendList {
    @NotNull
    private Integer fromUserId;
    @NotNull
    private Integer toUserId;
    @NotNull
    private boolean status;
}
