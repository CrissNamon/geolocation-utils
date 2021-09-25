# GeolocationUtils
Small library to perform operations with geolocation data:
+ Converts latitude, longitude to [QTH locator](https://en.wikipedia.org/wiki/Maidenhead_Locator_System) and back
    + Supports precision specification. Converts location to 2-10 symbols QTH locators
    + Contains `Location` object to safely storage locations and equality checks
+ Checks any QTH locator to validity
+ Calculates distance between two location points
    + Default in meters, but supports distance converters
+ Calculates distance in a path (list) of many location points
    + Supports distance converters too
    + Recalculates distance only if list changes
    
See `Main` class for examples