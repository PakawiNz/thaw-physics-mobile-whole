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
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.pkjm.thaw.analyser.ColorAnalyser;

public class CameraActivity extends Activity {

    ColorAnalyser analyser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Camera2BasicFragment fragment = Camera2BasicFragment.newInstance();
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        TextView debugText = (TextView)findViewById(R.id.debug);
        Button showColorBtn = (Button)findViewById(R.id.showColor);
        this.analyser = new ColorAnalyser(this,debugText,showColorBtn,fragment);
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

}
