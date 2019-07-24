package net.ricardochavezt.budgetbuddy.persistence;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long timestampToDate(Date value) {
        return value == null ? null : value.getTime();
    }
}
