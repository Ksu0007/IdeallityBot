package Ideallity;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Map;

import static Ideallity.IdeallityBotUtils.createMessage;
import static Ideallity.IdeallityBotUtils.createPhotoMessage;
import static Ideallity.IdeallityBotUtils.*;
import static org.example.TelegramBotContent.*;


public class Bot extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = "7151183890:AAHK3qD2XpFIBYLXD-0q78YdVMW-xTY3cUY";
    private static final String BOT_USERNAME = "Ideallity_bot";
    private UserDataStorage userDataStorage = new UserDataStorage();
    private BotState botState = BotState.START;


    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    @Override
    public void onUpdateReceived(Update update) {

        Long chatId = getChatId(update);
        User user = userDataStorage.getUser(chatId);
        if (user == null) {
            user = new User();
            user.setChatId(chatId);
            userDataStorage.addUser(user);
        }

        if (update.hasMessage() && (update.getMessage().getText().equals("/start") || update.getMessage().getText().equals("Calculate my normal weight"))) {
            sendMessage(chatId, "step_1_pic", STEP_1_TEXT, Map.of(
                    "Calculate my normal weight", "step_1_btn"));
            user.setBotState(BotState.ASK_IDEAL_WEIGHT);
            return;
        }
        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            if (callbackData.equals("step_1_btn")) {
                sendMessage(chatId, STEP_2_TEXT);
                user.setBotState(BotState.ASK_IDEAL_WEIGHT);
                return;
            }
        }

        BotState currentState = user.getBotState();

        switch (currentState) {
            case ASK_IDEAL_WEIGHT:
                if (update.hasMessage() && update.getMessage().hasText()) {
                    try {
                        Double idealWeight = Double.parseDouble(update.getMessage().getText());
                        user.setIdealWeight(idealWeight);
                        sendMessage(chatId, "Please enter the circumference of your wrist in centimeters:");
                        user.setBotState(BotState.ASK_WRIST_SIZE);
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Invalid input. Please enter a number.");
                    }
                }
                break;
            case ASK_WRIST_SIZE:
                if (update.hasMessage() && update.getMessage().hasText()) {
                    try {
                        Float wristSize = Float.parseFloat(update.getMessage().getText());
                        user.setWristSize(wristSize);
                        sendMessage(chatId, "Please enter your height in centimeters:");
                        user.setBotState(BotState.ASK_HEIGHT);
                        return;
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Invalid input. Please enter a number.");
                        return;
                    }
                }
                break;
            case ASK_HEIGHT:
                if (update.hasMessage() && update.getMessage().hasText()) {
                    try {
                        int height = Integer.parseInt(update.getMessage().getText());
                        user.setHeight(height);
                        String gender = user.getGender();
                        if (gender == null) {
                            sendMessage(chatId, "step_1_pic", "Please choose your gender",
                                    Map.of("Female", "step_4_btn", "Male", "step_4.1_btn"));
                        }
                        user.setBotState(BotState.ASK_GENDER);
                        return;
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Invalid input. Please enter a number.");
                    }
                }
                break;
            case ASK_GENDER:
                if (update.getCallbackQuery() != null) {
                    if (user.getGender() == null) {
                        String callbackData = update.getCallbackQuery().getData();
                        if ("step_4_btn".equals(callbackData)) {
                            user.setGender("female");
                        } else if ("Male".equals(callbackData)) {
                            user.setGender("male");
                        }
                        Double normalWeight = calculateNormalWeight(user);
                        user.setNormalWeight(normalWeight);
                        String compareMessage = compareWeight(normalWeight, user.getIdealWeight());
                        sendMessage(chatId, "Your normal weight is " + normalWeight + " kg. " + compareMessage
                                + "Please enter your real weight.");
                        user.setBotState(BotState.ASK_REAL_WEIGHT);
                    }
                }
                break;
            case ASK_REAL_WEIGHT:
                if (update.hasMessage() && update.getMessage().hasText()) {
                    try {
                        Double realWeight = Double.parseDouble(update.getMessage().getText());
                        user.setRealWeight(realWeight);
                        sendMessage(chatId, "Please enter your wish weight");
                        user.setBotState(BotState.ASK_WISH_WEIGHT);
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Invalid input. Please enter a number.");
                    }
                }
                break;
            case ASK_WISH_WEIGHT:
                if (update.hasMessage() && update.getMessage().hasText()) {
                    try {
                        Double wishWeight = Double.parseDouble(update.getMessage().getText());
                        user.setWishWeight(wishWeight);
                        sendMessage(chatId, STEP_6_TEXT);
                        user.setBotState(BotState.ASK_AGE);
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Invalid input. Please enter a number.");
                    }
                }
                break;
            case ASK_AGE:
                if (update.hasMessage() && update.getMessage().hasText()) {
                    try {
                        int age = Integer.parseInt(update.getMessage().getText());
                        user.setAge(age);
                        sendMessage(chatId, "step_1_pic", STEP_7_TEXT, Map.of("1", "step_7.1_btn",
                                "2", "step_7.2_btn",
                                "3", "step_7.3_btn",
                                "4", "step_7.4_btn",
                                "5", "step_7.5_btn"));
                        user.setBotState(BotState.ASK_ACTIVITY_LEVEL);
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Invalid input. Please enter a number.");
                    }
                }
                break;
            case ASK_ACTIVITY_LEVEL:
                if (update.getCallbackQuery() != null) {
                    if (user.getActivityLevel() == 0) {
                        String callbackData = update.getCallbackQuery().getData();
                        if ("step_7.1_btn".equals(callbackData)) {
                            user.setActivityLevel(0.2);
                        } else if ("step_7.2_btn".equals(callbackData)) {
                            user.setActivityLevel(0.4);
                        } else if ("step_7.3_btn".equals(callbackData)) {
                            user.setActivityLevel(0.6);
                        } else if ("step_7.4_btn".equals(callbackData)) {
                            user.setActivityLevel(0.7);
                        } else {
                            user.setActivityLevel(0.9);
                        }
                        sendMessage(chatId, "Your energy consumption is " + totalEnergyConsumption(user)
                                + " calories per day. *Please set  the period in days during which you want" +
                                " to achieve a normal weight*. Considering that it is advisable to lose weight per month " +
                                "by no more than 5% of real weight. It's " + user.getRealWeight() * 0.05 + " kg for you");
                        user.setBotState(BotState.ASK_GOAL_DAYS);
                    }
                }
                break;
            case ASK_GOAL_DAYS:
                if (update.hasMessage() && update.getMessage().hasText()) {
                    try {
                        int goalDays = Integer.parseInt(update.getMessage().getText());
                        user.setGoalDays(goalDays);
                        sendMessage(chatId, "To achieve your goal in " + goalDays + " days you need to spend "
                                + caloriesPerDay(user) + " additional calories per day. You could do it by cutting your " +
                                "calories " + caloriesFromFood(user) + " from your ration. And "
                                + caloriesFromSport(user) + " from sport activities");
                        user.setBotState(BotState.FINISH);
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Invalid input. Please enter a number.");
                    }
                }
                break;
            case FINISH:
                // Handle finish state if needed
                break;
            default:
                // Handle default state if needed
                break;
        }
    }



    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void sendMessage(Long chatId, String picName, String text, Map<String, String> buttons){

        SendPhoto photoMessage =  createPhotoMessage(chatId, picName);
        executeAsync(photoMessage);

        SendMessage message = createMessage(chatId, text, buttons);
        sendApiMethodAsync(message);
    }


    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new Bot());
    }
}
