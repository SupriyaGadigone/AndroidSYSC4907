package com.example.supriyagadigone.androidsysc4907;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.supriyagadigone.androidsysc4907.Customer.CustomerGroceryListPage;
import com.example.supriyagadigone.androidsysc4907.Customer.CustomerProfilePage;
import com.example.supriyagadigone.androidsysc4907.Organization.OrgWriteNfc;

public class BaseActivity extends AppCompatActivity {
    public Toolbar toolbar;

    public DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    protected Context mContext;
    protected FrameLayout frameLayout;
    protected boolean mIsCustomer; //if false, it is organization

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;

        setContentView(R.layout.drawer_layout);
        frameLayout = findViewById(R.id.content_frame);
    }

    public void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    private void setUpNav() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        navigationView = findViewById(R.id.nav_view);

        if(mIsCustomer){
            navigationView.inflateMenu(R.menu.customer_drawer_contents);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    if (menuItem.isChecked()) {
                        menuItem.setChecked(false);
                    }
                    else {
                        menuItem.setChecked(true);
                    }

                    drawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {

                        case R.id.profile:
                            startActivity(new Intent(BaseActivity.this, CustomerProfilePage.class));
                            break;
                        case R.id.read_nfc:
                            startActivity(new Intent(BaseActivity.this, ReadNfc.class));
                            break;
                        case R.id.grocery:
                            startActivity(new Intent(BaseActivity.this, CustomerGroceryListPage.class));
                            break;
                        case R.id.tapped_products:
                            Intent allCust = new Intent(BaseActivity.this, UserLandingPage.class);
                            allCust.putExtra("BTN_PRESSED", "0");
                            startActivity(allCust);
                            break;
                    }
                    return false;
                }
            });

        }else{
            navigationView.inflateMenu(R.menu.org_drawer_contents);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    if (menuItem.isChecked()) {
                        menuItem.setChecked(false);
                    }
                    else {
                        menuItem.setChecked(true);
                    }

                    drawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {

                        case R.id.read_nfc:
                            startActivity(new Intent(BaseActivity.this, ReadNfc.class));
                            break;
                        case R.id.write_nfc:
                            startActivity(new Intent(BaseActivity.this, OrgWriteNfc.class));
                            break;
                        case R.id.all_products:
                            Intent allProd = new Intent(BaseActivity.this, UserLandingPage.class);
                            allProd.putExtra("BTN_PRESSED", "1");
                            startActivity(allProd);
                            break;
                    }
                    return false;
                }
            });
        }
        drawerToggle.syncState();

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNav();

        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item) ;
    }


}
