package softgalli.gurukulshikshalay;

import android.app.Application;
import android.content.Context;

import org.joda.time.LocalDateTime;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class AppController extends Application {

    private static AppController mInstance;

    public LocalDateTime setDate, selectedDate;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.roboto_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // initialize Realm
        Realm.init(getApplicationContext());
        RealmConfiguration cfg = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(cfg);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /*getting the current week*/
    public LocalDateTime getDate() {
        return setDate;
    }

    /**
     * Set the current week date
     *
     * @param setDate
     */
    public void setDate(LocalDateTime setDate) {
        this.setDate = setDate;
    }

    /*getting the selected week*/

    public LocalDateTime getSelected() {
        return selectedDate;
    }

    /**
     * Setting selected week
     *
     * @param selectedDate
     */
    public void setSelected(LocalDateTime selectedDate) {
        this.selectedDate = selectedDate;
    }


}
