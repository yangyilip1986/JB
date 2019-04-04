package com.jiubang.p2p;

public class AppConstants {

    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";// App唯一标识

    public static String PATH_LOG_PATH = "/jiubang/Log/";// 日志默认保存目录

    public static String PATH_UPDATE_APK = "/jiubang/Update/";// 软件更新默认保存目录

    public static final int TIME = 60000;// 手势密码出现持续时间

    public static final int PRODUCT_STATUS_REPAY = 1;// 还款中

    public static final int PRODUCT_STATUS_SOLD_OUT = 2;// 已售罄

    public static final int PRODUCT_STATUS_APPOINTMENT = 3;// 预约

    public static final int PRODUCT_STATUS_END = 4;// 已结束

    public static final int PRODUCT_STATUS_ON_SALE = 5;// 正在售卖

    /**
     * 返回成功
     */
    public static final int SUCCESS = 1;

    /**
     * 返回失败
     */
    public static final int FAILED = 2;

    public final static String HOST = "http://mapp.9bxt.com";
    public final static String SPECIALHOSTWAPP = "http://wapp.9bxt.com";
    public final static String SPECIALHOST = "http://www.9bxt.com";

//	public final static String HOST = "http://192.168.1.198:9080/mapp";// 接口
//	public final static String SPECIALHOST = "http://192.168.1.198:8080";// PC
//	public final static String SPECIALHOSTWAPP = "http://192.168.1.198:9080";// 微信

    //新测试
//    public final static String HOST = "http://mapp.9banker.cn";// 接口
//    public final static String SPECIALHOSTWAPP = "http://wapp.9banker.cn/";// 微信
//	public final static String SPECIALHOST = "http://192.168.1.198:8080";// PC


    public final static String HTTP = "http://";

    /**
     * 首页
     */
    public static final String INDEX = HOST + "/index5";

    /**
     * 理财计划
     */
    public static final String FINANCIAL_PLAN = HOST + "/financial_plan4";

    /**
     * 充值 已绑卡
     */
    public static final String RECHARGE_YES_WAPP = SPECIALHOSTWAPP + "/myaccount/recharge4/rechargePay";
    /**
     * 充值 未绑卡
     */
    public static final String RECHARGE_NO_WAPP = SPECIALHOSTWAPP + "/myaccount4/bankSelect";

    /**
     * 提现 已绑卡
     */
    public static final String CASH_YES_WAPP = SPECIALHOSTWAPP + "/myaccount/withdraws4/withdrawsPay";

    /**
     * 提现 未绑卡
     */
    public static final String CASH_NO_WAPP = SPECIALHOSTWAPP + "/myaccount4/bankSelect";

    /**
     * 绑卡
     */
    public static final String BANK_CARD_WAPP = SPECIALHOSTWAPP + "/myaccount4/bankSelect";

    /**
     * 提现说明
     */
    public static final String CASH_CAPTION = SPECIALHOST + "/WithdrawCaption.html";

    /**
     * 登录
     */
    public static final String SIGNIN = HOST + "/login/noOauth2";

    /**
     * 校验手机号
     */
    public static final String CHECK_PHONE = HOST + "/checkPhone4";

    /**
     * 注册账号
     */
    public static final String SIGNUP = HOST + "/register/noOauth4";

    /**
     * 注册协议
     */
    public static final String REGISTER = SPECIALHOST + "/page/app/register/";

    /**
     * 发送手机验证码
     */
    public static final String GETCODE = HOST + "/reg/sendcode";
    /**
     * 获取验证码
     */
    public static final String CAPTCHA = HOST + "/etc/captcha";

    /**
     * 验证是否登录
     */
    public static final String ISSIGNIN = HOST + "/islogin";

    /**
     * 找回登陆密码时发送验证码
     */
    public static final String SENDCODE = HOST + "/findback/sendcode";

    /**
     * 找回登陆密码时验证手机验证码
     */
    public static final String VERIFY_CODE = HOST + "/findback/find";

