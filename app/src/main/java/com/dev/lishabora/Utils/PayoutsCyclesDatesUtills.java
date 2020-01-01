package com.dev.lishabora.Utils;

import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.Days;

import timber.log.Timber;

public class PayoutsCyclesDatesUtills {

    // public static final int BI_WEEKLY_CYCLE_CODE = 2;
    public static final int SEMI_MONTHLY_CYCLE_CODE = 2;
    public static final int MONTHLY_CYCLE_CODE = 3;



    private static final int WEEKLY_CYCLE_CODE = 1;

    public static EndAndStart getPayoutStartEndDate(int cycleCode, @Nullable EndAndStart tradersEndAndStart, @Nullable EndAndStart lastEndStartDate) {

        switch (cycleCode) {
            case WEEKLY_CYCLE_CODE:
                return weeklyAlgorothim(tradersEndAndStart, lastEndStartDate);
//            case BI_WEEKLY_CYCLE_CODE:
//                return biWeeklyAlgorithm(tradersEndAndStart, lastEndStartDate);
            case SEMI_MONTHLY_CYCLE_CODE:
                return semiMonthly();
            case MONTHLY_CYCLE_CODE:
                return monthly();
            default:


        }

        return null;

    }

    private static EndAndStart monthly() {
        EndAndStart dates = new EndAndStart();

        DateTime today = DateTimeUtils.Companion.getTodayDate();

        DateTime endOfMonth = today.dayOfMonth().withMaximumValue();
        DateTime startOfMonth = today.dayOfMonth().withMinimumValue();

        dates.setStartDate(DateTimeUtils.Companion.convert2String(startOfMonth));
        dates.setEndDate(DateTimeUtils.Companion.convert2String(endOfMonth));

        return dates;


    }

    private static EndAndStart semiMonthly() {
        EndAndStart dates = new EndAndStart();

        DateTime today = DateTimeUtils.Companion.getTodayDate();
        DateTime endOfMonth = today.dayOfMonth().withMaximumValue();
        DateTime startOfMonth = today.dayOfMonth().withMinimumValue();

        int p = Days.daysBetween(startOfMonth.toLocalDate(), today.toLocalDate()).getDays();
        if (p >= 15) {

            dates.setStartDate(DateTimeUtils.Companion.convert2String(DateTimeUtils.Companion.addDays(startOfMonth, 15)));
            dates.setEndDate(DateTimeUtils.Companion.convert2String(endOfMonth));

            return dates;
        } else {

            dates.setStartDate(DateTimeUtils.Companion.convert2String(startOfMonth));
            dates.setEndDate(DateTimeUtils.Companion.convert2String(DateTimeUtils.Companion.addDays(startOfMonth, 14)));

            return dates;
        }


    }

    private static EndAndStart biWeeklyAlgorithm(EndAndStart tradersEndAndStart, EndAndStart lastEndStartDate) {
        EndAndStart dates = new EndAndStart();
        if (lastEndStartDate != null) {
            DateTime lastStartDate = DateTimeUtils.Companion.conver2Date(lastEndStartDate.getStartDate());
            DateTime lastEndDate = DateTimeUtils.Companion.conver2Date(lastEndStartDate.getEndDate());
            if (DateTimeUtils.Companion.isPastLastDay(DateTimeUtils.Companion.convert2String(lastEndDate)) && DateTimeUtils.Companion.isPastLastDay(DateTimeUtils.Companion.addDaysString(lastEndDate, 13))) {
                int traderStartDayNumber = tradersEndAndStart.getStartDayNumber();
                int todayNumber = getNumberByDate(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "EEEE"));

                dates.setStartDate(DateTimeUtils.Companion.getDatePrevious(getDaysToSubtractFromToday(todayNumber, traderStartDayNumber)));
                dates.setEndDate(DateTimeUtils.Companion.addDaysString(DateTimeUtils.Companion.conver2Date(dates.getStartDate()), 13));


                return dates;
            }


            dates.setStartDate(DateTimeUtils.Companion.addDaysString(lastStartDate, 14));
            dates.setEndDate(DateTimeUtils.Companion.addDaysString(lastEndDate, 14));

            return dates;
        } else {
            int traderStartDayNumber = tradersEndAndStart.getStartDayNumber();
            int todayNumber = getNumberByDate(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "EEEE"));

