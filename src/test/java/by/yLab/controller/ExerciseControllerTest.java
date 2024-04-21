package by.yLab.controller;


import by.yLab.util.FormatDateTime;
import by.yLab.entity.Exercise;
import by.yLab.entity.User;
import by.yLab.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseControllerTest {

    private static final String TEST_USER_FIRSTNAME = "first";
    private static final String TEST_USER_LASTNAME = "last";
    private static final String TEST_USER_BIRTHDAY = "11.11.2020";
    private static final String TEST_USER_EMAIL = "@.";
    private static final String TEST_EXERCISE_NAME = "run";
    private static final int TEST_EXERCISE_BURN_CALORIES = 5;

    private final User user = new User(TEST_USER_FIRSTNAME,
            TEST_USER_LASTNAME,
            LocalDate.parse(TEST_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_USER_EMAIL,
            LocalDate.now());

    private Exercise exercise;
    @InjectMocks
    private ExerciseController exerciseController;
    @Mock
    private ExerciseService exerciseService;

    @BeforeEach
    void init() {
        exercise = new Exercise(TEST_EXERCISE_NAME, TEST_EXERCISE_BURN_CALORIES);
    }

    @Test
    void createExercise() {
        doReturn(exercise).when(exerciseService).createExercise(user, TEST_EXERCISE_NAME, TEST_EXERCISE_BURN_CALORIES);
        assertEquals(exercise, exerciseController.createExercise(user, TEST_EXERCISE_NAME, TEST_EXERCISE_BURN_CALORIES),
                "возвращен не правильный тип тренировки");
        verify(exerciseService, times(1)).createExercise(user, TEST_EXERCISE_NAME, TEST_EXERCISE_BURN_CALORIES);
    }

    @Test
    void isExerciseNew() {
        doReturn(true).when(exerciseService).isExerciseNew(user, exercise);
        assertTrue(exerciseController.isExerciseNew(user, exercise),
                "не созданный тип тренировки принят за созданный");
        verify(exerciseService, times(1)).isExerciseNew(user, exercise);
    }

    @Test
    void getUserExercises() {
        Set<Exercise> exerciseSet = new HashSet<>();
        doReturn(exerciseSet).when(exerciseService).getUserExercises(user);
        assertEquals(exerciseSet, exerciseController.getUserExercises(user),
                "возвращен не правильный список типов тренировок");
        verify(exerciseService, times(1)).getUserExercises(user);
    }

    @Test
    void getExerciseToName() {
        doReturn(Optional.of(exercise)).when(exerciseService).getExerciseToName(user, TEST_EXERCISE_NAME);
        assertTrue(exerciseController.getExerciseToName(user, TEST_EXERCISE_NAME).isPresent(),
                "тип тренировки не найден по названию");
        verify(exerciseService, times(1)).getExerciseToName(user, TEST_EXERCISE_NAME);
    }

}
