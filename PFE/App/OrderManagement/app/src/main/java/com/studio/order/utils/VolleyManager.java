package com.studio.order.utils;

import static com.studio.order.utils.VolleyConstants.API_URL_ADD_COMMANDS;
import static com.studio.order.utils.VolleyConstants.API_URL_ADD_PRODUCTS;
import static com.studio.order.utils.VolleyConstants.API_URL_DELETE_PRODUCTS;
import static com.studio.order.utils.VolleyConstants.API_URL_GET_COMMANDS;
import static com.studio.order.utils.VolleyConstants.API_URL_GET_PRODUCTS;
import static com.studio.order.utils.VolleyConstants.API_URL_LOGIN;
import static com.studio.order.utils.VolleyConstants.API_URL_UP_PRODUCTS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.studio.order.adapters.AdapterCommands;
import com.studio.order.adapters.AdapterProducts;
import com.studio.order.models.ModelCommands;
import com.studio.order.models.ModelProducts;
import com.studio.order.ui.MainActivityCommands;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VolleyManager {


    public static void login(Activity activity,
                             TextInputLayout emailTextInputLayout,
                             TextInputLayout passwordTextInputLayout,
                             ProgressDialog myProgress) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL_LOGIN,
                response -> {
                    try {
                        // Parse the response as JSON
                        JSONObject jsonObject = new JSONObject(response);

                        // Check if the login was successful
                        String status = jsonObject.getString("status");
                        if (status.equals("true")) {
                            // Get the user data from the response
                            JSONArray userArray = jsonObject.getJSONArray("data");
                            JSONObject userJson = userArray.getJSONObject(0);

                            // Extract the necessary information from the user object
                            int userId = userJson.getInt("user_id");
                            String userName = userJson.getString("name");
                            String userEmail = userJson.getString("email");
                            // ... (extract other fields as needed)

                            // Save the session data or necessary information
                            // For example, you can use SharedPreferences to save the session
                            SharedPreferences sharedPrefs = activity.getSharedPreferences("session", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putInt("userId", userId);
                            editor.putString("userName", userName);
                            editor.putString("userEmail", userEmail);
                            // ... (save other fields as needed)
                            editor.apply();

                            myProgress.cancel();
                            // Navigate to the desired activity
                            Intent intent = new Intent(activity, MainActivityCommands.class);
                            activity.startActivity(intent);
                            activity.finish();
                        } else {
                            // Login failed, display a dialog to inform the user
                            String message = jsonObject.getString("message");
                            passwordTextInputLayout.setError(message);
                            myProgress.cancel();
                        }
                    } catch (JSONException e) {
                        // Error occurred while parsing the response
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Error occurred while sending the request
                    error.printStackTrace();
                    myProgress.cancel();
                    Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString().trim());
                params.put("password", Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString().trim());
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(activity).add(stringRequest);
    }


    public static void addCommand(
            Activity activity, String userId, String nameCommand,
            String contentCommand, ProgressDialog myProgress, RecyclerView recyclerView, TextView noDataTextView, ProgressBar progressBar) {


        myProgress.show();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL_ADD_COMMANDS,
                response -> {
                    try {
                        // Parse the response as JSON
                        JSONObject jsonObject = new JSONObject(response);

                        // Check if the login was successful
                        String status = jsonObject.getString("status");
                        if (status.equals("true")) {
                            // Get the professor data from the response

                            //MainActivityCommands.getCommands();
                           getCommands(activity,String.valueOf(userId),recyclerView,noDataTextView,progressBar);

                            myProgress.cancel();

                        } else {
                            // Login failed, display a dialog to inform the user
                            String message = jsonObject.getString("message");
                            Toast.makeText(activity, "Error: " + message, Toast.LENGTH_SHORT).show();

                            myProgress.cancel();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    myProgress.cancel();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", nameCommand);
                params.put("description", contentCommand);
                params.put("userId", userId);

                return params;
            }
        };

        Volley.newRequestQueue(activity).add(stringRequest);
    }

    public static void getCommands(Activity activity, String userId, RecyclerView recyclerView, TextView noDataTextView, ProgressBar progressBar) {

        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL_GET_COMMANDS + "userId=" + userId,
                null,
                response -> {
                    try {
                        // Parse the JSON response
                        List<ModelCommands> modelCommands = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            String cmdId = jsonObject.getString("id");
                            String cmdName = jsonObject.getString("name");
                            String cmdDescription = jsonObject.getString("description");

                            ModelCommands modelsList = new ModelCommands(cmdId, cmdName, cmdDescription);
                            modelCommands.add(modelsList);
                        }

                        // Set up RecyclerView and adapter
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        AdapterCommands adapterCommands = new AdapterCommands(activity, modelCommands);
                        recyclerView.setAdapter(adapterCommands);

                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        noDataTextView.setVisibility(View.GONE);

                        // Notify the adapter of the data change
                        adapterCommands.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        noDataTextView.setVisibility(View.VISIBLE);
                        noDataTextView.setText("Aucun donnes trouvé");
                    }
                },
                error -> {
                    error.printStackTrace();
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    noDataTextView.setVisibility(View.VISIBLE);
                    noDataTextView.setText("Aucun donnes trouvé");
                }) {
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    String errorResponse = new String(volleyError.networkResponse.data);
                    if (errorResponse.equals("No commands found")) {
                        // Handle "No models found" case
                        activity.runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            noDataTextView.setText("Aucun donnes trouvé");
                            noDataTextView.setVisibility(View.VISIBLE);
                        });
                    }
                }
                return super.parseNetworkError(volleyError);
            }
        };


        // Add the request to the request queue
        Volley.newRequestQueue(activity).add(request);
    }

    public static void addProduct(Activity activity, String commandId,
                                  String nameProduct, String priceProduct,
                                  String quantityProduct, ProgressDialog myProgress,
                                  RecyclerView recyclerView,
                                  TextView noDataTextView, ProgressBar progressBar) {


        myProgress.show();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL_ADD_PRODUCTS,
                response -> {
                    try {
                        // Parse the response as JSON
                        JSONObject jsonObject = new JSONObject(response);

                        // Check if the login was successful
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("true")) {
                            // Get the professor data from the response
                            Toast.makeText(activity,  message, Toast.LENGTH_SHORT).show();

                            getProducts(activity,commandId,recyclerView,noDataTextView,progressBar);
                        } else {
                            // Login failed, display a dialog to inform the user
                            Toast.makeText(activity, "Error: " + message, Toast.LENGTH_SHORT).show();

                        }
                        myProgress.cancel();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    myProgress.cancel();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("commandId", commandId);
                params.put("nameProduct", nameProduct);
                params.put("priceProduct", priceProduct);
                params.put("quantityProduct", quantityProduct);

                return params;
            }
        };

        Volley.newRequestQueue(activity).add(stringRequest);
    }

    public static void getProducts(Activity activity, String commandId, RecyclerView recyclerView, TextView noDataTextView, ProgressBar progressBar) {


        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL_GET_PRODUCTS + "commandId=" + commandId,
                null,
                response -> {
                    try {
                        // Parse the JSON response
                        List<ModelProducts> modelProducts = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            String proId = jsonObject.getString("id");
                            String proName = jsonObject.getString("name");
                            String proPrice= jsonObject.getString("price");
                            String proQuantity= jsonObject.getString("quantity");

                            ModelProducts modelProducts1 = new ModelProducts(proId, proName, proPrice,proQuantity);
                            modelProducts.add(modelProducts1);
                        }

                        // Set up RecyclerView and adapter
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        AdapterProducts adapterProducts = new AdapterProducts(activity, modelProducts);
                        recyclerView.setAdapter(adapterProducts);

                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        noDataTextView.setVisibility(View.GONE);

                        // Notify the adapter of the data change
                        adapterProducts.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        noDataTextView.setVisibility(View.VISIBLE);
                        noDataTextView.setText("Aucun donnes trouvé");
                    }
                },
                error -> {
                    error.printStackTrace();
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    noDataTextView.setVisibility(View.VISIBLE);
                    noDataTextView.setText("Aucun donnes trouvé");
                }) {
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    String errorResponse = new String(volleyError.networkResponse.data);
                    if (errorResponse.equals("No commands found")) {
                        // Handle "No models found" case
                        activity.runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            noDataTextView.setText("Aucun donnes trouvé");
                            noDataTextView.setVisibility(View.VISIBLE);
                        });
                    }
                }
                return super.parseNetworkError(volleyError);
            }
        };


        // Add the request to the request queue
        Volley.newRequestQueue(activity).add(request);
    }

    public static void deleteProduct(Activity activity, String productId) {

        StringRequest deleteRequest = new StringRequest(Request.Method.POST, API_URL_DELETE_PRODUCTS,
                response -> {
                    try {
                        // Parse the response as JSON
                        JSONObject jsonObject = new JSONObject(response);

                        // Check the status from the response
                        boolean status = jsonObject.getBoolean("status");
                        String message = jsonObject.getString("message");

                        if (status) {

                            activity.onBackPressed();

                        } else {
                            // Failed to delete model
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // Error occurred while parsing the response
                        Toast.makeText(activity, "Error parsing response", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Error occurred while sending the request
                    Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add the model ID to the request parameters
                Map<String, String> params = new HashMap<>();
                params.put("productId", String.valueOf(productId));
                return params;
            }
        };

        Volley.newRequestQueue(activity).add(deleteRequest);
    }


    public static void updateProduct(Activity activity, String productId, String nameText, String priceText, String quantityText) {


        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL_UP_PRODUCTS,
                response -> {
                    try {
                        // Parse the response as JSON
                        JSONObject jsonObject = new JSONObject(response);

                        // Check if the login was successful
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("true")) {
                            // Get the professor data from the response
                            Toast.makeText(activity,  message, Toast.LENGTH_SHORT).show();

                        } else {
                            // Login failed, display a dialog to inform the user
                            Toast.makeText(activity, "Error: " + message, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("productId", productId);
                params.put("nameProduct", nameText);
                params.put("priceProduct", priceText);
                params.put("quantityProduct", quantityText);

                return params;
            }
        };

        Volley.newRequestQueue(activity).add(stringRequest);
    }
}
