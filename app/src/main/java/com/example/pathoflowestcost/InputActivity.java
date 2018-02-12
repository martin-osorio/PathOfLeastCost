package com.example.pathoflowestcost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Simple activity to allow manual testing of the Pathfinder
 */
public class InputActivity extends AppCompatActivity {
    public EditText inputET;
    public TextView outputTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        inputET = (EditText) findViewById(R.id.et_input);
        outputTV = (TextView) findViewById(R.id.et_output);
    }

    /**
     * When the button in the activity is tapped,
     * use Pathfinder to find the path,
     * Then output that to a text view
     *
     * @param v
     */
    public void onClick(View v) {
        String input = inputET.getText().toString();
        outputTV.setText(Pathfinder.findPath(input).toString());
    }
}
