package com.example.taobaou.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.domain.Histories;
import com.example.taobaou.model.domain.IBaseInfo;
import com.example.taobaou.model.domain.SearchRcommend;
import com.example.taobaou.model.domain.SearchResult;
import com.example.taobaou.model.message.MessageCode;
import com.example.taobaou.model.message.MessageEvent;
import com.example.taobaou.presenter.ISearchPresenter;
import com.example.taobaou.ui.adapter.LinerItemContentAdapter;
import com.example.taobaou.ui.custom.TextFlowLayout;
import com.example.taobaou.utils.KeyboardUtils;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.utils.SizeUtils;
import com.example.taobaou.utils.TickUtils;
import com.example.taobaou.utils.ToastUtsils;
import com.example.taobaou.view.ISearchPageCallBack;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchPageCallBack, TextFlowLayout.onFlowTextItemClick, LinerItemContentAdapter.OnListItemClickListener {

    private ISearchPresenter mSearchPresenter;
    @BindView(R.id.search_history_view)
    public TextFlowLayout mHistoriesView;
    @BindView(R.id.search_recommed_view)
    public TextFlowLayout mRecommendView;
    @BindView(R.id.search_histories_container)
    public LinearLayout mSearchHistoryContainer;
    @BindView(R.id.serach_recommend_container)
    public LinearLayout mSearchRecommendViewContainer;
    @BindView(R.id.search_delete_histories)
    public ImageView mdeletHistoriesImage;
    @BindView(R.id.search_result_list)
    public RecyclerView msearchResultList;
    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout msearchRefreshContainer;
    @BindView(R.id.search_input_box)
    public EditText mSearchInputEdt;
    @BindView(R.id.search_input_remove_btn)
    public ImageView mSearchInputRemovelIv;
    @BindView(R.id.search_btn)
    public TextView mSearchBtn;
    public static final String TAG = "SearchFragmentjae";
    public String templeKeyWord = "";


    private LinerItemContentAdapter mSearchResultAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    //处理EventBus传递来的数据
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessageCode() == MessageCode.ANALYSRESULTSEARCH) {
            String keyword = messageEvent.getMkeyword();
            if (!TextUtils.isEmpty(keyword)) {
                Log.d(TAG, "onEvent: +设置mSearchInputEdt文字" + keyword);
                if (!TextUtils.isEmpty(keyword)) {
                    mSearchInputEdt.setText(keyword);
                    startSearch(keyword);
                }


            } else if (keyword.trim().length()==0){
                Toast.makeText(getContext(), "没有识别出来,请手动搜索!", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    protected int getRootViewResid() {
        return R.layout.fragment_serarch;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        mHistoriesView.setOnFlowTextItemClick(this);
        msearchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        msearchRefreshContainer.setVisibility(View.GONE);
        mSearchResultAdapter = new LinerItemContentAdapter();
        mSearchInputEdt.setSaveEnabled(false);
        msearchResultList.setAdapter(mSearchResultAdapter);
        mSearchResultAdapter.setOnListItemClickListener(this);
        //设置刷新控件
        msearchRefreshContainer.setEnableLoadmore(true);
        msearchRefreshContainer.setEnableRefresh(false);
        msearchRefreshContainer.setEnableOverScroll(true);
        msearchResultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(), 1.5f);
            }
        });


    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_bar_layout, container, false);
    }

    @Override
    protected void initListener() {

        /**
         * 当文本框显示的内容是搜索时,则可以进行搜索,不然就不可以
         *
         */
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasInputContainSpace(false)) {
                    //搜索,发起搜索
                    if (mSearchPresenter != null) {
                        startSearch(mSearchInputEdt.getText().toString());
//                        mSearchPresenter.doSearch(mSearchInputEdt.getText().toString());
                        KeyboardUtils.hide(getContext(), v);
                    }

                } else {
                    //取消,拉起键盘
                    KeyboardUtils.hide(getContext(), v);


                }
            }
        });


        /**
         *   对removeImage设置点击事件让它清除edt框中的所有字符
         */
        mSearchInputRemovelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchInputEdt.setText("");
                //回到历史记录界面
                switch2HistoryPage();
            }
        });


        /**
         * 监听输入框的内容变化
         */
        mSearchInputEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtils.d(this, "");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                LogUtils.d(SearchFragment.class,"s---->"+s+"    count ------->"+count+"    start ------->"+start+"    before ------->"+before);
                //如果长度不为0,显示删除按钮,否则不显示

                mSearchInputRemovelIv.setVisibility(hasInputContainSpace(true) ? View.VISIBLE : View.GONE);//就算只空格也要显示出来
                mSearchBtn.setText(hasInputContainSpace(false) ? "搜索" : "取消");//搜索的时候不能含有空格


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**清空历史记录按钮的事件
         *
         */
        mdeletHistoriesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchPresenter.delHistories();
            }
        });
        /**
         * 加载更多内容的事件
         */
        msearchRefreshContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mSearchPresenter != null) {
                    mSearchPresenter.loaderMore();
                }
            }
        });

        /**
         * EditText的搜索事件
         */
        mSearchInputEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                    //如果是搜索要求,则去请求数据
                    String inputKeyword = v.getText().toString();
                    if (!TextUtils.isEmpty(inputKeyword)) {

//                        mSearchPresenter.doSearch(inputKeyword);
                        startSearch(inputKeyword);
                    } else {
                        return false;
                    }

                }
                return false;
            }
        });
        //历史记录点击事件
        mHistoriesView.setOnFlowTextItemClick(this);
        //热门推荐点击事件
        mRecommendView.setOnFlowTextItemClick(this);
    }

    private void switch2HistoryPage() {


        //切换到历史和推荐界面
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
        if (mRecommendView.getContentSize() != 0) {
            mSearchRecommendViewContainer.setVisibility(View.VISIBLE);
        } else {
            mSearchRecommendViewContainer.setVisibility(View.GONE);

        }
        //内容要被隐藏掉
        msearchResultList.setVisibility(View.GONE);

    }

    private boolean hasInputContainSpace(boolean containSpace) {
        if (containSpace) {
            return mSearchInputEdt.getText().toString().length() > 0;

        } else {
            return mSearchInputEdt.getText().toString().trim().length() > 0;

        }
    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresentManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //获取推荐关键字：判断一下有没有搜索记录
//        mSearchPresenter.doSearch("键盘");
        mSearchPresenter.getHistories();
        mSearchPresenter.getRecommendWords();
    }

    @Override
    public void onHistoriesLoad(Histories histories) {
        //历史记录数据从这里回来
        LogUtils.d(this, "histories-------->" + histories);
        if (histories == null || histories.getHistories().size() == 0) {
            //没有历史记录
            mSearchHistoryContainer.setVisibility(View.GONE);
        } else {
            //有历史记录
            mSearchHistoryContainer.setVisibility(View.VISIBLE);
            mHistoriesView.setTextList(histories.getHistories());
            mSearchRecommendViewContainer.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onHistoriesDeleted() {
        //历史记录 被删除了
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
            mSearchRecommendViewContainer.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSearchLoadSuccess(SearchResult result) {
        if (result != null) {
            setUpState(State.SUCCESS);
            msearchResultList.setVisibility(View.VISIBLE);
        }
        //查找数据从这里回来

//        LogUtils.d(this,"result-------->"+result);
        //隐藏掉历史记录/推荐列表
        mSearchHistoryContainer.setVisibility(View.GONE);
        mSearchRecommendViewContainer.setVisibility(View.GONE);
        //显示搜索结果界面
        msearchRefreshContainer.setVisibility(View.VISIBLE);
        //设置数据
        try {
            mSearchResultAdapter.setData(result.getData()
                    .getTbk_dg_material_optional_response()
                    .getResult_list()
                    .getMap_data());

        } catch (Exception e) {
            e.printStackTrace();
            //切换到搜索内容为空
            LogUtils.d(this, "//切换到搜索内容为空");
            setUpState(State.EMPTY);


        }
    }

    @Override
    protected void onRetryClick() {
        //重新加载
        if (mSearchPresenter != null) {
            mSearchPresenter.research();
        }
    }

    @Override
    public void onMoreLoadError() {
        msearchRefreshContainer.finishLoadmore();
        ToastUtsils.showToast("网路异常请稍后重试");


    }

    @Override
    public void onMoreLoadEmpty() {
        msearchRefreshContainer.finishLoadmore();
        ToastUtsils.showToast("没有跟多数据了");
    }

    @Override
    public void onMoreLoaded(SearchResult moreresult) {
        msearchRefreshContainer.finishLoadmore();
        //加载更多的结果
        //拿到结果,添加到适配器尾部
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> moreData = moreresult.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addData(moreData);
        ToastUtsils.showToast("加载新的" + moreData.size() + "数据");

    }

    @Override
    public void onRecommandWordsLoader(List<SearchRcommend.DataBean> recommendWords) {
        //推荐词的数据从这里回来
        LogUtils.d(this, "recommendSize--------->" + recommendWords.size());
        List<String> recommendTextS = new ArrayList<>();
        for (SearchRcommend.DataBean recommendWord : recommendWords) {
            recommendTextS.add(recommendWord.getKeyword());
        }
        if (recommendWords == null || recommendWords.size() == 0) {
            //如果没有推荐数据则将推荐界面隐藏
            mSearchRecommendViewContainer.setVisibility(View.GONE);
        } else {

            mSearchRecommendViewContainer.setVisibility(View.VISIBLE);
        }

        mRecommendView.setTextList(recommendTextS);


    }


    @Override
    public void onError() {
        setUpState(State.ERROR);

    }

    @Override
    public void onLoading() {

        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);

    }

    @Override
    protected void releas() {
        mSearchPresenter.unregisterViewCallback(this);
    }

    @Override
    public void onFlowItemClick(String text) {
        //历史记录或热门内容中的item被点击
        startSearch(text);
    }

    private void startSearch(String text) {
        if (mSearchPresenter != null) {
            msearchResultList.scrollToPosition(0);
            mSearchPresenter.doSearch(text);
            mSearchInputEdt.setText(text);
            mSearchInputEdt.setFocusable(true);
            mSearchInputEdt.requestFocus();
            mSearchInputEdt.setSelection(text.length());
        }
    }

    @Override
    public void onItemClickListener(IBaseInfo item) {
        Log.d("jae_search", "onItemClickListener: " + item.getUrl().toString());
        //当列表内容被点击
        TickUtils.toTicketPage(getContext(), item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
