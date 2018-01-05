package com.versatile.fastclas.payment;



public enum AppEnvironment {


    PRODUCTION {
        @Override
        public String merchant_Key() {
            return "T6uR32Mr";
        }

        @Override
        public String merchant_ID() {
            return "6012943";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "icpqK0c6PA";
        }

        @Override
        public boolean debug() {
            return true;
        }
    };
  /*  SANDBOX {
        @Override
        public String merchant_Key() {
            return "T6uR32Mr";
        }

        @Override
        public String merchant_ID() {
            return "6012943";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "icpqK0c6PA";
        }

        @Override
        public boolean debug() {
            return true;
        }
    };*/

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();

}
