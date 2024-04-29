package Ideallity;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IdeallityBotUtils {

    public static Long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        }

        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }

        return null;
    }

    public static SendMessage createMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        message.setParseMode("markdown");
        message.setChatId(chatId);
        return message;
    }

    public static SendMessage createMessage(Long chatId, String text, Map<String, String> buttons) {
        SendMessage message = createMessage(chatId, text);
        if (buttons != null && !buttons.isEmpty())
            attachButtons(message, buttons);
        return message;
    }

    private static void attachButtons(SendMessage message, Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonName : buttons.keySet()) {
            String buttonValue = buttons.get(buttonName);

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(new String(buttonName.getBytes(), StandardCharsets.UTF_8));
            button.setCallbackData(buttonValue);

            keyboard.add(List.of(button));
        }

        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
    }

    public static SendPhoto createPhotoMessage(Long chatId, String name) {
        try {
            SendPhoto photo = new SendPhoto();
            InputFile inputFile = new InputFile();
            inputFile.setMedia(Files.newInputStream(Path.of("images/" + name + ".jpg")), name);
            photo.setPhoto(inputFile);
            photo.setChatId(chatId);
            return photo;
        } catch (IOException e) {
            throw new RuntimeException("Can't create photo message!");
        }
    }

    static double calculateNormalWeight(User user) {
        Double wristSize = Double.valueOf(user.getWristSize());
        int height = user.getHeight();
        String gender = user.getGender();

        if ("female".equals(gender)) {
            if (wristSize > 15.0 && wristSize <= 17.0) {
                double normalWeight = (double) (height * 0.34);

                return normalWeight;
            } else if (wristSize > 17.0) {
                return  height * 0.355 * 1.01;
            } else {
                return height * 0.325 * 0.99;
            }
        } else {
            if (wristSize > 18.0 && wristSize <= 20.0) {
                return height * 0.39;
            } else if (wristSize > 20.0) {
                return  height * 0.41 * 1.01;
            } else {
                return height * 0.375 * 0.99;
            }
        }
    }

    static String compareWeight(Double idealWeight, Double normalWeight) {
        double diff = idealWeight - normalWeight;
        String formattedDiff = String.format("%.2f", Math.abs(diff));

        if (diff == 0) {
            return "Your ideal weight is equal to your normal weight.";
        } else if (diff > 0) {
            return "Your ideal weight is " + formattedDiff + " kg more than your normal weight.";
        } else {
            return "Your ideal weight is " + formattedDiff + " kg less than your normal weight.";
        }
    }

    static Double basalMetabolism(User user) {
        String gender = user.getGender();
        double normalWeight = user.getNormalWeight();
        int height = user.getHeight();
        int age = user.getAge();

        if ("female".equals(gender)) {
           double basalAct = (655 + (9.6 * normalWeight) + (1.8 * height) - (4.7 * age));
            return basalAct;
        } else {
            return (double) (66 + (13.7 * normalWeight) + (5 * height) - (6.8 * age));
        }
    }

    static Double energy(User user) {
        double activityLevel = user.getActivityLevel();
        return basalMetabolism(user) * activityLevel;
    }

    static Double foodThermo(User user) {
        return (basalMetabolism(user) + energy(user)) * 0.1;
    }

    static int totalEnergyConsumption(User user) {
        return (int) (basalMetabolism(user) + energy(user) + foodThermo(user));
    }

    static String extraCalories(User user) {
        int extraCalories = (int) ((user.getRealWeight() - user.getNormalWeight()) * 7500);
        return  "You have extra " + extraCalories + " calories in your body to achieve normal weight";
    }

    static int caloriesPerDay(User user) {
        int goalDays = user.getGoalDays();
        int extraCalories = (int) ((user.getRealWeight() - user.getWishWeight()) * 7500);
        return (int) (extraCalories / goalDays);
    }

    static int caloriesFromFood(User user) {
        return (int) (basalMetabolism(user) * 0.2);
    }

    static int caloriesFromSport(User user) {
        int caloriesFromSport = (int) (caloriesPerDay(user) - caloriesFromFood(user));
        return caloriesFromSport;
    }

}
