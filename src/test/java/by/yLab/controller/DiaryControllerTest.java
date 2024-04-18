package by.yLab.controller;

import by.yLab.util.FormatDateTime;
import by.yLab.entity.Exercise;
import by.yLab.entity.NoteDiary;
import by.yLab.entity.User;
import by.yLab.service.NoteDiaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class DiaryControllerTest {

    private static final String TEST_USER_FIRSTNAME = "first";
    private static final String TEST_USER_LASTNAME = "last";
    private static final String TEST_USER_BIRTHDAY = "11.11.2020";
    private static final String TEST_USER_EMAIL = "@.";
    private static final String TEST_FIRST_EXERCISE_NAME = "run";
    private static final int TEST_FIRST_EXERCISE_BURN_CALORIES = 5;
    private static final int TEST_FIRST_EXERCISE_TIMES = 3;
    private static final LocalDateTime EXERCISE_DATE_TIME = LocalDateTime.now();
    private static final String START_TIME_SLICE = LocalDate.now().minusDays(4).format(FormatDateTime.reformDate());
    private static final String END_TIME_SLICE = LocalDate.now().minusDays(1).format(FormatDateTime.reformDate());


    private final User user = new User(TEST_USER_FIRSTNAME,
            TEST_USER_LASTNAME,
            LocalDate.parse(TEST_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_USER_EMAIL,
            LocalDate.now().minusDays(5));

    private final Exercise firstExercise = new Exercise(TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);

    @InjectMocks
    private DiaryController diaryController;
    @Mock
    private NoteDiaryService diaryService;

    @Test
    void addExercise() {
        diaryController.addExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES);
        verify(diaryService, times(1)).addExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES);
    }

    @Test
    void addMissingExercise() {
        diaryController.addMissingExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES, EXERCISE_DATE_TIME);
        verify(diaryService, times(1)).addExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES, EXERCISE_DATE_TIME);
    }

    @Test
    void deleteExercise() {
        diaryController.deleteExercise(user, firstExercise, EXERCISE_DATE_TIME.toLocalDate());
        verify(diaryService, times(1)).deleteExercise(user, firstExercise, EXERCISE_DATE_TIME.toLocalDate());
    }

    @Test
    void getDiaryTimeSlice() {
        ArrayList<NoteDiary> noteDiaries = new ArrayList<>();
        doReturn(noteDiaries).when(diaryService).getDiaryList(user);
        List<NoteDiary> diaryTimeSlice = diaryController.getDiaryTimeSlice(START_TIME_SLICE + "/" + END_TIME_SLICE, user);
        verify(diaryService, times(1)).getDiaryList(user);
        verify(diaryService, times(0)).getDateNoteExercises(user, LocalDate.now());
        assertEquals(noteDiaries, diaryTimeSlice, "возвращен не правильный список записей");
    }
}
