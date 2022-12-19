package com.aos.seed.Adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.aos.seed.R;
import com.aos.seed.Ui.Account;
import com.aos.seed.Ui.Designer;
import com.aos.seed.Ui.Farmer;
import com.aos.seed.Ui.Store;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chip_bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chip_bottom_nav = findViewById(R.id.chip_bottom_nav);
        chip_bottom_nav.setItemEnabled(R.id.store,true);
        chip_bottom_nav.setItemSelected(R.id.store,true);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Store()).commit();
        navSelected();

    }

private void navSelected(){
        chip_bottom_nav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.store:
                        fragment = new Store();
                        break;
                    case R.id.farmer:
                        fragment = new Farmer();
                        break;
                    case R.id.designer:
                        fragment = new Designer();
                        break;
                    case R.id.account:
                        fragment = new Account();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
        });
}
}