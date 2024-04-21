package by.yLab.inOut;

import static by.yLab.util.SelectionItems.*;

/**
 * Страница пунктов основного меню
 */
public class MenuOptionsPage extends Page {

    private static final String OPTIONS_LIST = "Options list:";
    private static final String SELECT_FROM_THE_LIST_PROVIDED = "select from the list provided";

    /**
     * Демонстрация пунктов основного меню
     * Запрос на выбор пункта основного меню
     * @return ответ пользователя
     */
    public static String sayOptionsList() {
        System.out.println(OPTIONS_LIST);
        System.out.println(ADD_EXERCISE_TO_DIARY);
        System.out.println(CREATE_EXERCISE);
        System.out.println(SHOW_DIARY);
        System.out.println(ADMIN);
        System.out.println(EXIT);
        System.out.println();
        return SCANNER.nextLine();
    }

    /**
     * Демонстрация сообщения об ошибке в случае неверного запроса пункта основного меню
     */
    public static void nonMenuMessage() {
        System.out.println(SELECT_FROM_THE_LIST_PROVIDED);
    }
}
