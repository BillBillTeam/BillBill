package bhj;

import android.content.Context;
import android.content.Intent;

import fivene.billbill.MainActivity;

/**
 * Created by ubuntu on 17-11-16.
 */

public class UIHelper {
    public static void returnHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
