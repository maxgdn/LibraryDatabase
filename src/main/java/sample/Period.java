package sample;

import java.time.LocalTime;

/**
 * Created by Max on 3/14/2016.
 */
public enum Period {
    BEFORE_CLASS(8,30), ONE(9,50), TWO(11,15), LUNCH(12,00), THREE(13,20), FOUR(14,40), AFTER_CLASS(19,00);

    public final LocalTime endTime;

    Period(int hour, int minute) {
        endTime = LocalTime.of(hour,minute);
    }
}
