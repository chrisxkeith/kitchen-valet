/* Copyright (c) 2009 Christopher Keith
All rights reserved.

This software is the property of Christopher Keith and his licensors
and contains their confidential trade secrets.  Use, examination, copying,
transfer and disclosure to others, in whole or in part, are prohibited
except with the express prior written consent of Christopher Keith.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE. */

package com.kitchenvalet;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

public class MealTimerActivity extends Activity {
	static final int TIME_DIALOG_ID = 0;
	
	private TextView mTimeDisplay;
	private Meal	mMeal;
	private Date	mTargetTime;
	
	private void updateDisplay() {
		SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
	    mTimeDisplay.setText(df.format(mTargetTime));
	}
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
	    new TimePickerDialog.OnTimeSetListener() {
	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		        Date d = new Date();
	        	d.setHours(hourOfDay);
	        	d.setMinutes(minute);
		        Date minTime = new Date(Calendar.getInstance().getTime().getTime() + 
      				  (mMeal.getTotalMinutes() * 60 * 1000));
	        	if (d.after(minTime)) {
	        		mTargetTime = d;
		            updateDisplay();
	        	}
	        }
	    };
	private void setupMealStepsView() throws Exception {
		ListView v = (ListView) findViewById(R.id.android_steps_list);
        Cursor c = KitchenValet.getDbAdapter().fetchMealSteps();
        startManagingCursor(c);
        String[] from = new String[]{KVDbAdapter.MEALSTEPS_COLS[2]};
        int[] to = new int[]{R.id.text1};
        v.setAdapter(new SimpleCursorAdapter(this, R.layout.recipe_row, c, from, to));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        try {
	        setContentView(R.layout.meal_steps);
	        mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
			mMeal = new Meal();
	        mTargetTime = new Date(Calendar.getInstance().getTime().getTime() + 
	        				  (mMeal.getTotalMinutes() * 60 * 1000));
			mMeal.loadMealSteps(mTargetTime);
	        setupMealStepsView();
	        updateDisplay();
	        mTimeDisplay.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                showDialog(TIME_DIALOG_ID);
	            }
	        });
		} catch (Exception e) {
			Utility.showThrowable(e);
		}
	}
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case TIME_DIALOG_ID:
	        return new TimePickerDialog(this, mTimeSetListener,
	        		mTargetTime.getHours(), mTargetTime.getMinutes(), false);
	    }
	    return null;
	}
}
