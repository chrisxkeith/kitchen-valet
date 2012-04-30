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

public class Recipe {
	private String				mName;
	private int					mKey;
	private Vector<RecipeStep>	mSteps;
	
	public Recipe(int key, String name) {
		mName = name;
		mSteps = null;
		mKey = key;
	}
	public String	getName() { return mName; }
	public int		getKey() { return mKey; }
	public void		setSteps(Vector<RecipeStep> r) { mSteps = r; }
	
	public int totalMinutes() {
		int	total = 0;
		for (RecipeStep rs : mSteps) {
			total += rs.getDuration();
		}
		return total;
	}
	public void loadMealSteps(Date targetTime) throws Exception {
		long t = targetTime.getTime();
		for (int i = mSteps.size() - 1 ; i >= 0; i--) {
			RecipeStep rs = mSteps.elementAt(i);
			t -= (rs.getDuration() * 60 * 1000);
			KitchenValet.getDbAdapter().insertMealStep(mKey, rs.getKey(), new Date(t));
		}		
	}
}