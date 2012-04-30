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

import android.app.Activity;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class KitchenValet extends Activity implements OnItemClickListener, OnKeyListener {	
    private static final int STARTMEAL_ID = Menu.FIRST;
    private static final int ACTIVITY_TIMER = 0;
	
    private	static KVDbAdapter	_mDbAdapter;
    private	ListView		_mSourceList;
    private	ListView		_mTargetList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.recipes_list);
            _mDbAdapter = new KVDbAdapter(this);
            _mDbAdapter.open();
            _mSourceList = (ListView) findViewById(R.id.android_source_list);
            _mTargetList = (ListView) findViewById(R.id.android_target_list);
            _mSourceList.setOnItemClickListener(this);
            _mSourceList.setOnKeyListener(this);
            _mTargetList.setOnItemClickListener(this);
            _mTargetList.setOnKeyListener(this);
            registerForContextMenu(_mTargetList);  	
            fillData();
            Utility.setContext(this);
        } catch(Throwable t) {
        	Utility.showThrowable(t);
        }
    }
    private void setAdapter(int mealNumber, ListView v) {
        Cursor notesCursor = _mDbAdapter.fetchRecipeRows(mealNumber);
        startManagingCursor(notesCursor);
        String[] from = new String[]{KVDbAdapter.RECIPES_COLS[0]};
        int[] to = new int[]{R.id.text1};
        v.setAdapter(new SimpleCursorAdapter(this, R.layout.recipe_row, notesCursor, from, to));
    }
    private void fillData() {
    	setAdapter(0, _mSourceList);
    	setAdapter(1, _mTargetList);   	
    }
    private void startTimerScreen() {
        Intent i = new Intent(this, MealTimerActivity.class);
        startActivityForResult(i, ACTIVITY_TIMER);
    }
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		switch (keyCode) {
		case 23:
    		startTimerScreen();
    		return true;
		}
		return false;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try {
			Cursor c;
			c = _mDbAdapter.fetchRecipe(arg3);
			int mealNumber;
			if (c.getInt(2) == 0) {
				mealNumber = 1;
			} else {
				mealNumber = 0;
			}
			_mDbAdapter.updateRecipe(c.getInt(0), c.getString(1), mealNumber);
			fillData();
		} catch (Exception e) {
			Utility.showThrowable(e);
		}
	}
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, STARTMEAL_ID, 0, R.string.menu_start);
	}
    @Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
    	case STARTMEAL_ID:
    		startTimerScreen();
	        return true;
		}
		return super.onContextItemSelected(item);
	}
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putLong(NotesDbAdapter.KEY_ROWID, mRowId);
    }   
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }   
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }   
    private void saveState() {
    }
    private void populateFields() {
    }
	public static KVDbAdapter getDbAdapter() {
		return _mDbAdapter;
	}
}