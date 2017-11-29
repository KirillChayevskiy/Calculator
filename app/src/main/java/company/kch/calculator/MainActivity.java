package company.kch.calculator;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import company.kch.calculator.parser.MathParser;

public class MainActivity extends AppCompatActivity {

    TextView textView, textView2;
    Button buttonComma, buttonPlusMinus;
    Button buttonPower, buttonEquality;
    Button buttonClear, buttonPercent;
    Button buttonBkt1, buttonBkt2;
    Button buttonRadical, buttonDenominator, buttonSin, buttonCos, buttonTan, buttonLog, buttonLn;
    Button buttonPi, buttonE;
    HorizontalScrollView horizontalScrollView, horizontalScrollView2;
    ImageButton buttonDelete;

    boolean buttonAClicked = false;
    boolean result = false;
    boolean preNum = false;
    boolean negativeNum = false;
    boolean power = false;
    String positiveString;
    int funcCounter = 0;



    MathParser parser = new MathParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        horizontalScrollView2 = (HorizontalScrollView) findViewById(R.id.horizontalScrollView2);
        buttonComma = (Button) findViewById(R.id.buttonComma);
        buttonPlusMinus = (Button) findViewById(R.id.buttonPlusMinus);
        buttonEquality = (Button) findViewById(R.id.buttonEquality);
        buttonPercent = (Button) findViewById(R.id.buttonPercent);
        buttonPower = (Button) findViewById(R.id.buttonPower);
        buttonRadical = (Button) findViewById(R.id.buttonRadical);
        buttonDenominator = (Button) findViewById(R.id.buttonDenominator);
        buttonSin = (Button) findViewById(R.id.buttonSin);
        buttonCos = (Button) findViewById(R.id.buttonCos);
        buttonTan = (Button) findViewById(R.id.buttonTan);
        buttonLog = (Button) findViewById(R.id.buttonLog);
        buttonLn = (Button) findViewById(R.id.buttonLn);
        buttonPi = (Button) findViewById(R.id.buttonPi);
        buttonE = (Button) findViewById(R.id.buttonE);
        buttonBkt1 = (Button) findViewById(R.id.buttonBkt1);
        buttonBkt2 = (Button) findViewById(R.id.buttonBkt2);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonDelete = (ImageButton) findViewById(R.id.buttonDelete);

        clickButtonF(buttonCos, getString(R.string.button_Cos));
        clickButtonF(buttonSin, getString(R.string.button_Sin));
        clickButtonF(buttonTan, getString(R.string.button_Tan));
        clickButtonF(buttonLn, getString(R.string.button_Ln));
        clickButtonF(buttonLog, getString(R.string.button_Log));
        clickButtonF(buttonRadical, "sqrt");
        clickButtonF(buttonDenominator, "1÷");

