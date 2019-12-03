package accessbility;

import android.content.Context;

import service.GrabRedPacketService;
import util.Config;


/**
 * BaseAccessbility
 */
public abstract class BaseAccessbility implements Accessbility {

    private GrabRedPacketService service;

    @Override
    public void onCreateAccessbility(GrabRedPacketService service) {
        this.service = service;
    }

    public Context getContext() {
        return service.getApplicationContext();
    }

    public Config getConfig() {
        return service.getConfig();
    }

    public GrabRedPacketService getService() {
        return service;
    }
}
