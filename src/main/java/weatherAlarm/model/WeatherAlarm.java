/*
 * Copyright 2015 John Scattergood
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package weatherAlarm.model;

import weatherAlarm.util.PredicateEnum;

import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author <a href="mailto:john.scattergood@gmail.com">John Scattergood</a> 1/4/2015
 */
public class WeatherAlarm {
    private String username;
    private String emailAddress;
    private Map<WeatherDataEnum, ValuePredicate> criteria = new EnumMap<>(WeatherDataEnum.class);
    private Date lastNotification;

    public WeatherAlarm(String username, String emailAddress) {
        this.username = username;
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ValuePredicate<?> getCriteria(WeatherDataEnum weatherDataEnum) {
        return criteria.get(weatherDataEnum);
    }

    public void setCriteria(WeatherDataEnum weatherDataEnum, ValuePredicate<?> valuePredicate) {
        criteria.put(weatherDataEnum, valuePredicate);
    }

    public boolean matchesCriteria(WeatherDataEnum weatherDataEnum, Comparable value) {
        ValuePredicate valuePredicate = getCriteria(weatherDataEnum);
        //noinspection unchecked
        return valuePredicate != null &&
                valuePredicate.satisfies(value);
    }

    public Date getLastNotification() {
        return lastNotification;
    }

    public void setLastNotification(Date lastNotification) {
        this.lastNotification = lastNotification;
    }

    public static class ValuePredicate<T> {
        private PredicateEnum predicate;
        private Comparable<T> value;
        private Class<T> valueClass;

        public ValuePredicate(PredicateEnum predicate, Comparable<T> value) {
            this.predicate = predicate;
            this.value = value;
        }

        public PredicateEnum getPredicate() {
            return predicate;
        }

        public Comparable<T> getValue() {
            return value;
        }

        public boolean satisfies(T value) {
            int comparison = this.value.compareTo(value);
            switch (predicate) {
                case EQ:
                    return this.value.equals(value);
                case NE:
                    return !this.value.equals(value);
                case GT:
                    return comparison == -1;
                case GE:
                    return comparison == -1 || comparison == 0;
                case LT:
                    return comparison == 1;
                case LE:
                    return comparison == 1 || comparison == 0;
                default:
                    return false;
            }
        }
    }
}