        OnClickListener onClickDelete = new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (textView.getText().toString().contains("E") || textView.getText().toString().contains(getString(R.string.error))) {
                    deleteResult();
                } else  if (textView.getText().length() != 0){
                    deleteSymbol(textView);
                    if (result) {
                        result = false;
                        textView2.setText("");
                    }
                } else if (textView2.getText().length() > 0){
                    deleteFromTopView();
                }
            }
        };

        buttonDelete.getId();
        buttonDelete.setOnClickListener(onClickDelete);

        View.OnLongClickListener onLongClickDelete = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                textView.setText("");
                if (result) {
                    result = false;
                    textView2.setText("");
                }
                return false;
            }
        };
        buttonDelete.getId();
        buttonDelete.setOnLongClickListener(onLongClickDelete);

        OnClickListener onClickComma = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText().toString().contains(getString(R.string.error))) {
                    deleteResult();
                }
                if (!textView.getText().toString().contains(".") && textView.getText().length() < 12 && !textView.getText().equals("-")) {
                    if (textView.getText().length() == 0) {
                        textView.setText("0.");
                    } else {
                        textView.setText(textView.getText() + ".");
                        if (result) {
                            result = false;
                            textView2.setText("");
                            funcCounter = 0;
                        }
                        scroller(horizontalScrollView);
                    }
                }
            }
        };
        buttonComma.getId();
        buttonComma.setOnClickListener(onClickComma);

        OnClickListener onClickPlusMinus = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText().toString().contains(getString(R.string.error))) {
                    deleteResult();
                }
                positiveString = textView.getText().toString().replace("-" , "");
                if (!negativeNum && !textView.getText().toString().contains("-")) {
                    textView.setText("-" + positiveString);
                    negativeNum = true;
                } else {
                    textView.setText(positiveString);
                    negativeNum = false;
                }
                if (result) {
                    result = false;
                    textView2.setText("");
                    funcCounter = 0;
                }

            }
        };
        buttonPlusMinus.getId();
        buttonPlusMinus.setOnClickListener(onClickPlusMinus);

        OnClickListener onClickBkt1 = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result) {
                    textView2.setText("");
                    funcCounter = 0;
                }

                textView2.setText(textView2.getText() + "(");
                buttonAClicked = false;

            }
        };
        buttonBkt1.getId();
        buttonBkt1.setOnClickListener(onClickBkt1);

        OnClickListener onClickBkt2 = new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!result) {
                    textView2.setText(textView2.getText() + "" + textView.getText() + ")");
                    textView.setText("");
                    buttonAClicked = false;
                    power = false;
                }


            }
        };
        buttonBkt2.getId();
        buttonBkt2.setOnClickListener(onClickBkt2);

        OnClickListener onClickEquality = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView2.getText().length() != 0) {
                    if (!result) textView2.setText(textView2.getText() + "" + textView.getText());
                    try {
                        String resultString = textView2.getText().toString();
                        int countBtkOpen = 0, countBtkClose = 0, difference;
                        for (char element : resultString.toCharArray()){
                            if (element == '(') countBtkOpen++;
                            if (element == ')') countBtkClose++;
                        }

                        //dd on ss code not available - - - - -
                        textView2.setText(correctString(textView2.getText().toString()));
                        if (checkLast(getString(R.string.button_Plus)) || checkLast(getString(R.string.button_Minus)) || checkLast(getString(R.string.button_Multiply)) || checkLast(getString(R.string.button_Devide))) {
                            textView2.setText(textView2.getText().toString().substring(0,  textView2.getText().length() - 1));
                        }
                        textView.setText("" + parser.Parse(textView2.getText().toString(), 10));
                    } catch (Exception e) {
                        textView.setText(getString(R.string.error));
                    }
                    result = true;
                    clearValue();
                    if (textView.getText().equals("0E-10")) {
                        textView.setText("0");
                    }
                    buttonAClicked = false;
                    power = false;

                    if (textView.getText().length() > 13) {
                        int count = 0;
                        if (textView.getText().toString().contains(".")) {
                            int ooo = 0;
                            while (textView.getText().length() > 13) {
                                try {
                                    textView.setText("" + parser.Parse(textView.getText().toString(), (10-ooo)));
                                } catch (Exception ignored) {
                                }
                                clearValue();
                                ooo++;
                            }
                            showToast(getString(R.string.toast_RoundingNumber));
                        } else {
                            while (textView.getText().length() > 13) {
                                deleteSymbol(textView);
                                count++;
                            }
                            for (int i = 0; i < 12; i++) {
                                try {
                                    textView.setText("" + parser.Parse(correctString(textView.getText().toString() + "÷10"), 10));
                                } catch (Exception ignored) {}
                            }
                            while (textView.getText().length() > 6) {
                                deleteSymbol(textView);
                            }
                            textView.setText(textView.getText() + "E" + (count + 12));
                        }
                    }
                    scroller(horizontalScrollView);
                    scroller(horizontalScrollView2);
                }
            }
        };
        buttonEquality.getId();
        buttonEquality.setOnClickListener(onClickEquality);

        OnClickListener onClickPercent = new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (textView.getText().length() != 0) {
                    MathParser parserPercent = new MathParser();
                    try {
                        StringBuilder stringBuffer = new StringBuilder(textView2.getText().toString());
                        stringBuffer.delete(textView2.getText().length() - 1, textView2.getText().length());
                        textView.setText("" + parserPercent.Parse(correctString(stringBuffer.toString() + "×0.01×" + textView.getText()), 10));
                        clearValue();
                        if (textView.getText().length() > 13) {
                            int count = 0;
                            while (textView.getText().length() > 13) {
                                deleteSymbol(textView);
                                count++;
                            }
                            if (textView.getText().toString().contains(".")) {
                                clearValue();
                            } else {
                                for (int i = 0; i < 12; i++) {
                                    try {
                                        textView.setText("" + parser.Parse(correctString(textView.getText().toString() + "÷10"), 10));
                                    } catch (Exception ignored) {}
                                }
                                while (textView.getText().length() > 6) {
                                    deleteSymbol(textView);
                                }
                                textView.setText(textView.getText() + "E" + (count + 12));
                            }
                        }
                        textView2.setText(correctString(textView2.getText() + "" + textView.getText()));
                        textView.setText("");
                        buttonPercent.setEnabled(false);
                    } catch (Exception e) {
                        textView.setText("");
                    }
                    buttonAClicked = false;
                    result = true;
                }
            }
        };
        buttonPercent.getId();
        buttonPercent.setOnClickListener(onClickPercent);

        OnClickListener onClickClear = new OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteResult();
            }
        };
        buttonClear.getId();
        buttonClear.setOnClickListener(onClickClear);

        OnClickListener onClickPi = new OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(parser.getPI() + "");
                buttonAClicked = false;
            }
        };
        buttonPi.getId();
        buttonPi.setOnClickListener(onClickPi);

        OnClickListener onClickE = new OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(parser.getE() + "");
                buttonAClicked = false;
            }
        };
        buttonE.getId();
        buttonE.setOnClickListener(onClickE);

        OnClickListener onClickPower = new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearValue();
                if (textView2.getText().length() != 0 || textView.getText().length() != 0) {
                    if (buttonAClicked) {
                        deleteSymbol(textView2);
                        textView2.setText(textView2.getText() + "^(");
                        power = true;
                        buttonAClicked = false;
                    } else if (textView.getText().toString().contains(getString(R.string.error))) {
                        deleteResult();
                    } else if (result) {
                        textView2.setText(textView2.getText() + "^(");
                        funcCounter = 0;
                        result = false;
                        preNum = true;
                    } else {
                        textView2.setText(textView2.getText() + "" + textView.getText() + "^(");
                        preNum = true;
                        power = true;
                    }
                    buttonPercent.setEnabled(true);
                    textView.setText("");
                    scroller(horizontalScrollView2);
                }
            }
        };
        buttonPower.getId();
        buttonPower.setOnClickListener(onClickPower);
    }

    public void clickButtonF (final Button button, final String string) {
        OnClickListener onClickFunction = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText().toString().contains(getString(R.string.error))) {
                    deleteResult();
                } else if (funcCounter <= 20) {
                    clearValue();
                    textView2.setText(textView2.getText() + "" + string + "(" + textView.getText());
                    textView.setText("");

                    scroller(horizontalScrollView2);
                    funcCounter++;
                } else {
                    showToast(getString(R.string.toast_Limit));
                }

            }
        };
        button.getId();
        button.setOnClickListener(onClickFunction);
    }
    public void deleteSymbol(TextView tV) {
        StringBuilder stringBuffer = new StringBuilder(tV.getText().toString());
        if (stringBuffer.length() != 0) {
            stringBuffer.delete(tV.getText().length() - 1, tV.getText().length());
            tV.setText(stringBuffer.toString());

        }
    }

    public void deleteFromTopView() {
        if (textView2.getText().length() != 0) {
            if (checkLast("(")){
                deleteSymbol(textView2);
                if (checkLast("g") || checkLast("s")) {
                    for (int i = 0; i < 3; i++){
                        deleteSymbol(textView2);
                    }
                } else if (checkLast("n")) {
                    deleteSymbol(textView2);
                    if (checkLast("l")) {
                        deleteSymbol(textView2);
                    } else {
                        deleteSymbol(textView2);
                        deleteSymbol(textView2);
                    }
                } else if (checkLast("t")) {
                    for (int i = 0; i < 4; i++){
                        deleteSymbol(textView2);
                    }
                } else {
                    deleteSymbol(textView2);
                }
            } else {
                deleteSymbol(textView2);
            }
            if (checkLast(".")) {
                deleteSymbol(textView2);
            }

        }
    }

    public void clearValue (){
        if (textView.getText().equals("-")) textView.setText("");
        if (textView.getText().toString().contains(".")) {
            cleaning:
            while (true) {
                int i = textView.getText().length();
                String s = textView.getText().toString().substring(i - 1, i);
                switch (s) {
                    case "0":
                        deleteSymbol(textView);
                        break;
                    case ".":
                        deleteSymbol(textView);
                        break cleaning;
                    default:
                        break cleaning;
                }
            }
        }
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 80);
        toast.show();
    }
    //test is failed
    //Return to previous version
    public String correctString (String string){


        return string;
    }

    public void deleteResult () {
            textView.setText("");
            textView2.setText("");
            buttonAClicked = false;
            result = false;
            preNum = false;
            power = false;
            negativeNum = false;
            funcCounter = 0;
            buttonPercent.setEnabled(true);
    }

    public void onClickButtonNumber(View view) {
        if (result) {
            deleteResult();
        }
        if (preNum) {
            textView.setText("");
            preNum = false;
        }
        if (textView.getText().length() < 13) {
            if (textView.getText().equals("0") || textView.getText().equals("-0")) {
                textView.setText(((Button)view).getText());
            } else {
                textView.setText(textView.getText().toString() + ((Button)view).getText());
            }
            buttonAClicked = false;
        }
        scroller(horizontalScrollView);
        power = false;
    }

    public void onClickButtonAction(View view) {
        clearValue();
        if (textView2.getText().length() != 0 || textView.getText().length() != 0) {
            String string = (String) ((Button)view).getText();
            if (textView.getText().toString().contains(getString(R.string.error))) {
                deleteResult();
            } else if (textView2.getText().length() != 0) {
                if (power && textView.getText().length() == 0) {
                    deleteSymbol(textView2);
                    deleteSymbol(textView2);
                    power = false;
                }
                if (buttonAClicked && textView2.getText().length() >= 1 && (checkLast(getString(R.string.button_Plus)) || checkLast(getString(R.string.button_Minus)) || checkLast(getString(R.string.button_Multiply)) || checkLast(getString(R.string.button_Devide)))) {
                    deleteSymbol(textView2);
                    textView2.setText(textView2.getText() + "" + string);
                    textView.setText("");
                    preNum = true;
                } else if (result) {
                    textView2.setText(textView2.getText() + "" + string);
                    result = false;
                    preNum = true;
                    funcCounter = 0;
                } else {
                    for (int i = 0; i <10; i++) {
                        if (textView2.getText().toString().substring(textView2.getText().length() - 1, textView2.getText().length()).equals(i + "")) {
                            textView2.setText(textView2.getText() + string);
                            buttonPercent.setEnabled(true);
                            scroller(horizontalScrollView2);
                            return;
                        }
                    }
                    if (textView.getText().length() != 0) {
                        textView2.setText(textView2.getText() + "" + textView.getText() + string);
                    } else {
                        textView2.setText(textView2.getText() + string);
                    }
                    preNum = true;
                }
            } else {
                textView2.setText(textView.getText() + "" + string);
                preNum = true;
            }
            buttonPercent.setEnabled(true);
            buttonAClicked = true;
            scroller(horizontalScrollView2);
        }
    }

    public void scroller (final HorizontalScrollView horizontalScroll){
        horizontalScroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                horizontalScroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textView.setText(savedInstanceState.getString("textView"));
        textView2.setText(savedInstanceState.getString("textView2"));
        positiveString = savedInstanceState.getString("positiveString");
        buttonAClicked = savedInstanceState.getBoolean("buttonAClicked");
        result = savedInstanceState.getBoolean("result");
        preNum = savedInstanceState.getBoolean("preNum");
        negativeNum = savedInstanceState.getBoolean("negativeNum");
        power = savedInstanceState.getBoolean("power");
        funcCounter = savedInstanceState.getInt("funcCounter");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("textView", textView.getText().toString());
        outState.putString("textView2", textView2.getText().toString());
        outState.putString("positiveString", positiveString);
        outState.putBoolean("buttonAClicked", buttonAClicked);
        outState.putBoolean("result", result);
        outState.putBoolean("preNum", preNum);
        outState.putBoolean("negativeNum", negativeNum);
        outState.putBoolean("power", power);
        outState.putInt("funcCounter", funcCounter);
    }

    public boolean checkLast (String symbol) {
        int l = textView2.getText().length();
        if (textView2.getText().length() != 0) {
            return textView2.getText().toString().substring(l - 1, l).equals(symbol);
        } else return false;
    }
}