package com.sudhindra.delta_onsites_task_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sudhindra.delta_onsites_task_2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}