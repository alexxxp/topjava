package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List list = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        HashMap<LocalDate, Integer> dateToCalories = new HashMap<>();
        int temp = 0;
        UserMeal userMeal;

        List<UserMealWithExceed> mealWithExceedList = new ArrayList<>();

        for (int i = 0; i < mealList.size(); i++) {
            userMeal = mealList.get(i);
            LocalDate userMealDate = userMeal.getDateTime().toLocalDate();
            int userMealCalories = userMeal.getCalories();

            if (!dateToCalories.containsKey(userMealDate)) {
                dateToCalories.put(userMealDate, userMealCalories);
            }
            else {
                temp = dateToCalories.get(userMealDate);
                temp += userMealCalories;
                dateToCalories.put(userMealDate, temp);
            }
        }

        for (UserMeal u : mealList) {
            int sumCalories = dateToCalories.get(u.getDateTime().toLocalDate());
            if (sumCalories > caloriesPerDay) {
                mealWithExceedList.add(new UserMealWithExceed(
                        u.getDateTime(),
                        u.getDescription(),
                        u.getCalories(),
                        true));
            } else {
                mealWithExceedList.add(new UserMealWithExceed(
                        u.getDateTime(),
                        u.getDescription(),
                        u.getCalories(),
                        false));
            }
        }

        return mealWithExceedList
                .stream()
                .filter(userMealWithExceed ->
                        userMealWithExceed.getDateTime().toLocalTime().isAfter(startTime) &&
                        userMealWithExceed.getDateTime().toLocalTime().isBefore(endTime))
                .collect(Collectors.toList());
    }
}
