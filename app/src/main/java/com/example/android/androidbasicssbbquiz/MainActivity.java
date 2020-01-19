package com.example.android.androidbasicssbbquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String name;
    private String email;
    private boolean emailSummaryRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void submitQuiz(View view) {
        if (view.getTag().equals(1)) {
            resetQuiz();
        } else {
            processQuiz();
        }
    }

    /**
     * Instead of resetting all the checkboxes and radioboxes and textfields, it
     * is much easier to simple restart the application.
     */
    private void resetQuiz() {
        resetPersonalInformation();
        resetQuestions();
        resetTextView();
    }

    /**
     * This method resets all textViews and checkboxes of the personal part.
     * The hints are re-displayes as soon as the text is set to emtpy.
     */
    private void resetPersonalInformation() {
        TextView nameView = findViewById(R.id.input_text_name);
        nameView.setText("");
        TextView emailView = findViewById(R.id.input_text_email);
        emailView.setText("");
        CheckBox emailCheckBox = findViewById(R.id.checkbox_receive_email);
        emailCheckBox.setChecked(false);
    }

    private void resetQuestions() {
        LinearLayout layout = findViewById(R.id.linear_layout);

        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof CheckBox) {
                ((CheckBox) v).setChecked(false);
            } else if (v instanceof RadioGroup) {
                resetRadioGroup((RadioGroup) v);
            } else if (v instanceof EditText) {
                if(v.getId() == R.id.input_text_question_six
                || v.getId() == R.id.input_text_question_five) {
                    ((EditText) v).setText("");
                }
            }

        }
    }

    /**
     * This method gets the current RadioGroup and finds all it's RadioButton
     * children which it sets to unchecked.
     *
     * @param radioGroup
     */
    private void resetRadioGroup(RadioGroup radioGroup) {
        for(int i = 0; i < radioGroup.getChildCount(); i++) {
            View v = radioGroup.getChildAt(i);
            if(v instanceof RadioButton) {
                ((RadioButton) v).setChecked(false);
            }
        }
    }

    private void resetTextView() {
        TextView summaryView = findViewById(R.id.summary_text_view);
        summaryView.setText("");
        Button submitButton = findViewById(R.id.send_button);
        submitButton.setTag(0);
        submitButton.setText(R.string.submint_button);
    }

    /**
     * Processing the Quiz. Getting personal information, updating the score and sending
     * the email out if requestd.
     */
    private void processQuiz() {
        fetchPersonalInformation();
        int score = updateScoreValuesWithAnswers();
        String message = createMessage(score);
        createAndDisplayToastMessage(score);
        if(emailSummaryRequested) {
            sendOutEmailSummary(message);
        }
        updateSummaryView(message);
        updateSubmitButton();
    }


    /**
     * This method creates a toast of short duration and displays the total
     * score of the user.
     *
     * @param score
     */
    private void createAndDisplayToastMessage(int score) {
        Context context = getApplicationContext();
        CharSequence totalScore = "Yout Total Score is: " + score;
        int durationOfToast = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, totalScore, durationOfToast);
        toast.show();
    }

    /**
     * When the naming of the button changes, the tag of the button must be updated in
     * order to dicriminate between two actions taken by onClick().
     * Changing the Tag to 0 is not necessary since if the test is retaken, the app
     * is restarted and the tag is 0 by default.
     */
    private void updateSubmitButton() {
        Button button = findViewById(R.id.send_button);
        button.setText("ReTake Quiz?");
        button.setTag(1);

    }

    /**
     * This method creates a message from the String ressource and 3 global variables
     *
     * @param score
     * @return
     */
    private String createMessage(int score) {
        String result = (score >= 5) ? "PASSED" : "FAILED";
        String messageWithPlaceholders = getString(R.string.summary_message);
        String message = String.format(messageWithPlaceholders, name, score, result);
        return message;
    }


    /**
     * This method is called if the user checked the summary by email option. It
     * sends the email with pre-defined subject and body.
     *
     * @param message
     */
    private void sendOutEmailSummary(String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+email));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject) + " " + name);
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
     * This method updates the local score variable by going through each question
     * and adding 1 to the score if the question was answered correctly and 0 if it wasn't
     *
     * @return
     */
    private int updateScoreValuesWithAnswers() {
        int score = 0;

        score += fetchResultFirstQuestion();
        score += fetchResultSecondQuestion();
        score += fetchResultThirdQuestion();
        score += fetchResultFourthQuestion();
        score += fetchResultFifthQuestion();
        score += fetchResultSixthQuestion();

        return score;
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
            Log.v("MainActivity", "Question 1: received 1 point");
            return 1;
        }
        Log.v("MainActivity", "Question 1: received 0 point");
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
            Log.v("MainActivity", "Question 2: received 1 point");
            return 1;
        }
        Log.v("MainActivity", "Question 2: received 0 point");
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
            Log.v("MainActivity", "Question 3: received 1 point");
            return 1;
        }
        Log.v("MainActivity", "Question 3: received 0 point");
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
            Log.v("MainActivity", "Question 4: received 1 point");
            return 1;
        }
        Log.v("MainActivity", "Question 4: received 0 point");
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
        String[] correctAnswers = {"switzerland", "schweiz", "helvetia", "swiss", "schwiiz"};
        List<String> listOfCorrectAnswers = Arrays.asList(correctAnswers);
        List<String> listOfGivenAnswers = Arrays.asList(answer.split(" "));
        for(String str : listOfGivenAnswers) {
            if(listOfCorrectAnswers.contains(str)) {
                Log.v("MainActivity", "Question 5: received 1 point");
                return 1;
            }
        }
        Log.v("MainActivity", "Question 5: received 0 point");
        return 0;
    }

    /**
     * This method simply checks if something was entered into the editText field and
     * if sth was entered it returns 1 and 0 otherwise.
     *
     * @return
     */
    private int fetchResultSixthQuestion() {
        EditText editTextField = findViewById(R.id.input_text_question_six);
        String answer = editTextField.getText().toString();
        String[] correctAnswers = {"germany", "deutschland", "dütschland", "france", "frankreich", "frankriich"};
        List<String> listOfCorrectAnswers = Arrays.asList(correctAnswers);
        List<String> listOfGivenAnswers = Arrays.asList(answer.split(" "));
        for(String str : listOfGivenAnswers) {
            if(listOfCorrectAnswers.contains(str)) {
                Log.v("MainActivity", "Question 6: received 1 point");
                return 1;
            }
        }
        Log.v("MainActivity", "Question 6: received 0 point");
        return 0;
    }


}
