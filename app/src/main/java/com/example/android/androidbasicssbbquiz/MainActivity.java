package com.example.android.androidbasicssbbquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String name;
    private String email;
    private boolean emailSummaryRequested = false;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitQuiz(View view) {
        fetchPersonalInformation();
        updateScoreValuesWithAnswers();
        String message = createMessage();
        if(emailSummaryRequested) {
            sendOutEmailSummary(message);
        } else {
            updateSummaryView(message);
        }
    }

    /**
     * This method creates a message from the String ressource and 3 global variables
     *
     * @return
     */
    private String createMessage() {
        String result = (score >= 5) ? "PASSED" : "FAILED";
        String messageWithPlaceholders = getString(R.string.summary_message);
        String message = String.format(messageWithPlaceholders, name, score, result);
        return message;
    }

    private void sendOutEmailSummary(String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject) + name);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * If the user didn't request an email summary, display the results on the screen.
     *
     * @param message
     */
    private void updateSummaryView(String message) {
        TextView view = findViewById(R.id.summary_text_view);
        view.setText(message);
    }

    /**
     * This method updates the global score variable by going through each question
     * and adding 1 to the score if the question was answered correctly and 0 if it wasn't
     */
    private void updateScoreValuesWithAnswers() {
        score += fetchResultFirstQuestion();
        score += fetchResultSecondQuestion();
        score += fetchResultThirdQuestion();
        score += fetchResultFourthQuestion();
        score += fetchResultFifthQuestion();
        score += fetchResultSixthQuestion();
    }

    /**
     * This method retrieves all the entered personal information and
     * stores it into the global variables.
     */
    private void fetchPersonalInformation() {
        TextView nameInputView = findViewById(R.id.input_text_name);
        name = nameInputView.getText().toString();
        TextView emailInputView = findViewById(R.id.input_text_email);
        email = emailInputView.getText().toString();
        CheckBox emailSummaryView = findViewById(R.id.checkbox_receive_email);
        emailSummaryRequested = emailSummaryView.isChecked();
    }

    /**
     * This method checks all CheckBoxes from question one and returns 1 if the
     * right ones have been selected. It returns 0 otherwise.
     *
     *  @return
     */
    private int fetchResultFirstQuestion() {
        CheckBox answerACheckBox = findViewById(R.id.question_one_answer_a);
        CheckBox answerBCheckBox = findViewById(R.id.question_one_answer_b);
        CheckBox answerCCheckBox = findViewById(R.id.question_one_answer_c);
        CheckBox answerDCheckBox = findViewById(R.id.question_one_answer_d);

        if(answerACheckBox.isChecked() && answerBCheckBox.isChecked()
                && answerCCheckBox.isChecked() && !answerDCheckBox.isChecked()) {
            return 1;
        }
        return 0;
    }

    /**
     * This method checks all CheckBoxes from question one and returns 1 if the
     * right ones have been selected. It returns 0 otherwise.
     *
     * @return
     */
    private int fetchResultSecondQuestion() {
        CheckBox answerACheckBox = findViewById(R.id.question_two_answer_a);
        CheckBox answerBCheckBox = findViewById(R.id.question_two_answer_b);
        CheckBox answerCCheckBox = findViewById(R.id.question_two_answer_c);
        CheckBox answerDCheckBox = findViewById(R.id.question_two_answer_d);

        if(answerACheckBox.isChecked() && !answerBCheckBox.isChecked()
                && answerCCheckBox.isChecked() && !answerDCheckBox.isChecked()) {
            return 1;
        }
        return 0;
    }

    /**
     * This method retrieves the correct RadioBox element and checks if it was
     * selected. If so, 1 is returned. If it was not selected 0 is returned.
     *
     * @return
     */
    private int fetchResultThirdQuestion() {
        RadioButton radioButtonAnswer = findViewById(R.id.question_three_answer_c);
        if(radioButtonAnswer.isChecked()) {
            return 1;
        }
        return 0;
    }

    /**
     * This method retrieves the correct RadioBox element and checks if it was
     * selected. If so, 1 is returned. If it was not selected 0 is returned.
     *
     * @return
     */
    private int fetchResultFourthQuestion() {
        RadioButton radioButtonAnswer = findViewById(R.id.question_four_answer_a);
        if(radioButtonAnswer.isChecked()) {
            return 1;
        }
        return 0;
    }

    /**
     * This method simply checks if something was entered into the editText field and
     * if sth was entered it returns 1 and 0 otherwise.
     *
     * @return
     */
    private int fetchResultFifthQuestion() {
        EditText editTextField = findViewById(R.id.input_text_question_five);
        String answer = editTextField.getText().toString();
        if(!answer.isEmpty()) {
            return 1;
        }
        return 0;
    }

    /**
     * This method simply checks if something was entered into the editText field and
     * if sth was entered it returns 1 and 0 otherwise.
     *
     * @return
     */
    private int fetchResultSixthQuestion() {
        EditText editTextField = findViewById(R.id.input_text_question_five);
        String answer = editTextField.getText().toString();
        if(!answer.isEmpty()) {
            return 1;
        }
        return 0;
    }


}
