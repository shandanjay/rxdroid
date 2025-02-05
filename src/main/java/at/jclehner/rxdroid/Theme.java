/**
 * RxDroid - A Medication Reminder
 * Copyright (C) 2011-2014 Joseph Lehner <joseph.c.lehner@gmail.com>
 *
 *
 * RxDroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. Additional terms apply (see LICENSE).
 *
 * RxDroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RxDroid.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package at.jclehner.rxdroid;

import java.util.NoSuchElementException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;
import at.jclehner.rxdroid.Settings.Keys;
import at.jclehner.rxdroid.util.Timer;

public final class Theme
{
	private static final String TAG = Theme.class.getSimpleName();
	private static final boolean LOGV = false;

	private static final SparseIntArray sAttrCache = new SparseIntArray();

	private static final int LIGHT = R.style.Theme_RxDroid;
	private static final int DARK = R.style.Theme_RxDroid_Dark;

	public static boolean isDark() {
		return Settings.getBoolean(Keys.THEME_IS_DARK, false);
	}

	public static int get() {
		return isDark() ? DARK : LIGHT;
	}

	public static int getResourceAttribute(int attr)
	{
		synchronized(sAttrCache)
		{
			if(sAttrCache.indexOfKey(attr) < 0)
			{
				final Timer t = LOGV ? new Timer() : null;

				final Context c = RxDroid.getContext();
				final int[] attrs = { attr };
				final TypedArray a = c.obtainStyledAttributes(get(), attrs);
				final int resId = a.getResourceId(0, 0);

				a.recycle();

				if(resId == 0)
					throw new NoSuchElementException();

				sAttrCache.put(attr, resId);

				if(LOGV) Log.v(TAG, "getResourceAttributes: " + t);

				return resId;
			}

			return sAttrCache.get(attr);
		}
	}

	public static int getColorAttribute(int attr)
	{
		synchronized(sAttrCache)
		{
			if(sAttrCache.indexOfKey(attr) < 0)
			{
				final Context c = RxDroid.getContext();
				final Resources.Theme t = RxDroid.getContext().getTheme();
				final TypedValue v = new TypedValue();
				final int color;

				if(t.resolveAttribute(attr, v, true))
				{
					color = ContextCompat.getColor(c, v.resourceId);
					sAttrCache.put(attr, color);
					return color;
				}

				return Color.TRANSPARENT;
			}

			return sAttrCache.get(attr);
		}
	}

	public static void clearAttributeCache()
	{
		synchronized(sAttrCache) {
			sAttrCache.clear();
		}
	}

	private Theme() {}
}
