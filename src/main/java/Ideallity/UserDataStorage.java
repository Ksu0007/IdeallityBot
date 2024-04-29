package Ideallity;

import java.util.HashMap;
import java.util.Map;

import Ideallity.User;


public class UserDataStorage {
    private Map<Long, User> userMap;

    public UserDataStorage() {
        userMap = new HashMap<>();
    }

    public void addUser(User user) {
        userMap.put(user.getChatId(), user);
    }

    public User getUser(Long chatId) {
        return userMap.get(chatId);
    }

    public void updateUser(User user) {
        userMap.put(user.getChatId(), user);
    }

    public void removeUser(Long chatId) {
        userMap.remove(chatId);
    }
}
