package ma.fstt.convertisseurdedevise;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "a8fe7a33010d23551fe51741";
    private static final String BASE_API_URL = "https://v6.exchangerate-api.com/v6/";

    private String currencyFrom = "EUR";
    private String currencyTo = "USD";
    private float conversionRate = 0f;
    private EditText inputText;
    private EditText resultText;
    private Button convertBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.input_text);
        resultText = findViewById(R.id.result_text);
        convertBtn = findViewById(R.id.btn_convert);

        spinnerSetup();
        onButton();
    }

    private void fetchConversionRate() {
        if (TextUtils.isEmpty(inputText.getText().toString().trim())) {
            showToast("Please enter a value to convert");
            return;
        }

        Uri.Builder uriBuilder = Uri.parse(BASE_API_URL + API_KEY + "/latest/" + currencyFrom).buildUpon();
        String apiUrl = uriBuilder.build().toString();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                int responseCode = connection.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    handleSuccessfulResponse(connection);
                } else {
                    handleErrorResponse(responseCode);
                }
            } catch (IOException e) {
                handleApiError(e);
            }
        });
    }

    private void handleSuccessfulResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String apiResult = stringBuilder.toString();
            Log.d("MainActivity", "API Result: " + apiResult);
            handleApiResponse(apiResult);
        }
    }

    private void handleErrorResponse(int responseCode) {
        Log.e("MainActivity", "HTTP error code: " + responseCode);
    }

    private void handleApiError(IOException e) {
        Log.e("MainActivity", "Error during API call", e);
    }

    private void handleApiResponse(String apiResult) {
        try {
            JSONObject jsonObject = new JSONObject(apiResult);
            String rateString = jsonObject.getJSONObject("conversion_rates").getString(currencyTo);
            conversionRate = Float.parseFloat(rateString);

            new Handler(Looper.getMainLooper()).post(() -> {
                float value = Float.parseFloat(inputText.getText().toString());
                Log.d("MainActivity", "Input Text Value: " + value);

                runOnUiThread(() -> {
                    String text = Float.toString(value * conversionRate);
                    if (resultText != null) {
                        resultText.setText(text);
                        Log.d("MainActivity", "Result Text Set: " + text);
                    }
                });
            });
        } catch (JSONException e) {
            Log.e("MainActivity", "Error parsing JSON", e);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void spinnerSetup() {
        Spinner spinnerFrom = findViewById(R.id.spinner_from);
        Spinner spinnerTo = findViewById(R.id.spinner_to);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.currencies2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(adapter2);

        setupSpinnerListener(spinnerFrom, () -> currencyFrom = spinnerFrom.getSelectedItem().toString());
        setupSpinnerListener(spinnerTo, () -> currencyTo = spinnerTo.getSelectedItem().toString());
    }

    private void setupSpinnerListener(Spinner spinner, Runnable onItemSelected) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO: Handle the scenario when nothing is selected
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelected.run();
            }
        });
    }

    public void onButton() {
        convertBtn.setOnClickListener(
                view -> {
                    try {
                        fetchConversionRate();
                    } catch (Exception e) {
                        showToast("An error occurred");
                    }
                }
        );
    }
}
