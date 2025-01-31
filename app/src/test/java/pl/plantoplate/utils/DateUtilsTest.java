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
package pl.plantoplate.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.time.LocalDate;
import java.util.List;

public class DateUtilsTest {

    @Test
    public void testFormatPolishDate() {
        // Test formatting a specific LocalDate
        LocalDate testDate = LocalDate.of(2023, 5, 15);
        String formattedDate = DateUtils.formatPolishDate(testDate);

        assertEquals("Maj 15, 2023", formattedDate);
    }

    @Test
    public void testGenerateDates() {
        // Test generating dates without including past dates
        List<LocalDate> dateList = DateUtils.generateDates(false);

        assertEquals(8, dateList.size());

        // Test if the dates are consecutive
        for (int i = 0; i < dateList.size() - 1; i++) {
            LocalDate current = dateList.get(i);
            LocalDate next = dateList.get(i + 1);
            assertEquals(current.plusDays(1), next);
        }

        // Test generating dates including past dates
        List<LocalDate> dateListWithPast = DateUtils.generateDates(true);

        assertEquals(11, dateListWithPast.size());
        // Assuming today is the reference point for the test
        assertEquals(LocalDate.now().minusDays(3), dateListWithPast.get(0));
    }
}
