package com.example.wangzhi6.Parse;


public class MainContract {
    public interface IMainView {
        void showProgress();
        void hideProgress();
        void onParseSuccess(String url);
        void onParseError(Throwable t);
    }

    public interface IParsePresenter {
        void parseIcon(String url);
        void unsubscribe();
    }
}
