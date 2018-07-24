package kiit.tanmay.MultipurposeTextView;


import android.support.annotation.NonNull;
import android.util.Log;

public class MuppetUtils {
    private OnGetPermissionResult onGetPermissionResult;
    private OnGetContact onGetContact;
    private static MuppetUtils instance;
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

    protected MuppetUtils setOnGetPermissionResult(OnGetPermissionResult onGetPermissionResult) {
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
    }

    public MuppetUtils setHandleDataTypeInterface(HandleDataTypeInterface handleDataTypeInterface) {
        this.handleDataTypeInterface = handleDataTypeInterface;
        return instance;
    }

    protected HandleDataTypeInterface getHandleDataTypeInterface() {
        return handleDataTypeInterface;
    }

    protected interface OnGetPermissionResult {
        void getPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    protected interface OnGetContact{
        void getContactNumber(String number) ;
    }
}
