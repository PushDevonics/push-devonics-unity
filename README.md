[![Release](https://jitpack.io/v/PushDevonics/push-devonics-unity.svg)](https://jitpack.io/#PushDevonics/push-devonics-unity)

# push-devonics-unity
Push Devonics Unity Plugin

Java:

MainActivity:

    private PushDevonics pushDevonics;

MainActivity in onCreate():

    pushDevonics = new PushDevonics(getApplicationContext(), "appId");
        
    // If you need internalId
    String internalId = pushDevonics.getInternalId();
    
    // If you want add tag type String
    pushDevonics.setTags("key", "value");
    
    // If you need deeplink
    String deeplink = pushDevonics.getDeeplink();
    
    // If you need open URL in browser
    pushDevonics.openUrl();

MainActivity in onStop():

    pushDevonics.stopSession();
