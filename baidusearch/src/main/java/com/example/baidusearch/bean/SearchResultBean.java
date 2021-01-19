package com.example.baidusearch.bean;

import java.util.List;

public class SearchResultBean {


    /**
     * log_id : 4905918174068615175
     * result_num : 5
     * result : [{"score":0.705528,"root":"非自然图像-图像素材","keyword":"非主流空间素材"},{"score":0.55613,"root":"非自然图像-图像素材","keyword":"小窝"},{"score":0.400525,"root":"非自然图像-屏幕截图","keyword":"屏幕截图"},{"score":0.250249,"root":"人物-人物特写","keyword":"人物特写"},{"score":0.070936,"root":"非自然图像-文字图","keyword":"报纸杂志"}]
     */

    private long log_id;
    private int result_num;
    private List<ResultBean> result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getResult_num() {
        return result_num;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * score : 0.705528
         * root : 非自然图像-图像素材
         * keyword : 非主流空间素材
         */

        private double score;
        private String root;
        private String keyword;

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }
}
