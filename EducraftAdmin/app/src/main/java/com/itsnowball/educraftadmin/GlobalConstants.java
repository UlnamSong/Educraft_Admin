package com.itsnowball.educraftadmin;

/**
 * Created by Ulnamsong on 2016. 11. 13..
 */
public class GlobalConstants {
    //public static final String AES_KEY = "kr.co.redcore";
    public static final String AES_IVT = "E5173DCA57A9D66E6DA5C0DA69D644AB";

    public static String ADMIN_LOGININFO_KEY = "ADMIN_LOGININFO_KEY";
    public static String TOKEN_ERROR_KEY = "TOKEN_ERROR";

    public static final String IS_VALID_Y = "Y";
    public static final String IS_VALID_N = "N";

    public static final String API_RESULT_SUCCESS = "0000";// 성공.
    public static final String API_RESULT_FAIL = "9999";// 실패.
    public static final String API_FORMAT_JSON = "json";// 실패.

    public static final String MEMBER_TYPE_ADMIN = "A";
    public static final String MEMBER_TYPE_FRONT = "F";
    public static final String MEMBER_TYPE_RECOM = "R";

    public static final String MEBMER_LEVEL_A_SPR = "1"; //슈퍼관리자
    public static final String MEBMER_LEVEL_F_NOR = "11"; //회원(보통)
    public static final String MEBMER_LEVEL_F_TMP = "12"; //회원(휴면)
    public static final String MEBMER_LEVEL_R_NOR = "21"; //추천인

    public static final String PUB_STATUS_R = "R";//발행요청
    public static final String PUB_STATUS_F = "F";//발행중
    public static final String PUB_STATUS_S = "S";//발행완료
    public static final String PUB_STATUS_E = "E";//발행에러
}


