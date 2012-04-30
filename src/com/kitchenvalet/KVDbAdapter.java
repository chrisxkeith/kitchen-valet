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

import java.util.Date;
import java.util.Vector;
import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DefaultData {
	public static String[] mDefaultSteps = {
			"Unknown recipe step!	0", 		// 00
			"Prep lasagna pan	1",				// 01
			"Bake lasagna	115",				// 02
			"Let lasagna rest	10",			// 03
			"Assemble salad ingredients	5",		// 04
			"Put salads together	2",			// 05
			"Mix bisquick & milk	2",			// 06
			"Bake biscuits	10",				// 07
			"Preheat oven to 375	6",			// 08
			"Boil 2.5 cups water	4",			// 09
			"Let couscous stand	5",				// 10
			"Mix couscous	1",					// 11
			"Put salad bowls in freezer	10",	// 12
			"Prep meat / heat pan	4",			// 13
			"Stir-fry meat / prep veg	7",		// 14
			"Stir-fry veg	8",					// 15
			"Mix all together & heat sauce	5", // 16
			"Boil 1 gallon water + salt	9",		// 17
			"Boil cut potatos	22",			// 18
			"Mash with butter/milk	5",			// 19
			"Microwave pot roast	9",			// 20
			"Let post roast sit	3",				// 21
			"Carve pot roast	2",				// 22
			"Cook noodles	12",				// 23
			"Drain noodles	1",					// 24
			"Boil 3 qts water	8",				// 25
			"Assemble frittata ingredients	2", // 26
			"Slice sausage	2",					// 27
			"Stir fry sausage & cut veg	5",		// 28
			"Stir fry vegetables	6",			// 29
			"Steam vegetables	2",				// 30
			"Beat eggs, mix into veg mix	2",	// 31
			"Cook on low/medium top of stove	5",	// 32
			"Sprinkle with cheese, cook in oven	10",	// 33
			"Preheat oven to 450	8",			// 34
			"Chop vegetables	10",			// 35
			"Stir-fry onion and garlic	8",		// 36
			"Stir other ingredients, bring to boil	5",	// 37
			"Simmer	9",							// 38
			"Mix other ingredients, heat	5"	// 39
	};
	public static String[] mDefaultRecipes = {
			"Frozen Lasagna",		// 00
			"Spinach Salad",		// 01
			"Drop Biscuits",		// 02
			"Couscous",				// 03
			"Spaghetti Sauce",		// 04
			"Spaghetti Noodles",	// 05
			"Mashed Potatoes",		// 06
			"Pre-cooked Pot Roast",	// 07
			"Frittata in a Pan",	// 08
			"Garbanzo beans and vegetables" // 09
	};
	public static String[] mDefaultRecipeSteps = {
			"00	0	34",
			"00	1	01",
			"00	2	02",
			"00	3	03",
			"01	0	12",
			"01	1	04",
			"01	2	05",
			"02	0	34",
			"02	1	06",
			"02	2	07",
			"03	0	09",
			"03	1	10",
			"03	2	11",
			"04	0	13",
			"04	1	14",
			"04	2	15",
			"04	3	16",
			"05	0	17",
			"05	1	23",
			"05	2	24",
			"06	0	17",
			"06	1	18",
			"06	2	19",
			"07	0	20",
			"07	1	21",
			"07	2	22",
			"08	0	26",
			"08	1	27",
			"08	2	28",
			"08	3	29",
			"08	4	30",
			"08	5	31",
			"08	6	32",
			"08	7	33",
			"09	0	35",
			"09	1	36",
			"09	2	37",
			"09	3	38",
			"09	4	39"
	};
}
public class KVDbAdapter {
    private static final int		DATABASE_VERSION = 1;
    private static final String 	DATABASE_NAME = "kvdata";
    
    private static final String		RECIPES_TABLE = "recipes";
    public static final String[]	RECIPES_COLS = { "title", "mealNumber" };	// TODO: unique ?
    private static final String[]	RECIPES_COLTYPES = { "text", "int" };

    private static final String		STEPS_TABLE = "steps";
    public static final String[]	STEPS_COLS = { "title", "duration" };
    private static final String[]	STEPS_COLTYPES = { "text", "int" };

    private static final String		RECIPESTEPS_TABLE = "recipesteps";
    public static final String[]	RECIPESTEPS_COLS = { "recipeId", "stepnumber", "stepId" };
    private static final String[]	RECIPESTEPS_COLTYPES = { "int", "int", "int" };

