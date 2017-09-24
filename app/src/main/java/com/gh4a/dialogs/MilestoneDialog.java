package com.gh4a.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gh4a.R;
import com.gh4a.fragment.IssueMilestoneListFragment;

import org.eclipse.egit.github.core.Milestone;

public class MilestoneDialog extends BasePagerDialog
        implements IssueMilestoneListFragment.SelectionCallback {
    private static final String EXTRA_OWNER = "owner";
    private static final String EXTRA_REPO = "repo";
    private static final String EXTRA_SHOW_ANY_MILESTONE = "show_any_milestone";
    private static final int[] TITLES = new int[] {
            R.string.open, R.string.closed
    };

    public static MilestoneDialog newInstance(String repoOwner, String repoName,
            boolean showAnyMilestoneButton) {
        MilestoneDialog dialog = new MilestoneDialog();
        Bundle args = new Bundle();
        args.putString(EXTRA_OWNER, repoOwner);
        args.putString(EXTRA_REPO, repoName);
        args.putBoolean(EXTRA_SHOW_ANY_MILESTONE, showAnyMilestoneButton);
        dialog.setArguments(args);
        return dialog;
    }

    private String mRepoOwner;
    private String mRepoName;
    private boolean mShowAnyMilestoneButton;
    private Button mNoMilestoneButton;
    private Button mAnyMilestoneButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mRepoOwner = args.getString(EXTRA_OWNER);
        mRepoName = args.getString(EXTRA_REPO);
        mShowAnyMilestoneButton = args.getBoolean(EXTRA_SHOW_ANY_MILESTONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mShowAnyMilestoneButton) {
            mAnyMilestoneButton = addButton(R.string.issue_filter_by_any_milestone);
        }
        mNoMilestoneButton = addButton(R.string.issue_filter_by_no_milestone);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mNoMilestoneButton) {
            onMilestoneSelected(new Milestone().setTitle(""));
        } else if (v == mAnyMilestoneButton) {
            onMilestoneSelected(null);
        } else {
            super.onClick(v);
        }
    }

    @Override
    protected int[] getTabTitleResIds() {
        return TITLES;
    }

    @Override
    protected Fragment makeFragment(int position) {
        return IssueMilestoneListFragment.newInstance(mRepoOwner, mRepoName, position == 1, false);
    }

    @Override
    public void onMilestoneSelected(@Nullable Milestone milestone) {
        FragmentActivity activity = getActivity();
        if (activity instanceof IssueMilestoneListFragment.SelectionCallback) {
            ((IssueMilestoneListFragment.SelectionCallback) activity)
                    .onMilestoneSelected(milestone);
        }
        dismiss();
    }
}
