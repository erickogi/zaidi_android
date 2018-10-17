package com.dev.lishabora.Views.Trader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishaboramobile.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.LinkedList;
import java.util.List;

public class FarmerToolBarUI extends LinearLayout {
    public final static int TYPE_LOAN = 2;
    public final static int TYPE_ALL = 0;
    public final static int TYPE_MILK = 1;
    public final static int TYPE_ORDERS = 3;

    public final static int CAT_MONTHS = 1;
    public final static int CAT_YEAR = 2;
    public final static int CAT_DAYS = 0;

    public final static int CAT_PAYOUTS = 3;
    private List<MonthsDates> monthsDates = new LinkedList<>();


    private String dateTo;
    private String dateFrom;
    private View view;
    private MaterialSpinner materialSpinnerType, materialSpinnerCat, materialSpinnerMonths;
    private LinearLayout lSpinnerType, lSpinnerCat, lSpinnerMonths;
    private ImageView imageFrom, imgTo;
    private TextView txtFrom, txtTo;
    private TextView txtValueLabel1, txtValueLabel2, txtDayLabel, txtDateLabel;//t
    private LinearLayout lmlo, lddv;
    private LinearLayout lFrom, lTo;
    private RelativeLayout dateRange;

    public FarmerToolBarUI(Context context) {
        super(context);
        init(context);
    }

    public FarmerToolBarUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FarmerToolBarUI(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public String getDateTo() {
        if (dateTo != null) {
            return dateTo;
        } else {
            return DateTimeUtils.Companion.getToday();
        }
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateFrom() {
        if (dateFrom != null) {
            return dateFrom;
        } else {
            return DateTimeUtils.Companion.getToday();
        }
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    private void init(Context context) {
        View.inflate(context, R.layout.history_toolbar_ui, this);
        if (isInEditMode()) {
            return;
        }

        materialSpinnerCat = findViewById(R.id.spinner_cat);
        materialSpinnerType = findViewById(R.id.spinner_type);
        materialSpinnerMonths = findViewById(R.id.spinner_months);

        lSpinnerType = findViewById(R.id.lspinner_type);
        lSpinnerCat = findViewById(R.id.lspinner_cat);
        lSpinnerMonths = findViewById(R.id.lspinner_months);

        imageFrom = findViewById(R.id.img_from);
        imgTo = findViewById(R.id.img_to);

        txtFrom = findViewById(R.id.txt_from);
        txtTo = findViewById(R.id.txt_to);

        lmlo = findViewById(R.id.linear_t_all_titles);
        lddv = findViewById(R.id.linear_t_titles);

        txtDateLabel = findViewById(R.id.txt_date_label);
        txtDayLabel = findViewById(R.id.txt_day_label);

        txtValueLabel1 = findViewById(R.id.txt_value1_label);
        txtValueLabel2 = findViewById(R.id.txt_value2_label);

        dateRange = findViewById(R.id.date_range);

        lFrom = findViewById(R.id.lfrom);
        lTo = findViewById(R.id.lto);


        initializeDefaults();
    }

    public void initializeDefaults() {
        materialSpinnerCat.setItems("CHOOSE DAYS", "BY MONTH", "YEAR", "PAYOUTS");
        materialSpinnerType.setItems("All", "Milk", "Loans", "Orders");


        materialSpinnerType.setSelectedIndex(0);
        materialSpinnerCat.setSelectedIndex(0);


        txtTo.setText("..To .." + DateTimeUtils.Companion.getDisplayDate(DateTimeUtils.Companion.getToday(), DateTimeUtils.Companion.getDisplayDatePattern1()));
        txtFrom.setText("..From .." + DateTimeUtils.Companion.getDisplayDate(DateTimeUtils.Companion.getToday(), DateTimeUtils.Companion.getDisplayDatePattern1()));


        monthsDates = DateTimeUtils.Companion.getMonthsInYear(12);
        String[] months = new String[monthsDates.size()];
        for (int a = 0; a < monthsDates.size(); a++) {
            months[a] = monthsDates.get(a).getMonthName() + " ";

        }
        materialSpinnerMonths.setItems(months);


    }

    public void setMonths(boolean isMonths) {
        if (isMonths) {
            lSpinnerMonths.setVisibility(VISIBLE);


        } else {
            lSpinnerMonths.setVisibility(GONE);

        }

    }

    public void setOnFromClickListener(OnClickListener clickListener) {
        lFrom.setOnClickListener(clickListener);
    }

    public void setOnToClickListener(OnClickListener clickListener) {
        lTo.setOnClickListener(clickListener);
    }

    public void setOnTypeSelectListener(MaterialSpinner.OnItemSelectedListener listener) {
        materialSpinnerType.setOnItemSelectedListener(listener);
    }

    public void setOnCatSelectListener(MaterialSpinner.OnItemSelectedListener listener) {
        materialSpinnerCat.setOnItemSelectedListener(listener);
    }

    public void setOnMonthSelectListener(MaterialSpinner.OnItemSelectedListener listener) {
        materialSpinnerMonths.setOnItemSelectedListener(listener);
    }


    public void show() {


        switch (materialSpinnerType.getSelectedIndex()) {

            case TYPE_LOAN:
                lmlo.setVisibility(GONE);
                lddv.setVisibility(VISIBLE);
                txtValueLabel1.setVisibility(INVISIBLE);
                txtValueLabel2.setText("Value");


                break;
            case TYPE_ALL:
                lmlo.setVisibility(VISIBLE);
                lddv.setVisibility(GONE);

                txtValueLabel1.setVisibility(GONE);
                txtValueLabel2.setText("Value");


                break;


            case TYPE_MILK:
                lmlo.setVisibility(GONE);
                lddv.setVisibility(VISIBLE);
                txtValueLabel1.setVisibility(VISIBLE);
                txtValueLabel1.setText("AM");
                txtValueLabel2.setText("PM");


                break;
            case TYPE_ORDERS:

                lmlo.setVisibility(GONE);
                lddv.setVisibility(VISIBLE);
                txtValueLabel1.setVisibility(INVISIBLE);
                txtValueLabel2.setText("Value");


                break;
        }


        if (materialSpinnerCat.getSelectedIndex() == CAT_PAYOUTS) {
            dateRange.setVisibility(GONE);
            lSpinnerType.setVisibility(GONE);

            lmlo.setVisibility(GONE);
            lddv.setVisibility(GONE);


        } else {
            if (materialSpinnerCat.getSelectedIndex() == CAT_DAYS) {
                dateRange.setVisibility(VISIBLE);
                lSpinnerType.setVisibility(VISIBLE);
            } else {
                dateRange.setVisibility(GONE);
                lSpinnerType.setVisibility(VISIBLE);
            }

        }

        if (materialSpinnerCat.getSelectedIndex() == CAT_MONTHS) {
            setMonths(true);
        } else {
            setMonths(false);
        }

    }

    public void setFrom(String date) {
        setDateFrom(date);
        txtFrom.setText("..From .." + DateTimeUtils.Companion.getDisplayDate(date, DateTimeUtils.Companion.getDisplayDatePattern1()));
    }

    public void setTo(String date) {
        setDateTo(date);
        txtTo.setText("..To " + DateTimeUtils.Companion.getDisplayDate(date, DateTimeUtils.Companion.getDisplayDatePattern1()));
    }


    public int getWhichType() {
        return materialSpinnerType.getSelectedIndex();
    }

    public int getWhichCat() {
        return materialSpinnerCat.getSelectedIndex();
    }

    public MonthsDates getWhichMonth() {
        return monthsDates.get(materialSpinnerMonths.getSelectedIndex());
    }
}
