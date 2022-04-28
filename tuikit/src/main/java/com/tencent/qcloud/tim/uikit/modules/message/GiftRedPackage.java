package com.tencent.qcloud.tim.uikit.modules.message;

public class GiftRedPackage {


    /**
     * gift : {"amount":1,"icon":"https://hashex-photo.oss-cn-hangzhou.aliyuncs.com/gift/kaidimao.png","name":"凯蒂猫"}
     * msgType : gift
     */

    private GiftBean gift;
    private String msgType;

    public GiftBean getGift() {
        return gift;
    }

    public void setGift(GiftBean gift) {
        this.gift = gift;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public static class GiftBean {
        /**
         * amount : 1
         * icon : https://hashex-photo.oss-cn-hangzhou.aliyuncs.com/gift/kaidimao.png
         * name : 凯蒂猫
         */

        private int amount;
        private String icon;
        private String name;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
