package kiit.tanmay.MultipurposeTextView;


import android.util.Log;

public class MuppetUtils {

    private static MuppetUtils instance;
    private MuppetView muppetView;
    private HandleDataTypeInterface handleDataTypeInterface;

    private MuppetUtils(){ }

    public static MuppetUtils getInstance() {
        if(instance == null) {
            synchronized (MuppetUtils.class) {
                instance = new MuppetUtils() ;
            }
        }
        return instance;
    }

    protected static void DisplayLog(String s ) {
        Log.i(MuppetUtils.class.getSimpleName() , s == null ? "null input in log" : s);
    }

 /*   protected MuppetUtils setOnGetPermissionResult(OnGetPermissionResult onGetPermissionResult) {
        this.onGetPermissionResult = onGetPermissionResult;
        return instance;
    }

    protected OnGetPermissionResult getOnGetPermissionResult() {
        return onGetPermissionResult;
    }

    protected OnGetContact getOnGetContact() {
        return onGetContact;
    }

    protected MuppetUtils setOnGetContact(OnGetContact onGetContact) {
        this.onGetContact = onGetContact;
        return instance;
    }*/

    public MuppetUtils setHandleDataTypeInterface(HandleDataTypeInterface handleDataTypeInterface) {
        this.handleDataTypeInterface = handleDataTypeInterface;
        return instance;
    }

    public HandleDataTypeInterface getHandleDataTypeInterface() {
        return handleDataTypeInterface;
    }

    protected MuppetView getMuppetView() {
        return muppetView;
    }

    protected void setMuppetView(MuppetView muppetView) {
        this.muppetView = muppetView;
    }


}
