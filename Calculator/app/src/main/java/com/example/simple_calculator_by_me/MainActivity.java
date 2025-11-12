package com.example.simple_calculator_by_me;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView result;
    private String operator = ""; // Lưu phép toán hiện tại

    private Button num0Button, num1Button, num2Button, num3Button, num4Button, num5Button, num6Button, num7Button, num8Button, num9Button;
    private Button addButton, subtractButton, multiplyButton, divideButton, equalButton, dotButton, allClearButton, plusMinusButton, percentButton;

    private double num1 = 0;
    private double num2 = 0;
    private boolean isAddition, isSubtraction, isMultiplication, isDivision;

    // Hàm tiện ích để xử lý việc nhập số
    private void appendNumber(String number) {
        String currentText = editText.getText().toString();
        if (currentText.equals("0")) {
            editText.setText(number);
        } else {
            editText.setText(currentText + number);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        editText = findViewById(R.id.editText2);
        result = findViewById(R.id.result);
        allClearButton = findViewById(R.id.all_clear);
        addButton = findViewById(R.id.add);
        subtractButton = findViewById(R.id.subtract);
        multiplyButton = findViewById(R.id.multiply);
        divideButton = findViewById(R.id.div);
        equalButton = findViewById(R.id.equal);
        dotButton = findViewById(R.id.dot);
        plusMinusButton = findViewById(R.id.button12); // Nút +/-
        percentButton = findViewById(R.id.button13);   // Nút %

        num0Button = findViewById(R.id.num0);
        num1Button = findViewById(R.id.num1);
        num2Button = findViewById(R.id.num2);
        num3Button = findViewById(R.id.num3);
        num4Button = findViewById(R.id.num4);
        num5Button = findViewById(R.id.num5);
        num6Button = findViewById(R.id.num6);
        num7Button = findViewById(R.id.num7);
        num8Button = findViewById(R.id.num8);
        num9Button = findViewById(R.id.num9);

        // Đặt giá trị ban đầu
        editText.setText("0");
        result.setText("Result:"); // Đặt giá trị mặc định cho result

        // --- SỰ KIỆN CHO CÁC NÚT SỐ ---
        num0Button.setOnClickListener(v -> {
            if (!editText.getText().toString().equals("0")) {
                appendNumber("0");
            }
        });
        num1Button.setOnClickListener(v -> appendNumber("1"));
        num2Button.setOnClickListener(v -> appendNumber("2"));
        num3Button.setOnClickListener(v -> appendNumber("3"));
        num4Button.setOnClickListener(v -> appendNumber("4"));
        num5Button.setOnClickListener(v -> appendNumber("5"));
        num6Button.setOnClickListener(v -> appendNumber("6"));
        num7Button.setOnClickListener(v -> appendNumber("7"));
        num8Button.setOnClickListener(v -> appendNumber("8"));
        num9Button.setOnClickListener(v -> appendNumber("9"));

        // --- SỰ KIỆN CHO CÁC NÚT CHỨC NĂNG ---
        allClearButton.setOnClickListener(v -> {
            editText.setText("0");
            result.setText("Result:"); // Reset về trạng thái ban đầu
            num1 = 0;
            num2 = 0;
            isAddition = false;
            isSubtraction = false;
            isMultiplication = false;
            isDivision = false;
        });

        dotButton.setOnClickListener(v -> {
            if (!editText.getText().toString().contains(".")) {
                editText.setText(editText.getText().toString() + ".");
            }
        });

        // --- CÁC PHÉP TOÁN ---
        View.OnClickListener operationListener = v -> {
            String currentText = editText.getText().toString();
            if (currentText.isEmpty() || currentText.equals("Error")) return;

            num1 = Double.parseDouble(currentText);

            operator = "";
            if (v.getId() == R.id.add) {
                isAddition = true;
                operator = "+";
            } else if (v.getId() == R.id.subtract) {
                isSubtraction = true;
                operator = "-";
            } else if (v.getId() == R.id.multiply) {
                isMultiplication = true;
                operator = "*";
            } else if (v.getId() == R.id.div) {
                isDivision = true;
                operator = "/";
            }

            // Thay vì reset "0", append ký tự toán
            editText.setText(currentText + operator);
        };


        addButton.setOnClickListener(operationListener);
        subtractButton.setOnClickListener(operationListener);
        multiplyButton.setOnClickListener(operationListener);
        divideButton.setOnClickListener(operationListener);

        equalButton.setOnClickListener(v -> {
            String currentText = editText.getText().toString();
            String num2Text = currentText.substring(currentText.lastIndexOf(operator) + 1);

            if (num2Text.isEmpty() || num2Text.equals("Error")) {
                return;
            }
            if (!isAddition && !isSubtraction && !isMultiplication && !isDivision) {
                return;
            }

            num2 = Double.parseDouble(num2Text);
            double calcResult = 0;
            String resultString = "";

            if (isAddition) calcResult = num1 + num2;
            else if (isSubtraction) calcResult = num1 - num2;
            else if (isMultiplication) calcResult = num1 * num2;
            else if (isDivision) {
                if (num2 != 0) {
                    calcResult = num1 / num2;
                } else {
                    result.setText("Result: Error"); // Xử lý lỗi chia cho 0
                    editText.setText("");
                    return;
                }
            }

            // Kiểm tra kết quả là số nguyên hay số thực để hiển thị cho đẹp
            if (calcResult == (long) calcResult) {
                resultString = String.format("%d", (long) calcResult);
            } else {
                resultString = String.format("%s", calcResult);
            }

            result.setText("Result: " + resultString);

            // Đặt kết quả lên editText để có thể tính tiếp
            editText.setText(resultString);

            // Reset trạng thái
            isAddition = false;
            isSubtraction = false;
            isMultiplication = false;
            isDivision = false;
        });

        // Xử lý nút +/-
        plusMinusButton.setOnClickListener(v -> {
            String currentText = editText.getText().toString();
            if (!currentText.isEmpty() && !currentText.equals("0")) {
                if (currentText.startsWith("-")) {
                    editText.setText(currentText.substring(1));
                } else {
                    editText.setText("-" + currentText);
                }
            }
        });

        // Xử lý nút %
        percentButton.setOnClickListener(v -> {
            String currentText = editText.getText().toString();
            if (!currentText.isEmpty()) {
                double value = Double.parseDouble(currentText) / 100;
                editText.setText(String.valueOf(value));
            }
        });
    }
}
