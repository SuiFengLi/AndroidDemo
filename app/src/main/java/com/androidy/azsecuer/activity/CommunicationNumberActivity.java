package com.androidy.azsecuer.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.activity.db.DBManager;
import com.androidy.azsecuer.adapter.CommunicationNumberAdapter;
import com.androidy.azsecuer.entity.CommunicationNumberInfo;

public class CommunicationNumberActivity extends BaseActionBarActivity implements AdapterView.OnItemClickListener {
    private CommunicationNumberAdapter communicationNumberAdapter = null;
    private ListView list_communication_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_number);
        setActionBarBack("通讯大全");
        initView();
        initData();
    }

    @Override
    public void initView() {
      list_communication_number =  ((ListView) this.findViewById(R.id.list_communication_number));
        list_communication_number.setOnItemClickListener(this);
    }

    public void initData(){

        communicationNumberAdapter = new CommunicationNumberAdapter(DBManager.readTable(this.getIntent().getExtras().getInt("idx")),this);
        list_communication_number.setAdapter(communicationNumberAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_action_bar_left_icon:
                finish();
                break;
        }
    }

    private void showMyDialog(int position) {
        CommunicationNumberInfo numberInfo = (CommunicationNumberInfo) communicationNumberAdapter.getItem(position);
        final String name = numberInfo.getName();
        final String number = numberInfo.getNumber();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("拨号")
                .setMessage("拨打" + name + "的号码:" + number)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + number));
                        if (ActivityCompat.checkSelfPermission(CommunicationNumberActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showMyDialog(position);
    }
}
