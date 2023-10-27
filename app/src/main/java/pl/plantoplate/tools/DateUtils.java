/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.plantoplate.tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateUtils {
    public static String formatPolishDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, d, yyyy", new Locale("pl", "PL"));
        String formattedDate = date.format(formatter);
        Map<String, String> monthMapping = createMonthMapping();
        formattedDate = monthMapping.get(formattedDate.split(",")[0]) + formattedDate.substring(formattedDate.indexOf(","));
        formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
        formattedDate = formattedDate.replaceFirst(",", "");
        return formattedDate;
    }

    private static Map<String, String> createMonthMapping() {
        Map<String, String> monthMapping = new HashMap<>();
        monthMapping.put("stycznia", "Styczeń");
        monthMapping.put("lutego", "Luty");
        monthMapping.put("marca", "Marzec");
        monthMapping.put("kwietnia", "Kwiecień");
        monthMapping.put("maja", "Maj");
        monthMapping.put("czerwca", "Czerwiec");
        monthMapping.put("lipca", "Lipiec");
        monthMapping.put("sierpnia", "Sierpień");
        monthMapping.put("września", "Wrzesień");
        monthMapping.put("października", "Październik");
        monthMapping.put("listopada", "Listopad");
        monthMapping.put("grudnia", "Grudzień");
        return monthMapping;
    }

    public static ArrayList<LocalDate> generateDates() {
        LocalDate today = LocalDate.now();
        ArrayList<LocalDate> dateList = new ArrayList<>();
        for (int i = -3; i <= 7; i++) {
            LocalDate date = today.plusDays(i);
            dateList.add(date);
        }
        return dateList;
    }
}