    private static final String		MEALSTEPS_TABLE = "mealsteps";
    public static final String[]	MEALSTEPS_COLS = { "recipeId", "stepId", "targetTime", "timeStarted" };
    private static final String[]	MEALSTEPS_COLTYPES = { "int", "int", "text", "text" };

    private static final String[]	ALL_TABLES = {RECIPES_TABLE, STEPS_TABLE, RECIPESTEPS_TABLE, MEALSTEPS_TABLE};
    public static final String		KEY_ROWID = "_id";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        private String buildCreate(String table, String[] columns, String[] colTypes) {
        	String	r = "create table " + table +  "(_id integer primary key autoincrement, ";
        	for (int i = 0; i < columns.length; i++) {
        		if (i > 0) {
        			r += ", ";
        		}
        		r += (columns[i] + " " + colTypes[i] + " not null");
        	}
        	return r + ");";
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(buildCreate(RECIPES_TABLE, RECIPES_COLS, RECIPES_COLTYPES));
            db.execSQL(buildCreate(STEPS_TABLE, STEPS_COLS, STEPS_COLTYPES));
            db.execSQL(buildCreate(RECIPESTEPS_TABLE, RECIPESTEPS_COLS, RECIPESTEPS_COLTYPES));
            db.execSQL(buildCreate(MEALSTEPS_TABLE, MEALSTEPS_COLS, MEALSTEPS_COLTYPES));
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(this.getClass().getName(), "Upgrading database from version " + oldVersion + " to "
                    + newVersion + " not supported yet.");
        }
    } 
    private final Context	_mCtx;
    
    private DatabaseHelper	_mDbHelper;
    private SQLiteDatabase	_mDb;
    
    private void loadDefaultRecipes() {
     	for (String tableName : ALL_TABLES) {
    		try {
                _mDb.execSQL("Drop table " + tableName + ";");
      	   	} catch (Throwable t) {
        		// ignore, go on to re-create table...
        	}
   		}
     	_mDbHelper.onCreate(_mDb);
     	try {
        	for (int i = 0; i < DefaultData.mDefaultRecipes.length; i++) {
        		insertRecipe(DefaultData.mDefaultRecipes[i], 0);
        	}
        	for (int i = 0; i < DefaultData.mDefaultSteps.length; i++) {
        		String[] arr = DefaultData.mDefaultSteps[i].split("\t");
        		createStep(arr[0], Integer.parseInt(arr[1]));
        	}
        	for (int i = 0; i < DefaultData.mDefaultRecipeSteps.length; i++) {
        		String[] arr = DefaultData.mDefaultRecipeSteps[i].split("\t");
        		insertRecipeStep(Integer.parseInt(arr[0]), 
        				Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
        	}
     	} catch (Exception e) {
     		Utility.showThrowable(e);
     	}
	}
    public KVDbAdapter(Context ctx) {
        _mCtx = ctx;
    }
    public KVDbAdapter open() throws SQLException {
        _mDbHelper = new DatabaseHelper(_mCtx);
        _mDb = _mDbHelper.getWritableDatabase();
        loadDefaultRecipes();
        return this;
    }   
    public void close() {
        _mDbHelper.close();
    }
    public Cursor fetchRecipeRows(int mealNumber) {
    	String selectionArgs = "mealNumber = " + mealNumber;
        return _mDb.query(RECIPES_TABLE,
        		new String[] {KEY_ROWID, RECIPES_COLS[0]},
        		selectionArgs, null, null, null, RECIPES_COLS[0]);
    }
    public Vector<Recipe> fetchRecipes(int mealNumber) throws Exception {
    	Vector<Recipe> v = new Vector<Recipe>();
    	Cursor c = fetchRecipeRows(mealNumber);
        if (c == null) {
        	throw new Exception("No recipe for key: " + mealNumber);
        }
		while (c.moveToNext()) {
			Recipe r = new Recipe(c.getInt(0), c.getString(1));
			r.setSteps(stepsForRecipe(r));
			v.add(r);
		}
    	return v;
    }
    public Cursor fetchRecipe(long rowId) throws Exception {
        Cursor c = _mDb.query(true, RECIPES_TABLE,
        		new String[] {KEY_ROWID, RECIPES_COLS[0], RECIPES_COLS[1]},
        		KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (c == null) {
        	throw new Exception("No recipe for key: " + rowId);
        }
        c.moveToFirst();
        return c;
    }
    private ContentValues recipeCV(String title, int mealNumber) {
        ContentValues args = new ContentValues();
        args.put(RECIPES_COLS[0], title);
        args.put(RECIPES_COLS[1], mealNumber);
        return args;
    }
    public void insertRecipe(String title, int mealNumber) throws Exception {
        checkInsert(_mDb.insert(RECIPES_TABLE, null, recipeCV(title, mealNumber)));
   }
   public boolean deleteRecipe(long rowId) {
       return _mDb.delete(RECIPES_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
   }
    public boolean updateRecipe(long rowId, String title, int mealNumber) {
        return _mDb.update(RECIPES_TABLE, recipeCV(title, mealNumber),
        		KEY_ROWID + "=" + rowId, null) > 0;
    }
    private ContentValues stepCV(String title, int duration) {
        ContentValues args = new ContentValues();
        args.put(STEPS_COLS[0], title);
        args.put(STEPS_COLS[1], duration);
        return args;
    }
    private void checkInsert(long returnVal) throws Exception {
    	if (returnVal == -1) {
    		throw new Exception("");
    	}
    }
    public void createStep(String title, int duration) throws Exception {
    	checkInsert(_mDb.insert(STEPS_TABLE, null, stepCV(title, duration)));
    }
    public boolean updateStep(long rowId, String title, int duration) {
        return _mDb.update(STEPS_TABLE, stepCV(title, duration),
        		KEY_ROWID + "=" + rowId, null) > 0;
    }
    public void insertRecipeStep(int recipeId, int stepOrder, int stepId) throws Exception {
        ContentValues initialValues = new ContentValues();
        initialValues.put(RECIPESTEPS_COLS[0], recipeId);
        initialValues.put(RECIPESTEPS_COLS[1], stepOrder);
        initialValues.put(RECIPESTEPS_COLS[2], stepId);
        checkInsert(_mDb.insert(RECIPESTEPS_TABLE, null, initialValues));
    }
    public void insertMealStep(int recipeId, int stepId, Date targetTime) throws Exception {
        ContentValues initialValues = new ContentValues();
        initialValues.put(MEALSTEPS_COLS[0], recipeId);
        initialValues.put(MEALSTEPS_COLS[1], stepId);
        SimpleDateFormat	df = new SimpleDateFormat("HH:mm");
        String s = df.format(targetTime);
        initialValues.put(MEALSTEPS_COLS[2], s);
        initialValues.put(MEALSTEPS_COLS[3], "--:--");
        checkInsert(_mDb.insert(MEALSTEPS_TABLE, null, initialValues));
    }
    public Vector<RecipeStep> stepsForRecipe(Recipe r) throws Exception {
    	Vector<RecipeStep> v = new Vector<RecipeStep>();
        Cursor c = _mDb.query(true, RECIPESTEPS_TABLE,
        		new String[] {RECIPESTEPS_COLS[2]},
        		RECIPESTEPS_COLS[0] + "=" + r.getKey(),
        		null, null, null, RECIPESTEPS_COLS[1], null);
        if (c == null) {
        	throw new Exception("No recipe steps for recipe: " + r.getKey());
        }
        while (c.moveToNext()) {
        	RecipeStep rs = createRecipeStep(c.getInt(0), r);
            v.add(rs);
        }
        return v;
    }
	public RecipeStep createRecipeStep(int key, Recipe r) throws Exception {
        Cursor c = _mDb.query(true, STEPS_TABLE,
        		new String[] {KEY_ROWID, STEPS_COLS[0], STEPS_COLS[1]},
        		KEY_ROWID + "=" + key, null, null, null, null, null);
        if (c == null) {
        	throw new Exception("No recipe step for key: " + key);
        }
        c.moveToFirst();
		return new RecipeStep(c.getString(1), Integer.parseInt(c.getString(2)), key, r);
	}
	public Recipe createRecipe(int key) throws Exception {
		Cursor c = fetchRecipe(key);
		Recipe r = new Recipe(key, c.getString(1));
		r.setSteps(stepsForRecipe(r));
		return r;
	}
	public Cursor fetchMealSteps() throws Exception {
		String q = "Select " + 
					STEPS_TABLE + "." + STEPS_COLS[0] + ", " + 
					MEALSTEPS_TABLE + "." + MEALSTEPS_COLS[2] +
					" from " + STEPS_TABLE + ", " + MEALSTEPS_TABLE +
					" where " + STEPS_TABLE + "." + KEY_ROWID + " = " +
					MEALSTEPS_TABLE + "." + MEALSTEPS_COLS[1] +
					" order by " + MEALSTEPS_TABLE + "." + MEALSTEPS_COLS[2];
		q = "Select * from " + MEALSTEPS_TABLE + " order by " + MEALSTEPS_COLS[2];
        Cursor c = _mDb.rawQuery(q, null);
        if (c == null) {
        	throw new Exception("No meal steps!");
        }
        c.moveToFirst();
        return c;
	}
}