    /**
     * 找回登陆密码
     */
    public static final String GET_LOSE = HOST + "/findback/reset";

    /**
     * 产品列表
     */
    public static final String PRODUCTS = HOST + "/product/all5";

    /**
     * 产品详情
     */
    public static final String DETAIL_PRODUCT = HOST + "/product/personal-loan/detail5/";

    /**
     * 收益计算
     */
    public static final String PROFIT_CALCULATOR = HOST + "/profit_calculator4/";

    /**
     * 产品详情(转让)
     */
    public static final String DETAIL_PRODUCT_TRANSFER = HOST + "/product/personal-loan/detail-transfer4/";

    /**
     * 直投宝列表
     */
    public static final String DIRECT = HOST + "/product/direct";

    /**
     * 购买产品
     */
    public static final String BUY = HOST + "/product/";

    /**
     * 公告列表
     */
    public static final String ANNOUNCE = HOST + "/article/announce4";

    /**
     * 媒体报道
     */
    public static final String MEDIA = HOST + "/article/media3";

    /**
     * 获取账户页信息
     */
    public static final String GAIN = HOST + "/my5";

    /**
     * 获取邀请信息
     */
    public static final String INVITE = HOST + "/my/invitation4";

    /**
     * 用户基本信息
     */
    public static final String BASICINFO = HOST + "/my/basic5";

    /**
     * 添加用户名
     */
    public static final String ADDNAME = HOST + "/my/name";

    /**
     * 绑定邮箱
     */
    public static final String BINDEMAIL = HOST + "/my/email/bind";

    /**
     * 修改密码
     */
    public static final String CHANGEPWD = HOST + "/my/password";

    /**
     * 投标详情
     */
    public static final String INVEST_DETAIL = HOST + "/my/invest/detail5";

    /**
     * 投标详情
     */
    public static final String INVEST = HOST + "/my/invest3";

    /**
     * 投资记录回款中
     */
    public static final String INVEST_ORDER = HOST + "/my/order5";

    /**
     * 投资记录待确认
     */
    public static final String INVEST_PENDING = HOST + "/my/order/pending5";

    /**
     * 投资记录已结清
     */
    public static final String INVEST_CLOSED = HOST + "/my/order/closed5";

    /**
     * 投资记录流标
     */
    public static final String INVEST_ABORTED = HOST + "/my/order/aborted";

    /**
     * 交易记录
     */
    public static final String TRANSACTION = HOST + "/my/bill4";

    /**
     * 现金券
     */
    public static final String RED = HOST + "/my/cash4";

    /**
     * 新现金券
     */
    public static final String NEWCASH = HOST + "/product/enablecash3";

    /**
     * 现金券
     */
    public static final String NEWCASH2 = HOST + "/product/enablecash5";

    /**
     * 加息券
     */
    public static final String NEWADDINTEREST = HOST + "/product/enablerate5";

    /**
     * 体验金券
     */
    public static final String ENABLE_EXPERIENCE = HOST + "/product/enable_experience5";

    /**
     * 充值
     */
    public static final String CHARGE = HOST + "/account/recharge";

    /**
     * 充值信息
     */
    public static final String CHARGE_INFO = HOST + "/account/recharge/info3";

    /**
     * 提现
     */
    public static final String CASH = HOST + "/account/withdraw3";

    /**
     * 提现手续费
     */
    public static final String FEE = HOST + "/my/withdraw-bill3";

    /**
     * 实名认证
     */
    public static final String IDCARD = HOST + "/account/register";

    /**
     * 银行卡绑定
     */
    public static final String BANKCARD = HOST + "/account/bindcard";

    /**
     * 检测更新
     */
    public static final String UPDATE = HOST + "/api/version/get";

    /**
     * 服务协议 直接
     */
    public static final String SERVICE_PROTOCOL = "/contract/show/new";

    /**
     * 服务协议 债权
     */
    public static final String SERVICE_PROTOCOL2 = "/tender/transfercontract.html";

    /**
     * 借款协议
     */
    public static final String INVEST_PROTOCOL = HOST + "/protocol/contract/";

