/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pkjm.thaw;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pkjm.thaw.analyser.Analyser;
import com.pkjm.thaw.camera2.Camera2BasicFragment;

public class ThawActivity extends Activity {

    public static ThawActivity activity;
    private Analyser analyser;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        activity = this;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Camera2BasicFragment fragment = Camera2BasicFragment.newInstance(prefs);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }

        TextView debugText = (TextView)findViewById(R.id.debug);
        Button showColorBtn = (Button)findViewById(R.id.showColor);
        this.analyser = new Analyser(this,prefs,fragment,debugText,showColorBtn);

        prefs.registerOnSharedPreferenceChangeListener(
                new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(
                    SharedPreferences sharedPreferences, String key) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        analyser.start();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        analyser.stop();
    }

    public void restart(View view){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void goSetting(View view){
        Intent intent = new Intent(getApplicationContext(),FragmentPreferences.class);
        startActivity(intent);
    }

    public SharedPreferences getPreferences(){
        return prefs;
    }

}
