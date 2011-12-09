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

public class AsyncResult<T> {

    protected final LoaderStatus status;
    protected final T data;
    protected final Throwable exception;
    
    public AsyncResult(LoaderStatus status, T data) {
        this.status = status;
        this.data = data;
        this.exception = null;
    }
    
    public AsyncResult(LoaderStatus status, Throwable exception) {
        this.status = status;
        this.data = null;
        this.exception = exception;
    }

    public LoaderStatus getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public Throwable getException() {
        return exception;
    }
    
}
