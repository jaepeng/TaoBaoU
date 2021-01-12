package com.example.taobaou.model.domain;

import java.util.List;

public class ImageIdentifyResult {

    /**
     * result : [{"score":0.984252,"root":"商品-穿戴","keyword":"围巾","baike_info":{"baike_url":"http://baike.baidu.com/item/%E5%9B%B4%E5%B7%BE/1216","image_url":"https://bkimg.cdn.bcebos.com/pic/1c950a7b02087bf40ad12dee499b402c11dfa9ec6129","description":"围巾，可围在脖子上的长条形、三角形、方形等形状，面料一般采用羊毛、棉、丝、莫代尔、人棉、腈纶、涤纶等材料，通常于保暖，也可因美观、清洁或是宗教而穿戴。"}},{"score":0.707486,"root":"商品-服装","keyword":"围巾/披肩","baike_info":{}},{"score":0.426737,"root":"商品-穿戴","keyword":"毛线围脖","baike_info":{}},{"score":0.213277,"root":"商品-穿戴","keyword":"纱巾","baike_info":{"baike_url":"http://baike.baidu.com/item/%E7%BA%B1%E5%B7%BE/4578382","description":"纱巾是一种纺织成品，特点是经纬线稀疏有网眼。主要是女性佩戴，用作时尚装饰和保暖的作用。根据地域和文化习惯的不同有不同的解释方法，很多人与围巾,丝巾的相混。"}},{"score":0.025961,"root":"商品-服装","keyword":"t恤","baike_info":{"baike_url":"http://baike.baidu.com/item/T%E6%81%A4/1960195","image_url":"https://bkimg.cdn.bcebos.com/pic/0df431adcbef76099d4a3fb425dda3cc7cd99e5c","description":"T恤，又称T恤衫、丅字衫，是春夏季人们最喜欢的服装之一，特别是烈日炎炎，酷暑难耐的盛夏，T恤衫以其自然、舒适、潇洒又不失庄重之感的优点而逐步替代昔日男士们穿件背心或汗衫外加一件短袖衬衫或香港衫的模式出现在社交场合，成为人们乐于穿着的时令服装。目前已成为全球男女老幼均爱穿着的时髦装。据说全世界年销售量已高达数十亿件，与牛仔裤构成了全球最流行、穿着人数最多的服装。"}}]
     * log_id : 2415561669392098260
     * result_num : 5
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
         * score : 0.984252
         * root : 商品-穿戴
         * keyword : 围巾
         * baike_info : {"baike_url":"http://baike.baidu.com/item/%E5%9B%B4%E5%B7%BE/1216","image_url":"https://bkimg.cdn.bcebos.com/pic/1c950a7b02087bf40ad12dee499b402c11dfa9ec6129","description":"围巾，可围在脖子上的长条形、三角形、方形等形状，面料一般采用羊毛、棉、丝、莫代尔、人棉、腈纶、涤纶等材料，通常于保暖，也可因美观、清洁或是宗教而穿戴。"}
         */

        private double score;
        private String root;
        private String keyword;
        private BaikeInfoBean baike_info;

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

        public BaikeInfoBean getBaike_info() {
            return baike_info;
        }

        public void setBaike_info(BaikeInfoBean baike_info) {
            this.baike_info = baike_info;
        }

        public static class BaikeInfoBean {
            /**
             * baike_url : http://baike.baidu.com/item/%E5%9B%B4%E5%B7%BE/1216
             * image_url : https://bkimg.cdn.bcebos.com/pic/1c950a7b02087bf40ad12dee499b402c11dfa9ec6129
             * description : 围巾，可围在脖子上的长条形、三角形、方形等形状，面料一般采用羊毛、棉、丝、莫代尔、人棉、腈纶、涤纶等材料，通常于保暖，也可因美观、清洁或是宗教而穿戴。
             */

            private String baike_url;
            private String image_url;
            private String description;

            public String getBaike_url() {
                return baike_url;
            }

            public void setBaike_url(String baike_url) {
                this.baike_url = baike_url;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
    }
}
