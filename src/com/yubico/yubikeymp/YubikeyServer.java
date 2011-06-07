package com.yubico.yubikeymp;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 * This class represents currently running server instance.
 * 
 * @author Vlastimil Ovčáčík
 */
public class YubikeyServer {
    // TODO what if GAE runs more instances?

    /**
     * Instance representing currently running server instance.
     */
    private static final YubikeyServer instance = new YubikeyServer();

    /**
     * Provides access to datastore.
     */
    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    /**
     * Private constructor.
     */
    private YubikeyServer() {
        super();
        // TODO is this static class?
    }

    /**
     * Use this method to get YubikeyServer instance representing currently running server instance.
     * 
     * @return YubikeyServer instance
     */
    public static YubikeyServer getInstance() {
        return instance;
    }

    /**
     * Getter for admin's name.
     * 
     * @return admin's name or null if not set.
     */
    public String getAdminName() {
        String adminName = null;
        final YubikeyPref pref = YubikeyPref.findInstance();
        if (pref != null) {
            adminName = pref.getPropertyAsString(YubikeyPref.ADMIN);
        }
        return adminName;
    }

    /**
     * Returns true if the provided OTP is issued by admin.
     * 
     * @param otp
     *            OTP to verify
     * @return true if OTP is issued by admin otherwise false.
     */
    public boolean hasAdminAccess(final YubikeyOTP otp) {
        final String adminName = getAdminName();
        return isInitialized() && adminName != null && otp != null && adminName.equals(otp.getStaticPart())
                && otp.verify();
    }

    /**
     * Server is initialized when datastore contains valid pref entity.
     * 
     * @return true if server is initialized otherwise false.
     */
    public boolean isInitialized() {
        if (getAdminName() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Put YubikeySecret to datastore.
     * 
     * @param secret
     *            the YubikeySecret to put in
     * @return true if operation is successful otherwise false.
     */
    public boolean put(final YubikeySecret secret) {
        if (secret != null) {
            datastore.put(secret.getEntity());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Put YubikeyPref to datastore.
     * 
     * @param pref
     *            the YubikeyPref to put in
     * @return true if operation is successful otherwise false.
     */
    public boolean put(YubikeyPref pref) {
        if (pref != null) {
            datastore.put(pref.getEntity());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns API key for use in YubiCloud.
     * 
     * @return API key from datastore Prefs kind if defined or 1 as default value.
     */
    public int getAPIKey() {
        int apiKey = 1; // TODO default value ok?
        final YubikeyPref pref = YubikeyPref.findInstance();
        if (pref != null) {
            apiKey = pref.getPropertyAsInt(YubikeyPref.API_KEY);
        }
        return apiKey;
    }
}
