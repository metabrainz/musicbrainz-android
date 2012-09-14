package org.musicbrainz.mobile.test.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContext;

public class RenamingContext extends RenamingDelegatingContext {
    
    private static final String PREFIX = "test.";

    public RenamingContext(Context context) {
        super(new DelegatedMockContext(context), PREFIX);
    }
    
    private static class DelegatedMockContext extends MockContext {
        
        private Context delegatedContext;
        
        public DelegatedMockContext(Context context) {
            delegatedContext = context;
        }
        
        @Override
        public String getPackageName() {
            return delegatedContext.getPackageName();
        }
        
        @Override
        public SharedPreferences getSharedPreferences(String name, int mode) {
            return delegatedContext.getSharedPreferences(PREFIX + name, mode);
        }
    }

}