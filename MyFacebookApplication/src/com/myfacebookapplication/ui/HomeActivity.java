package com.myfacebookapplication.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.myfacebookapplication.R;
import com.myfacebookapplication.ui.adapters.MyPageAdapter;
import com.myfacebookapplication.ui.fragment.AlbumFragment;
import com.myfacebookapplication.ui.fragment.PictureFragment;
import com.myfacebookapplication.ui.fragment.VideoFragment;

public class HomeActivity extends FragmentActivity implements
		OnPageChangeListener {

	private static final String	PERMISSION					= "publish_actions";
	private PictureFragment		mainFragment;
	private UiLifecycleHelper	uiHelper;
	private LoginButton			mLoginButton;
	private GraphUser			user;
	private TextView			mUserName;
	private RelativeLayout		mHeaderContainer;

	private PendingAction		pendingAction				= PendingAction.NONE;
	private ProfilePictureView	profilePictureView;
	private final String		PENDING_ACTION_BUNDLE_KEY	= "com.myfacebookapplication:PendingAction";

	protected ViewPager			mPager;
	public MyPageAdapter		mAdapter;
	private int					current_tab					= 0;
	protected PagerTabStrip		mTitlePageIndicator;

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	private Session.StatusCallback	callback		= new Session.StatusCallback() {
														@Override
														public void call(
																Session session,
																SessionState state,
																Exception exception) {
															onSessionStateChange(
																	session,
																	state,
																	exception);
														}
													};
	private FacebookDialog.Callback	dialogCallback	= new FacebookDialog.Callback() {
														@Override
														public void onError(
																FacebookDialog.PendingCall pendingCall,
																Exception error,
																Bundle data) {
															Log.d("HelloFacebook",
																	String.format(
																			"Error: %s",
																			error.toString()));
														}

														@Override
														public void onComplete(
																FacebookDialog.PendingCall pendingCall,
																Bundle data) {
															Log.d("HelloFacebook",
																	"Success!");
														}
													};

	private void setTabNavigation() {
		mAdapter = new MyPageAdapter(getSupportFragmentManager());
		setAdapterList();
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setOffscreenPageLimit(2);
		mPager.setAdapter(mAdapter);
		mTitlePageIndicator = (PagerTabStrip) findViewById(R.id.indicatorTabs);
		mTitlePageIndicator.setDrawFullUnderline(true);
		mTitlePageIndicator.setTabIndicatorColor(getResources().getColor(
				android.R.color.black));
		mPager.setCurrentItem(current_tab);
		mPager.setOnPageChangeListener(this);
	}

	private void setAdapterList() {
		Bundle bundle = new Bundle(1);
		mAdapter.setUpTab(getString(R.string.header_album),
				getString(R.string.header_album), Fragment.instantiate(this,
						AlbumFragment.class.getName(), bundle));
		mAdapter.setUpTab(getString(R.string.header_video),
				getString(R.string.header_video), Fragment.instantiate(this,
						VideoFragment.class.getName(), bundle));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}
		setContentView(R.layout.main);
		mHeaderContainer = (RelativeLayout) findViewById(R.id.header_container);
		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
		mUserName = (TextView) findViewById(R.id.user_name);
		mLoginButton = (LoginButton) findViewById(R.id.login_button);
		mLoginButton
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
					@Override
					public void onUserInfoFetched(GraphUser user) {
						HomeActivity.this.user = user;
						updateUI();
						// It's possible that we were waiting for this.user to
						// be populated in order to post a
						// status update.
						// handlePendingAction();
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();

		updateUI();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);

		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_PHOTO:
			// postPhoto();
			break;
		case POST_STATUS_UPDATE:
			// postStatusUpdate();
			break;
		}
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (pendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(HomeActivity.this)
					.setTitle(R.string.cancelled)
					.setMessage(R.string.permission_not_granted)
					.setPositiveButton(R.string.ok, null).show();
			pendingAction = PendingAction.NONE;
		}
		else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
		updateUI();
	}

	private void updateUI() {
		Session session = Session.getActiveSession();
		boolean enable = (session != null && session.isOpened());

		if (enable && user != null) {
			profilePictureView.setProfileId(user.getId());
			mUserName.setText(getString(R.string.hello_user,
					user.getFirstName()));
			mHeaderContainer.setVisibility(View.VISIBLE);
		}
		else {
			profilePictureView.setProfileId(null);
			mUserName.setVisibility(View.GONE);
		}

		setTabNavigation();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

}
