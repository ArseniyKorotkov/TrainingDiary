package by.y_lab.p5controller;

import by.y_lab.p0util.FormatDate;
import by.y_lab.p1entity.NoteExerciseInDiary;
import by.y_lab.p1entity.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ShowDiaryController {

    private static final ShowDiaryController INSTANCE = new ShowDiaryController();

    private ShowDiaryController() {}

    public List<TreeSet<NoteExerciseInDiary>> getDiaryInDays(String daysString, User user) {
        String[] days = daysString.split("/");
        LocalDate startQuestDayDiary = LocalDate.parse(days[0], FormatDate.reform());
        LocalDate endQuestDayDiary = LocalDate.parse(days[1], FormatDate.reform());

        if (endQuestDayDiary.isAfter(LocalDate.now())) {
            endQuestDayDiary = LocalDate.now();
        }

        if (user.getRegistrationDate().isAfter(startQuestDayDiary)) {
            startQuestDayDiary = user.getRegistrationDate();
        }


        int startQuestDayInList = (int) ChronoUnit.DAYS.between(user.getRegistrationDate(), startQuestDayDiary);
        int daysInQuestion = (int) ChronoUnit.DAYS.between(startQuestDayDiary, endQuestDayDiary) + 1;

        List<TreeSet<NoteExerciseInDiary>> diaryList = user.getDiary().getDiaryList();

        List<TreeSet<NoteExerciseInDiary>> exercisesInDays = new ArrayList<>();

        for (int i = startQuestDayInList; i < daysInQuestion; i++) {
            exercisesInDays.add(diaryList.get(i));
        }

        return exercisesInDays;

    }


    public static ShowDiaryController getInstance() {
        return INSTANCE;
    }
}