            dates.setStartDate(DateTimeUtils.Companion.getDatePrevious(getDaysToSubtractFromToday(todayNumber, traderStartDayNumber)));
            dates.setEndDate(DateTimeUtils.Companion.addDaysString(DateTimeUtils.Companion.conver2Date(dates.getStartDate()), 13));


            return dates;
        }
    }

    private static EndAndStart weeklyAlgorothim(EndAndStart tradersEndAndStart, EndAndStart lastEndStartDate) {

        EndAndStart dates = new EndAndStart();
        if (lastEndStartDate != null) {
            DateTime lastStartDate = DateTimeUtils.Companion.conver2Date(lastEndStartDate.getStartDate());
            DateTime lastEndDate = DateTimeUtils.Companion.conver2Date(lastEndStartDate.getEndDate());


            if (DateTimeUtils.Companion.isPastLastDay(DateTimeUtils.Companion.convert2String(lastEndDate)) && DateTimeUtils.Companion.isPastLastDay(DateTimeUtils.Companion.addDaysString(lastEndDate, 6))) {
                int traderStartDayNumber = tradersEndAndStart.getStartDayNumber();
                int todayNumber = getNumberByDate(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "EEEE"));

                dates.setStartDate(DateTimeUtils.Companion.getDatePrevious(getDaysToSubtractFromToday(todayNumber, traderStartDayNumber)));
                dates.setEndDate(DateTimeUtils.Companion.addDaysString(DateTimeUtils.Companion.conver2Date(dates.getStartDate()), 6));


            } else {
                dates.setStartDate(DateTimeUtils.Companion.addDaysString(lastStartDate, 7));
                dates.setEndDate(DateTimeUtils.Companion.addDaysString(lastEndDate, 7));
            }

            return dates;
        } else {
            int traderStartDayNumber = tradersEndAndStart.getStartDayNumber();
            int todayNumber = getNumberByDate(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(), "EEEE"));

            int difference = todayNumber - traderStartDayNumber;

            dates.setStartDate(DateTimeUtils.Companion.getDatePrevious(getDaysToSubtractFromToday(todayNumber, traderStartDayNumber)));

            dates.setEndDate(DateTimeUtils.Companion.addDaysString(DateTimeUtils.Companion.conver2Date(dates.getStartDate()), 6));
            Timber.d("Traders Start No " + traderStartDayNumber + "  Today No " + todayNumber + " Difference " + difference);

            return dates;
        }


    }

    private static int getDaysToSubtractFromToday(int today, int traderStart) {

        int postiveDiffrence = today - traderStart;
        if (postiveDiffrence >= 0) {
            return postiveDiffrence;
        } else {
            return 7 + postiveDiffrence;
        }


    }



    public static String getDayByNumber(int number) {
        switch (number) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "";
        }
    }

    public static int getNumberByDate(String number) {
        Timber.d(number);
        switch (number) {
            case "Sunday":
                return 1;
            case "Monday":
                return 2;
            case "Tuesday":
                return 3;
            case "Wednesday":
                return 4;
            case "Thursday":
                return 5;
            case "Friday":
                return 6;
            case "Saturday":
                return 7;
            default:
                return 0;
        }
    }

    public static class EndAndStart {
        private String startDate;
        private String endDate;
        private int startDayNumber;
        private int endDayNumber;

        public EndAndStart() {
        }

        public EndAndStart(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public EndAndStart(int startDayNumber, int endDayNumber) {
            this.startDayNumber = startDayNumber;
            this.endDayNumber = endDayNumber;
        }

        public int getStartDayNumber() {
            return startDayNumber;
        }

        public void setStartDayNumber(int startDayNumber) {
            this.startDayNumber = startDayNumber;
        }

        public int getEndDayNumber() {
            return endDayNumber;
        }

        public void setEndDayNumber(int endDayNumber) {
            this.endDayNumber = endDayNumber;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }

}