    /**
     * 获取首页轮播图数据
     */
    public static final String GET_SLIDE_IMAGE = HOST + "/appadv";

    /**
     * 关于我们
     */
    public static final String ABOUT_US = SPECIALHOST + "/page/app/about/";

    /**
     * 安全保障
     */
    public static final String SECURITY = SPECIALHOST + "/page/app/security/";

    /**
     * 帮助中心
     */
    public static final String FAQ = SPECIALHOST + "/page/app/faq/";

    /**
     * 债权转让 可转让
     */
    public static final String TRANSFER_CAN = HOST + "/claims/transfercan3";

    /**
     * 债权转让 转让中
     */
    public static final String TRANSFER_ING = HOST + "/claims/transfering4";

    /**
     * 债权转让 已转让
     */
    public static final String TRANSFER_ALREADY = HOST + "/claims/transferalready4";

    /**
     * 债权转让 发布转让
     */
    public static final String TRANSFER_PUBLISH = HOST + "/claims/transferPublish3";

    /**
     * 债权转让 转让详细信息
     */
    public static final String TRANSFER_INFO = HOST + "/claims/transfer/info3";

    /**
     * 账户中心 修改绑定手机号
     */
    public static final String UPDATE_PHONE = HOST + "/my/phone3";

    /**
     * 账户中心 修改绑定手机号 发送验证码
     */
    public static final String UPDATE_PHONE_SENDCODE = HOST + "/my/phone/sendcode3";

    /**
     * 账户中心 修改绑定手机号 验证验证码
     */
    public static final String UPDATE_PHONE_CHECKEDCODE = HOST + "/my/phone/checkedcode3";

    /**
     * 消息中心
     */
    public static final String MESSAGE_CENTER = HOST + "/my/center4";

    /**
     * 消息中心 已读
     */
    public static final String MESSAGE_CENTER_ALREAD = HOST
            + "/my/center/alread";

    /**
     * 月报
     */
    public static final String MESSAGE_MONTHLY_REPORT = HOST
            + "/my/center/monthly/report";

    /**
     * 用户是否有借款
     */
    public static final String ACCOUNT_ISLOAN = HOST + "/account/isloan";

    /**
     * 我的积分
     */
    public static final String MY_INTEGRAL = HOST + "/my/integral";
    /**
     * 我的积分详情
     */
    public static final String MY_INTEGRAL_DETAIL = HOST
            + "/my/integral/detail";
    /**
     * 积分商城
     */
    public static final String MY_INTEGRAL_MALL = HOST + "/my/integral/mall4";
    /**
     * 积分商城 兑换
     */
    public static final String MY_INTEGRAL_MALL_EXCHANGE = HOST
            + "/my/integral/mall/exchange";

    /**
     * 会员中心
     */
    public static final String MEMBER_CENTER = HOST + "/member/center4";

    /**
     * 会员等级积分
     */
    public static final String VIP = HOST + "/vip2";

    /**
     * 会员积分领取
     */
    public static final String GET_POINTS = HOST + "/vip/getPoints2";

    /**
     * 会员生日红包
     */
    public static final String BIRTHDAY = HOST + "/birthday/red2";

    /**
     * 会员生日红包领取
     */
    public static final String GET_BIRTHDAY_RED = HOST + "/birthday/get_red2";

    /**
     * 签到
     */
    public static final String MY_USER_SIGN = HOST + "/my/user/sign2";

    /**
     *
     */
    public static final String POINT_ALL = HOST + "/point/all";

    /**
     * 理财计划ABC产品详情
     */
    public static final String PLAN_B_DETAIL_PRODUCT = HOST + "/financial_plan_abc4/";

    /**
     * 理财计划ABC产品投资
     */
    public static final String PLAN_B_TENDER_PRODUCT = HOST + "/financial_plan_abc_tender4/";
    /**
     * "我的"界面睁眼闭眼
     */
    public static final String ACCOUNT_SEE = HOST + "/display_switch4";
}
