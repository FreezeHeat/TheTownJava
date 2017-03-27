/**
 
 */
package resources;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class handles languages (Hebrew and English)
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class LocalizationUtil 
{
    /**
     * Used with all forms to set the language <i>(HE/EN)</i>
     */
    public static ResourceBundle localizedResourceBundle;
    
    static
    {
        // gets the bundle for the default locale used
        localizedResourceBundle = ResourceBundle.getBundle(
                "resources.Bundle", new Locale("en_US"));
    }
    
    
}
