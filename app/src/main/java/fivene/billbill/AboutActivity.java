package fivene.billbill;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * 关于界面
 */
public class AboutActivity extends AppCompatActivity {

    private TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        String appVersion="未读取到";
        PackageManager manager = this.getPackageManager();
        try { PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            appVersion = info.versionName; //版本名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version=(TextView) findViewById(R.id.version_number);
        version.setText(appVersion);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
