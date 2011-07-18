/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.ws;

import org.musicbrainz.mobile.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Eventually, this service will be used for all webservice POST requests.
 * 
 * With GET requests we're only interested in the data if we can display it. The
 * app is search focused so the data isn't persistent. However, when the user
 * submits data, we want this to eventually succeed whether or not the Activity
 * is still available so the POST threads will live in a service.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class PostService extends Service {

	private NotificationManager notificationManager;

	public class LocalBinder extends Binder {
		PostService getService() {
			return PostService.this;
		}
	}

	public void onCreate() {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// notification that data is being sent
		Notification notify = new Notification(
				android.R.drawable.stat_notify_sync,
				getText(R.string.service_notification),
				System.currentTimeMillis());

		notificationManager.notify(R.string.service_title, notify);
	}

	public void onDestroy() {
		notificationManager.cancel(R.string.service_title);
	}

	public IBinder onBind(Intent intent) {
		return binder;
	}

	private final IBinder binder = new LocalBinder();

}
