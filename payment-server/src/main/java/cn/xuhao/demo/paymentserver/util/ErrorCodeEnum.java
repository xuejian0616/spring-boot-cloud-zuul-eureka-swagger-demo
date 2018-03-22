package cn.xuhao.demo.paymentserver.util;
public enum ErrorCodeEnum {
    OK(0, "ok"),
    SYSTEM_ERROR(500, "system error"),
    PARAM_ERROR(502, "param error"),
    HTTP_ERROR(599, "http error"),
    CHECKHEALTH_NOTFOUND(1000, "check health not found"),
    STOREHOUSE_NOTFOUND(1001, "store house not found"),
    SUPERMARKET_NOTFOUND(1002, "supermarket not found"),
    SUPMARKETADMIN_NOTFOUND(1003, "supermarket admin not found"),
    USERB_NOTFOUND(1004, "userb not found"),
    PASSWORD_ISNULL(1005, "password is null,please enter"),
    SUPERMARKETADMIN_ISNULL(1006, "supermarketadminlogin is null,please enter"),
    SUPERMARKETADMIN_NOTFOUND(1007, "supermarketadminlogin not found"),
    ADMIN_CREATE_ERROR(10010, "admin.create.error"),
    SUPERMARKET_EXISTED(10021, "supermarket.existed"),
    SUPERMARKET_CREATE_ERROR(10022, "supermarket.create.error"),
    AREA_NOTFOUND(1008, "area not found"),
    BRANCH_NOTFOUND(10081, "branch not found"),
    MOBILE_ERROR(10082, "mobile error"),
    SUPERMARKET_NOTEXIST(10083, "supermarket notExist"),
    USERB_EXIST(10084, "userb exist"),
    USERNAME_ISNULL(10085, "user_name is null"),
    MOBILE_ISNULL(10086, "mobile is null"),
    ACCOUNT_ID_ISNULL(10087, "accountId is null"),
    USERB_CREATE_ERROR(10088, "userb create error"),
    WECHAT_OPENID_NOTFOUND(71000, "wm_market_wechat openid not found"),
    WECHAT_MARKETID_NOTFOUND(71001, "wm_market_wechat marketid not found"),
    GOODS_SAFETY_STOCK_NOTFOUND(2000, "goods_safety_stock not found"),
    GOODS_SAFETY_STOCK_EXIST(2001, "goods_safety_stock exist"),
    GOODS_SAFETY_STOCK_NOT_CHANGED(2002, "goods_safety_stock can't be changed now"),
    GOODS_SAFETY_STOCK_NOT_DELETE(2003, "goods_safety_stock can't be delete now"),
    USER_GOODS_SAFETY_STOCK_INFO_NOTFOUND(3000, "user_goods_safety_stock_info not found"),
    USER_GOODS_SAFETY_STOCK_INFO_EXIST(3001, "user_goods_safety_stock_info exist"),
    USER_GOODS_SAFETY_STOCK_INFO_NOT_CHANGED(3002, "user_goods_safety_stock_info can't be changed now"),
    USER_GOODS_SAFETY_STOCK_INFO_NOT_DELETE(3003, "user_goods_safety_stock_info can't be delete now"),
    STOREPRONUM_EXISTED('鱁', "storepronum info existed");

    private int code;
    private String message;

    private ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
