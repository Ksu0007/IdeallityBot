package Ideallity;

public class UserDataStorageTest {

    public static void main(String[] args) {
        // Создаем экземпляр хранилища данных
        UserDataStorage storage = new UserDataStorage();

        // Тест добавления пользователя
        testAddUser(storage);

        // Тест извлечения пользователя
        testGetUser(storage);

        // Тест обновления пользователя
        testUpdateUser(storage);

        // Тест удаления пользователя
        testRemoveUser(storage);
    }

    private static void testAddUser(UserDataStorage storage) {
        // Создаем пользователя
        User user = new User();
        user.setChatId(123456789L);
        user.setIdealWeight(60.0);

        // Добавляем пользователя в хранилище
        storage.addUser(user);

        // Выводим сообщение об успешном добавлении
        System.out.println("User added successfully.");
    }

    private static void testGetUser(UserDataStorage storage) {
        // Извлекаем пользователя из хранилища
        User user = storage.getUser(123456789L);

        // Проверяем, что пользователь был успешно извлечен
        if (user != null) {
            // Выводим информацию о пользователе
            System.out.println("User chatId: " + user.getChatId());
            System.out.println("User idealWeight: " + user.getIdealWeight());
        } else {
            System.out.println("User not found.");
        }
    }

    private static void testUpdateUser(UserDataStorage storage) {
        // Получаем пользователя из хранилища
        User user = storage.getUser(123456789L);

        // Если пользователь найден, обновляем его данные
        if (user != null) {
            // Обновляем данные пользователя
            user.setIdealWeight(65.0);

            // Обновляем пользователя в хранилище
            storage.updateUser(user);

            // Выводим сообщение об успешном обновлении
            System.out.println("User updated successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void testRemoveUser(UserDataStorage storage) {
        // Удаляем пользователя из хранилища
        storage.removeUser(123456789L);

        // Пытаемся извлечь пользователя после удаления
        User user = storage.getUser(123456789L);

        // Проверяем, что пользователь был успешно удален
        if (user == null) {
            // Выводим сообщение об успешном удалении
            System.out.println("User removed successfully.");
        } else {
            System.out.println("User still exists.");
        }
    }
}
