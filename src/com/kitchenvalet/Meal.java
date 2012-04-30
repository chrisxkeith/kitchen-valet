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

public class Meal {
	private Vector<Recipe>	mRecipes;
	private	int				mTotalMins;
	public Meal() throws Exception {
		mRecipes = KitchenValet.getDbAdapter().fetchRecipes(1);
		mTotalMins = 0;
		for (Recipe r : mRecipes) {
			if (mTotalMins < r.totalMinutes()) {
				mTotalMins = r.totalMinutes();
			}
		}
	}
	public void loadMealSteps(Date targetTime) throws Exception {
		for (Recipe r : mRecipes) {
			r.loadMealSteps(targetTime);
		}
	}
	public String getTotalTimeString() {
		int h = mTotalMins / 60;
		int m = mTotalMins % 60;
		String s = "";
		Integer i = new Integer(h);
		if (i.intValue() < 10) {
			s += "0";
		}
		s += i.toString();
		s += ":";
		i = new Integer(m);
		if (i.intValue() < 10) {
			s += "0";
		}
		s += i.toString();
		return s;
	}
	public Date getTargetTime() {
		Date n = new Date();
		return new Date(n.getTime() + (mTotalMins * 60 * 1000));
	}
	public int getTotalMinutes() { return mTotalMins; }
}
