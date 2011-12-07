/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.loader;

import org.musicbrainz.android.api.data.UserData;

public class AsyncEntityResult<T> {
    
    private final LoaderResult result;
    private final T data;
    private final UserData userData;
    private final Throwable exception;
    
    public AsyncEntityResult(LoaderResult result, T data) {
        this.result = result;
        this.data = data;
        this.exception = null;
        this.userData = null;
    }
    
    public AsyncEntityResult(LoaderResult result, T data, UserData userData) {
        this.result = result;
        this.data = data;
        this.userData = userData;
        this.exception = null;
    }
    
    public AsyncEntityResult(LoaderResult result, Throwable exception) {
        this.result = result;
        this.exception = exception;
        this.data = null;
        this.userData = null;
    }

    public LoaderResult getResult() {
        return result;
    }

    public T getData() {
        return data;
    }

    public UserData getUserData() {
        return userData;
    }
    
    public Throwable getException() {
        return exception;
    }
    
    public boolean hasUserData() {
        return userData != null;
    }

}
