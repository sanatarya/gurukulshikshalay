package softgalli.gurukulshikshalay.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.adapter.TeacherListAdapter;
import softgalli.gurukulshikshalay.adapter.TopperListAdapter;
import softgalli.gurukulshikshalay.common.PreferenceName;
import softgalli.gurukulshikshalay.common.Utilz;
import softgalli.gurukulshikshalay.intrface.OnClickListener;
import softgalli.gurukulshikshalay.model.TeacherListModel;
import softgalli.gurukulshikshalay.model.TopperLisrModel;
import softgalli.gurukulshikshalay.retrofit.DownlodableCallback;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;

public class TeacherListActivity extends AppCompatActivity {
    @BindView(R.id.rv_common)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private RetrofitDataProvider retrofitDataProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topperlist);
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initToolbar();
        getTopperList();
    }

    private void getTopperList() {
        Utilz.showDailog(TeacherListActivity.this, getResources().getString(R.string.pleasewait));
        retrofitDataProvider.teacherList(new DownlodableCallback<TeacherListModel>() {
            @Override
            public void onSuccess(final TeacherListModel result) {
                Utilz.closeDialog();
                if (result.getStatus().contains(PreferenceName.TRUE)) {
                    recyclerView.setAdapter(new TeacherListAdapter(TeacherListActivity.this, result.getData(), new OnClickListener() {
                        @Override
                        public void onClick(int pos) {

                        }
                    }));
                }
            }

            @Override
            public void onFailure(String error) {
                Utilz.closeDialog();
            }

            @Override
            public void onUnauthorized(int errorNumber) {
                Utilz.closeDialog();
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Teacher List");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}