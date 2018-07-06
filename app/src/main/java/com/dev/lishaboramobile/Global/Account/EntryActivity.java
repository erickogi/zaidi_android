package com.dev.lishaboramobile.Global.Account;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dev.lishaboramobile.Global.Utils.NetworkUtils;
import com.dev.lishaboramobile.R;
import com.hbb20.CountryCodePicker;
import com.transitionseverywhere.ArcMotion;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionManager;

public class EntryActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    TextInputEditText editTextCarrierNumber;LinearLayout linearLayout2,linearLayout1;
    ImageView imageView;
    GridLayout gridView;
    private boolean isImageVisible=true;
    private RelativeLayout relativeLayoutParent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        final ViewGroup transitionsContainer = (ViewGroup) findViewById(R.id.transitions_container);
        linearLayout2=findViewById(R.id.linear2);
        imageView=findViewById(R.id.logo);
        gridView=findViewById(R.id.grid);
        gridView.setVisibility(View.GONE);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        relativeLayoutParent=findViewById(R.id.relative_parent);
        ccp.setCcpClickable(false);

        editTextCarrierNumber =  findViewById(R.id.editText_carrierNumber);
//        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
//
//        ccp.setPhoneNumberValidityChangeListener(isValidNumber -> {
//            // your code
//            if(!isValidNumber){
//                editTextCarrierNumber.setError("Invalid");
//            }
//        });

        editTextCarrierNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(transitionsContainer,
                        new ChangeBounds().setPathMotion(new ArcMotion()).setDuration(500));

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
                params.gravity =(Gravity.LEFT | Gravity.TOP);
                linearLayout2.setLayoutParams(params);
                imageView.setVisibility(View.GONE);
                isImageVisible=false;
                //gridView.setVisibility(View.VISIBLE);

            }
        });

        editTextCarrierNumber.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               // if(s.length()>0&&editTextCarrierNumber.getText().toString().charAt(0)!='0') {
                    if (s.length() > 0 && (s.length() % 4) == 0) {
                        final char c = s.charAt(s.length() - 1);
                        if (space == c) {
                            s.delete(s.length() - 1, s.length());
                        }

                    }
                    // Insert char where needed.
                    if (s.length() > 0 && (s.length() % 4) == 0) {
                        char c = s.charAt(s.length() - 1);
                        // Only if its a digit where there should be a space we insert a space
                        if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                            s.insert(s.length() - 1, String.valueOf(space));
                        }

                    }
//                }else {
//                    if(s.length()==4){
//                        final char c = s.charAt(s.length() - 1);
//                        if (space == c) {
//                            s.delete(s.length() - 1, s.length());
//                        }
//                        //s.insert(s.length() , String.valueOf(space));
//                        String no=editTextCarrierNumber.getText().toString()+space;
//                        editTextCarrierNumber.setText(no);
//                        editTextCarrierNumber.setSelection(editTextCarrierNumber.length());
//
//                    }
//                    if(s.length()==8){
//                        //s.insert(s.length() , String.valueOf(space));
//                        String no=editTextCarrierNumber.getText().toString()+space;
//                        editTextCarrierNumber.setText(no);
//                        editTextCarrierNumber.setSelection(editTextCarrierNumber.length());
//                    }
//
//
//
//                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(isImageVisible) {
            super.onBackPressed();
        }else {
            isImageVisible=true;
            imageView.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        }
    }
    private static boolean isValidPhoneNumber(String mobile) {
        Log.d("enteredPhone",mobile);
        String regEx = "^[0-9]{9}$";
        return mobile.matches(regEx);
    }
    public void numberClicked(int a){
        String n=editTextCarrierNumber.getText().toString();
        editTextCarrierNumber.setText(n+String.valueOf(a));
        editTextCarrierNumber.setSelection(editTextCarrierNumber.length());

    }
    public void clearClicked(){
        if(editTextCarrierNumber.getText()!=null&&editTextCarrierNumber.getText().toString().length()>0) {
            String n = editTextCarrierNumber.getText().toString();
            StringBuilder sb = new StringBuilder(n);
            editTextCarrierNumber.setText(sb.deleteCharAt(n.length() - 1).toString());
            editTextCarrierNumber.setSelection(editTextCarrierNumber.length());

        }
    }


    public void next(View view) {

        if(!TextUtils.isEmpty(editTextCarrierNumber.getText().toString())) {
            String phoneNumber = editTextCarrierNumber.getText().toString().replaceAll(" ", "").trim();

            String phone="";

            StringBuilder sb=new StringBuilder(phoneNumber);
            if(sb.charAt(0)=='0'){

            }
            if(isValidPhoneNumber(sb.toString()) ){

                if (NetworkUtils.Companion.isConnected(this)) {
                    checkPhone();
                }
            }else {
                editTextCarrierNumber.setError("Invalid Phone ");
            }
        }
    }

    private void checkPhone() {

    }

    public void clear(View view) {
        clearClicked();
    }

    public void zero(View view) {
        numberClicked(0);
    }

    public void nine(View view) {
        numberClicked(9);

    }

    public void eight(View view) {
        numberClicked(8);

    }

    public void six(View view) {
        numberClicked(6);

    }

    public void seven(View view) {
        numberClicked(7);

    }

    public void five(View view) {
        numberClicked(5);

    }

    public void four(View view) {
        numberClicked(4);

    }

    public void three(View view) {
        numberClicked(3);

    }

    public void two(View view) {
        numberClicked(2);

    }

    public void one(View view) {
        numberClicked(1);

    }

    private void snack(String msg) {
        Snackbar.make(relativeLayoutParent, msg, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
