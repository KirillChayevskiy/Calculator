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


import company.kch.calculator.parser.MatchParser;

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
    boolean bktOpen = false;
    boolean power = false;
    String positiveString;
    int funcCounter = 0;



    MatchParser parser = new MatchParser();

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

        //Кнопка удаления последнего символа
        OnClickListener onClickDelete = new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (textView.getText().toString().contains("E") || textView.getText().toString().contains(getString(R.string.error))) {
                    deleteResult();
                } else {
                    deleteSymbol(textView);
                    if (result) {
                        result = false;
                        textView2.setText("");
                    }
                }
            }
        };

        buttonDelete.getId();
        buttonDelete.setOnClickListener(onClickDelete);

        //Кнопка удаления последнего символа, если долго нажата
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

        //Кнопка запятая
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

        //Кнопка ±
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

        //Кнопка (
        OnClickListener onClickBkt1 = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result) {
                    textView2.setText("");
                    funcCounter = 0;
                }

                textView2.setText(textView2.getText() + "(");
                buttonAClicked = false;
                bktOpen = true;

            }
        };
        buttonBkt1.getId();
        buttonBkt1.setOnClickListener(onClickBkt1);

        //Кнопка )
        OnClickListener onClickBkt2 = new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!result) {
                    textView2.setText(textView2.getText() + "" + textView.getText() + ")");
                    textView.setText("");
                    bktOpen = false;
                    buttonAClicked = false;
                }


            }
        };
        buttonBkt2.getId();
        buttonBkt2.setOnClickListener(onClickBkt2);

        //Кнопка равенства
        OnClickListener onClickEquality = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView2.getText().length() != 0) {
                    if (!result) textView2.setText(textView2.getText() + "" + textView.getText());
                    try {
                        //Проверка на закрытость скобок
                        String resultString = textView2.getText().toString();
                        int countBtkOpen = 0, countBtkClose = 0, difference;
                        for (char element : resultString.toCharArray()){
                            if (element == '(') countBtkOpen++;
                            if (element == ')') countBtkClose++;
                        }

                        //Изменение строки, если количество открытых скобок не равно количеству закрытых
                        difference = countBtkOpen-countBtkClose;
                        if (difference > 0) {
                            for (int i = 0; i < difference; i++) {
                                textView2.setText(textView2.getText() + ")");
                            }
                        }
                        if (difference < 0) {
                            for (int i = 0; i > difference; i--) {
                                textView2.setText("(" + textView2.getText());
                            }
                        }
                        textView2.setText(correctString(textView2.getText().toString()));
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

                    // Если большое число , то ставим в конец букву E
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

        //Кнопка %
        OnClickListener onClickPercent = new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (textView.getText().length() != 0) {
                    MatchParser parserPercent = new MatchParser();
                    try {
                        StringBuilder stringBuffer = new StringBuilder(textView2.getText().toString());
                        stringBuffer.delete(textView2.getText().length() - 1, textView2.getText().length());
                        textView.setText("" + parserPercent.Parse(correctString("(" + stringBuffer.toString() + ")×0.01×" + textView.getText()), 10));
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
                }
            }
        };

        buttonPercent.getId();
        buttonPercent.setOnClickListener(onClickPercent);

        // Кнопка C
        OnClickListener onClickClear = new OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteResult();
            }
        };
        buttonClear.getId();
        buttonClear.setOnClickListener(onClickClear);

        // Кнопка Pi
        OnClickListener onClickPi = new OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(parser.getPI() + "");
                buttonAClicked = false;
            }
        };
        buttonPi.getId();
        buttonPi.setOnClickListener(onClickPi);

        // Кнопка E
        OnClickListener onClickE = new OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(parser.getE() + "");
                buttonAClicked = false;
            }
        };
        buttonE.getId();
        buttonE.setOnClickListener(onClickE);

        //Кнопка ^
        OnClickListener onClickPower = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText().length() != 0 && !textView.getText().equals("-")) {
                    if (buttonAClicked) {
                        deleteSymbol(textView2);
                        textView2.setText(textView2.getText() + "^(");
                        power = true;
                        buttonAClicked = false;
                    } else if (textView.getText().toString().contains(getString(R.string.error))) {
                        deleteResult();
                    } else if (result && !buttonAClicked) {
                        textView2.setText(textView2.getText() + "^(");
                        funcCounter = 0;
                        result = false;
                        preNum = true;
                    } else {
                        clearValue();
                        textView2.setText(textView2.getText() + "" + textView.getText() + "^(");
                        preNum = true;
                        power = true;
                    }
                    buttonPercent.setEnabled(true);
                    buttonBkt1.setEnabled(true);
                    buttonBkt2.setEnabled(true);
                    scroller(horizontalScrollView2);
                }
            }
        };
        buttonPower.getId();
        buttonPower.setOnClickListener(onClickPower);
    }

    //Функциональные кнопки калькулятора
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

    //Удаление последнего символа в TextView
    public void deleteSymbol(TextView tV) {
        StringBuilder stringBuffer = new StringBuilder(tV.getText().toString());
        if (stringBuffer.length() != 0) {
            stringBuffer.delete(tV.getText().length() - 1, tV.getText().length());
            tV.setText(stringBuffer.toString());

        }
    }

    // Очистка нижнего поля от лишних нулей и точек
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

    //Текстовое уведомление
    public void showToast(String message) {
        //создаем и отображаем текстовое уведомление
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 80);
        toast.show();
    }

    //Исправление строки
    public String correctString (String string){
        string = string.replace(")(", ")×(");
        string = string.replace("()", "(0)");
        string = string.replace("--", "+");
        string = string.replace("+-", "-");
        String[] func = {"c", "s", "t", "l"};
        for (int i = 0; i <10; i++){
            string = string.replace(i + "(", i + "×(" );
            string = string.replace(")" + i, ")×" + i );
        }
        for (int f = 0; f <4; f++){
            string = string.replace(")" + func[f], ")×" + func[f]);
            for (int i = 0; i <10; i++){
                string = string.replace(i + "" + func[f], i + "×" + func[f]);
            }
        }
        return string;
    }

    //Очистка результата
    public void deleteResult () {
            textView.setText("");
            textView2.setText("");
            buttonAClicked = false;
            result = false;
            preNum = false;
            power = false;
            negativeNum = false;
            buttonBkt1.setEnabled(true);
            buttonBkt2.setEnabled(true);
            funcCounter = 0;
            buttonPercent.setEnabled(true);
    }

    //Кнопки 0..9
    public void onClickButtonNumber(View view) {
        if (result) {
            textView.setText("");
            textView2.setText("");
            funcCounter = 0;
            result = false;
            buttonPercent.setEnabled(true);
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
    }

    //Кнопки +, -, *, /
    public void onClickButtonAction(View view) {
        clearValue();
        if (textView2.getText().length() != 0 || textView.getText().length() != 0) {
            String string = (String) ((Button)view).getText();
            if (textView.getText().toString().contains(getString(R.string.error))) {
                deleteResult();
            } else if (textView2.getText().length() != 0) {
                if (buttonAClicked && textView2.getText().length() >= 2 && (textView2.getText().toString().substring(textView2.getText().length() - 1, textView2.getText().length()).equals("+") ||
                        textView2.getText().toString().substring(textView2.getText().length() - 1, textView2.getText().length()).equals("-") ||
                        textView2.getText().toString().substring(textView2.getText().length() - 1, textView2.getText().length()).equals("×") ||
                        textView2.getText().toString().substring(textView2.getText().length() - 1, textView2.getText().length()).equals("÷") ||
                        textView2.getText().toString().substring(textView2.getText().length() - 2, textView2.getText().length() ).equals("^("))) {
                    if (power) {
                        deleteSymbol(textView2);
                        power = false;
                    }
                    deleteSymbol(textView2);
                    textView2.setText(textView2.getText() + "" + string);
                    preNum = true;
                } else if (result) {
                    textView2.setText(textView2.getText() + "" + string);
                    textView.setText("");
                    result = false;
                    preNum = true;
                    funcCounter = 0;
                } else {
                    for (int i = 0; i <10; i++) {
                        if (textView2.getText().toString().substring(textView2.getText().length() - 1, textView2.getText().length()).equals(i + "")) {
                            textView2.setText(textView2.getText() + string);
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
            buttonBkt1.setEnabled(true);
            buttonBkt2.setEnabled(true);
            scroller(horizontalScrollView2);
        }

    }

    //Скрол в конец строки
    public void scroller (final HorizontalScrollView horizontalScroll){
        horizontalScroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                horizontalScroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);
    }
}