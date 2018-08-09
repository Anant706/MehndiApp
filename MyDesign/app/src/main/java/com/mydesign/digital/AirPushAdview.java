package com.mydesign.digital;

/**
 * Created by maa on 9/3/2016.
 */
//Add import statements
import android.content.Context;

import com.ufsxvuiiow.spiuuvjifr228234.AdConfig;

public class AirPushAdview {


    public AirPushAdview(int appId, String apiKey, Context context){

        AdConfig.setAppId(appId);  //setting appid.
        AdConfig.setApiKey(apiKey); //setting apikey
        //AdConfig.setAdListener(context);  //setting global Ad listener.
        AdConfig.setCachingEnabled(true); //Enabling SmartWall ad caching.
        AdConfig.setPlacementId(0); //pass the placement id.
    }
    public AirPushAdview(){

    }
}
